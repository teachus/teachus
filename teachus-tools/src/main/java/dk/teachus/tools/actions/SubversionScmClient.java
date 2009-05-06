package dk.teachus.tools.actions;

import java.io.File;

import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.internal.io.dav.DAVRepositoryFactory;
import org.tmatesoft.svn.core.internal.io.fs.FSRepositoryFactory;
import org.tmatesoft.svn.core.wc.SVNCommitClient;
import org.tmatesoft.svn.core.wc.SVNCopyClient;
import org.tmatesoft.svn.core.wc.SVNRevision;
import org.tmatesoft.svn.core.wc.SVNUpdateClient;

import dk.teachus.tools.config.SubversionReleaseNode;
import dk.teachus.tools.config.SubversionTrunkNode;

public class SubversionScmClient implements ScmClient {
	
	private SubversionTrunkNode subversionTrunk;
	private SubversionReleaseNode subversionRelease;
	
	public SubversionScmClient(SubversionTrunkNode subversionTrunk, SubversionReleaseNode subversionRelease) {
		this.subversionTrunk = subversionTrunk;
		this.subversionRelease = subversionRelease;
	}

	public SubversionScmClient(SubversionTrunkNode subversionTrunk) {
		this.subversionTrunk = subversionTrunk;
	}

	public SubversionScmClient(SubversionReleaseNode subversionRelease) {
		this.subversionRelease = subversionRelease;
	}

	public void checkout(File workingDirectory, String version) {
		String checkoutUrl; 
			
		if (version == null) {
			checkoutUrl = subversionTrunk.getTrunkUrl();
		} else {
			checkoutUrl = subversionRelease.getReleaseUrl(version);
		}		

		try {
			DAVRepositoryFactory.setup();
			SVNURL url = SVNURL.parseURIDecoded(checkoutUrl);
			SVNUpdateClient updateClient = new SVNUpdateClient(null, null);
			updateClient.doCheckout(url, workingDirectory, SVNRevision.HEAD, SVNRevision.HEAD, true);
		} catch (SVNException e) {
			throw new RuntimeException(e);
		}
	}

	public void tag(File workingDirectory, String version) {
		String trunkUrl = subversionTrunk.getTrunkUrl();
		String releaseUrl = subversionRelease.getReleaseUrl(version);
		
		FSRepositoryFactory.setup();
		DAVRepositoryFactory.setup();
		
		
		try {
			SVNURL srcUrl = SVNURL.parseURIDecoded(trunkUrl);
			SVNURL dstUrl = SVNURL.parseURIDecoded(releaseUrl);
			
			SVNCopyClient copyClient = new SVNCopyClient(null, null);
			copyClient.doCopy(srcUrl, SVNRevision.HEAD, dstUrl, false, true, "Release "+version);
		} catch (SVNException e) {
			throw new RuntimeException(e);
		}		
	}
	
	public void commit(File projectDirectory, String message, File[] files) {
		try {
			SVNCommitClient commitClient = new SVNCommitClient(null, null);
			commitClient.doCommit(files, false, message, false, false);
		} catch (SVNException e) {
			throw new RuntimeException(e);
		}
	}

}
