package dk.teachus.tools.actions;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import org.apache.commons.io.FileUtils;

/**
 * Git client using local commands
 */
public class GitCommandScmClient implements ScmClient {

	private String remoteGitRepository;
	private String committerName;
	private String committerEmail;

	public GitCommandScmClient(String remoteGitRepository, String committerName, String committerEmail) {
		this.remoteGitRepository = remoteGitRepository;
		this.committerName = committerName;
		this.committerEmail = committerEmail;
	}

	public void checkout(File projectDirectory, String version) {
		if (projectDirectory.exists()) {
			try {
				FileUtils.deleteDirectory(projectDirectory);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
		
		executeGitCommand(null, new String[] {"clone", remoteGitRepository, projectDirectory.getAbsolutePath()});
		
		executeGitCommand(projectDirectory, new String[] {"config", "user.name", committerName});
		executeGitCommand(projectDirectory, new String[] {"config", "user.email", committerEmail});
		
		if (version != null) {
			executeGitCommand(projectDirectory, new String[] {"checkout", version});
		}
	}

	public void commit(File projectDirectory, String message, File[] files) {
		for (File file : files) {
			String relativePath = file.getAbsolutePath().replace(projectDirectory.getAbsolutePath(), "");
			if (relativePath.startsWith(System.getProperty("file.separator"))) {
				relativePath = relativePath.substring(1);
			}
			executeGitCommand(projectDirectory, new String[] {"add", relativePath});
		}
		
		executeGitCommand(projectDirectory, new String[]{"commit", "-m", message});
		
		executeGitCommand(projectDirectory, new String[]{"push"});
	}

	public void tag(File projectDirectory, String version) {
		executeGitCommand(projectDirectory, new String[]{"tag", version});
		
		executeGitCommand(projectDirectory, new String[]{"push", "--tags"});
	}
	
	private void executeGitCommand(File projectDirectory, String[] params) {
		try {
			String gitCommand[] = new String[params.length + 1];
			gitCommand[0] = "git";
			int i = 1;
			for (String param : params) {
				gitCommand[i++] = param;
			}
			System.out.println("Executing git command: '"+Arrays.toString(gitCommand)+"'");
			
			Process process = Runtime.getRuntime().exec(gitCommand, null, projectDirectory);
			if (process.waitFor() != 0) {
				throw new IOException("Git command didn't finish properly: "+process.exitValue());
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}

}
