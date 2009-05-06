package dk.teachus.tools.actions;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import org.spearce.jgit.errors.NotSupportedException;
import org.spearce.jgit.errors.TransportException;
import org.spearce.jgit.lib.Commit;
import org.spearce.jgit.lib.Constants;
import org.spearce.jgit.lib.GitIndex;
import org.spearce.jgit.lib.NullProgressMonitor;
import org.spearce.jgit.lib.ObjectId;
import org.spearce.jgit.lib.ObjectLoader;
import org.spearce.jgit.lib.ObjectWriter;
import org.spearce.jgit.lib.PersonIdent;
import org.spearce.jgit.lib.Ref;
import org.spearce.jgit.lib.RefComparator;
import org.spearce.jgit.lib.RefUpdate;
import org.spearce.jgit.lib.Repository;
import org.spearce.jgit.lib.RepositoryConfig;
import org.spearce.jgit.lib.Tag;
import org.spearce.jgit.lib.Tree;
import org.spearce.jgit.lib.TreeEntry;
import org.spearce.jgit.lib.WorkDirCheckout;
import org.spearce.jgit.lib.GitIndex.Entry;
import org.spearce.jgit.transport.FetchResult;
import org.spearce.jgit.transport.RefSpec;
import org.spearce.jgit.transport.RemoteConfig;
import org.spearce.jgit.transport.RemoteRefUpdate;
import org.spearce.jgit.transport.Transport;
import org.spearce.jgit.transport.URIish;

public class GitScmClient implements ScmClient {

	private String remoteGitRepository;
	private String committer;
	
	public GitScmClient(String remoteGitRepository, String committer) {
		this.remoteGitRepository = remoteGitRepository;
		this.committer = committer;
	}
	
	public void checkout(File projectDirectory, String version) {
		if (projectDirectory.exists() == false) {
			projectDirectory.mkdirs();
		}
		
		if (projectDirectory.isDirectory() == false) {
			throw new IllegalArgumentException("Working directory must be a directory");
		}
		
		File gitDir = new File(projectDirectory, ".git");
		
		try {
			Repository localRep = new Repository(gitDir);
			localRep.create();
			localRep.writeSymref(Constants.HEAD, Constants.R_HEADS + Constants.MASTER);
			
			localRep.getConfig().setBoolean("core", null, "bare", false);
			localRep.getConfig().save();
			
			RemoteConfig rc = new RemoteConfig(localRep.getConfig(), "origin");
			rc.addURI(new URIish(remoteGitRepository));
			rc.addFetchRefSpec(new RefSpec().setForceUpdate(true)
					.setSourceDestination(Constants.R_HEADS + "*",
							Constants.R_REMOTES + "origin" + "/*"));
			rc.update(localRep.getConfig());

			// setup the default remote branch for branchName
			localRep.getConfig().setString(RepositoryConfig.BRANCH_SECTION,
					Constants.MASTER, "remote", "origin");
			localRep.getConfig().setString(RepositoryConfig.BRANCH_SECTION,
					Constants.MASTER, "merge", Constants.R_HEADS + Constants.MASTER);
			
			localRep.getConfig().save();
			
			final Transport tn = Transport.open(localRep, "origin");
			final FetchResult r;
			try {
				r = tn.fetch(new NullProgressMonitor(), null);
			} finally {
				tn.close();
			}
			
			Ref branch = guessHEAD(r);
			doCheckout(branch, localRep);
			
			// Change to the tag
			if (version != null) {
				Ref ref = localRep.getRef("refs/tags/"+version);
				
				if (ref == null) {
					throw new IllegalArgumentException("The version doesn't exist: "+version);
				}
				
				Commit newCommit = localRep.mapCommit(ref.getName());
				Commit oldCommit = localRep.mapCommit(Constants.HEAD);
				Tree oldTree = oldCommit.getTree();
				GitIndex index = localRep.getIndex();
				Tree newTree = newCommit.getTree();
				WorkDirCheckout workDirCheckout = new WorkDirCheckout(localRep, localRep.getWorkDir(), oldTree, index, newTree);
				workDirCheckout.checkout();
				index.write();
				localRep.writeSymref(Constants.HEAD, ref.getName());
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		} catch (URISyntaxException e) {
			throw new RuntimeException(e);
		}
	}

	public void tag(File projectDirectory, String version) {
		try {
			Repository rep = new Repository(new File(projectDirectory, ".git"));
			ObjectId head = rep.resolve(Constants.HEAD);
			ObjectLoader ldr = rep.openObject(head);
			String type = Constants.typeString(ldr.getType());
			
			Tag tag = new Tag(rep);
			tag.setObjId(head);
			tag.setType(type);
			tag.setTagger(new PersonIdent(rep));
			tag.setMessage("Release "+version);
			tag.setTag(version);
			tag.tag();

			List<RefSpec> specs = new ArrayList<RefSpec>();
			specs.add(Transport.REFSPEC_TAGS);
			push(rep, specs);
		} catch (IOException e) {
			throw new RuntimeException(e);
		} catch (URISyntaxException e) {
			throw new RuntimeException(e);
		}
	}
	
	public void commit(File projectDirectory, String message, File[] files) {
		try {
			Repository rep = new Repository(new File(projectDirectory, ".git"));
			Map<Repository, Tree> treeMap = new HashMap<Repository, Tree>();
			
			track(rep, files);
			
			prepareTrees(projectDirectory, rep, files, treeMap);
			
			doCommits(message, treeMap);

			List<RefSpec> specs = new ArrayList<RefSpec>();
			specs.add(Transport.REFSPEC_PUSH_ALL);
			push(rep, specs);
		} catch (IOException e) {
			throw new RuntimeException(e);
		} catch (URISyntaxException e) {
			throw new RuntimeException(e);
		}
	}

	private void push(Repository rep, List<RefSpec> specs)
			throws NotSupportedException, URISyntaxException, IOException,
			TransportException {
		final List<Transport> transports = Transport.openAll(rep, "origin");
		for (final Transport transport : transports) {
			transport.setPushThin(Transport.DEFAULT_PUSH_THIN);
			final Collection<RemoteRefUpdate> toPush = transport.findRemoteRefUpdatesFor(specs);

			try {
				transport.push(new NullProgressMonitor(), toPush);
			} finally {
				transport.close();
			}
		}
	}

	private Ref guessHEAD(final FetchResult result) {
		final Ref idHEAD = result.getAdvertisedRef(Constants.HEAD);
		final List<Ref> availableRefs = new ArrayList<Ref>();
		Ref head = null;
		for (final Ref r : result.getAdvertisedRefs()) {
			final String n = r.getName();
			if (!n.startsWith(Constants.R_HEADS))
				continue;
			availableRefs.add(r);
			if (idHEAD == null || head != null)
				continue;
			if (r.getObjectId().equals(idHEAD.getObjectId()))
				head = r;
		}
		Collections.sort(availableRefs, RefComparator.INSTANCE);
		if (idHEAD != null && head == null)
			head = idHEAD;
		return head;
	}

	private void doCheckout(final Ref branch, Repository localRep) throws IOException {
		if (!Constants.HEAD.equals(branch.getName()))
			localRep.writeSymref(Constants.HEAD, branch.getName());

		final Commit commit = localRep.mapCommit(branch.getObjectId());
		final RefUpdate u = localRep.updateRef(Constants.HEAD);
		u.setNewObjectId(commit.getCommitId());
		u.forceUpdate();

		final GitIndex index = new GitIndex(localRep);
		final Tree tree = commit.getTree();
		final WorkDirCheckout co;

		co = new WorkDirCheckout(localRep, localRep.getWorkDir(), index, tree);
		co.checkout();
		index.write();
	}

	private void prepareTrees(File projectDirectory, Repository repository, File[] selectedItems,
			Map<Repository, Tree> treeMap) throws IOException,
			UnsupportedEncodingException {
		for (File file : selectedItems) {
			// System.out.println("\t" + file);

			Tree projTree = treeMap.get(repository);
			if (projTree == null) {
				projTree = repository.mapTree(Constants.HEAD);
				if (projTree == null)
					projTree = new Tree(repository);
				treeMap.put(repository, projTree);
			}
			GitIndex index = repository.getIndex();
			
			String repoRelativePath = file.getAbsolutePath().replace(projectDirectory.getAbsolutePath(), "");
			if (repoRelativePath.startsWith(System.getProperty("file.separator"))) {
				repoRelativePath = repoRelativePath.substring(1);
			}
 			
			TreeEntry treeMember = projTree.findBlobMember(repoRelativePath);
			// we always want to delete it from the current tree, since if it's
			// updated, we'll add it again
			if (treeMember != null)
				treeMember.delete();

			Entry idxEntry = index.getEntry(repoRelativePath);				
			
			if (idxEntry != null) {
				projTree.addFile(repoRelativePath);
				TreeEntry newMember = projTree.findBlobMember(repoRelativePath);

				newMember.setId(idxEntry.getObjectId());
				System.out.println("New member id for " + repoRelativePath
						+ ": " + newMember.getId() + " idx id: "
						+ idxEntry.getObjectId());
			}
		}
	}

	private void doCommits(String commitMessage,
			Map<Repository, Tree> treeMap) throws IOException {

		final Date commitDate = new Date();
		final TimeZone timeZone = TimeZone.getDefault();

		final PersonIdent authorIdent = new PersonIdent(committer);
		final PersonIdent committerIdent = new PersonIdent(committer);

		for (java.util.Map.Entry<Repository, Tree> entry : treeMap.entrySet()) {
			Tree tree = entry.getValue();
			Repository repo = tree.getRepository();
			writeTreeWithSubTrees(tree);

			ObjectId currentHeadId = repo.resolve(Constants.HEAD);
			ObjectId[] parentIds;
			if (currentHeadId != null)
				parentIds = new ObjectId[] { currentHeadId };
			else
				parentIds = new ObjectId[0];
			Commit commit = new Commit(repo, parentIds);
			commit.setTree(tree);
			commit.setMessage(commitMessage);
			commit.setAuthor(new PersonIdent(authorIdent, commitDate, timeZone));
			commit.setCommitter(new PersonIdent(committerIdent, commitDate, timeZone));

			ObjectWriter writer = new ObjectWriter(repo);
			commit.setCommitId(writer.writeCommit(commit));

			final RefUpdate ru = repo.updateRef(Constants.HEAD);
			ru.setNewObjectId(commit.getCommitId());
			ru.setRefLogMessage(buildReflogMessage(commitMessage), false);
			if (ru.forceUpdate() == RefUpdate.Result.LOCK_FAILURE) {
				throw new RuntimeException("Failed to update " + ru.getName()
						+ " to commit " + commit.getCommitId() + ".");
			}
		}
	}

	private void writeTreeWithSubTrees(Tree tree) {
		if (tree.getId() == null) {
			System.out.println("writing tree for: " + tree.getFullName());
			try {
				for (TreeEntry entry : tree.members()) {
					if (entry.isModified()) {
						if (entry instanceof Tree) {
							writeTreeWithSubTrees((Tree) entry);
						}
					}
				}
				ObjectWriter writer = new ObjectWriter(tree.getRepository());
				tree.setId(writer.writeTree(tree));
			} catch (IOException e) {
				throw new RuntimeException("Writing trees", e);
			}
		}
	}

	private String buildReflogMessage(String commitMessage) {
		String firstLine = commitMessage;
		int newlineIndex = commitMessage.indexOf("\n");
		if (newlineIndex > 0) {
			firstLine = commitMessage.substring(0, newlineIndex);
		}
		String commitStr = "\tcommit: ";
		String message = commitStr + firstLine;
		return message;
	}
	
	private void track(Repository rep, File[] files) throws IOException {
		GitIndex index = rep.getIndex();
		
		for (File file : files) {
			Entry entry = index.add(rep.getWorkDir(), file);
			entry.setAssumeValid(false);
		}
		
		index.write();
	}

}
