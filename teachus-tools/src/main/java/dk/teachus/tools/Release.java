package dk.teachus.tools;

import java.io.File;

import dk.teachus.tools.actions.GitCommandScmClient;
import dk.teachus.tools.actions.ReleaseAction;
import dk.teachus.tools.actions.ScmClient;
import dk.teachus.tools.config.Configuration;
import dk.teachus.tools.config.GitNode;
import dk.teachus.tools.config.MavenNode;
import dk.teachus.tools.config.WorkingDirectoryNode;

public class Release {

	public static void main(String[] args) throws Exception {
		File preferenceFile = new File(System.getProperty("user.home"), ".teachus/release.xml");
		
		Configuration configuration = new Configuration();
		configuration.add(new MavenNode());
		configuration.add(new WorkingDirectoryNode());
		configuration.add(new GitNode());
		configuration.initialize(preferenceFile);
		
		MavenNode maven = configuration.getNode(MavenNode.class);
		WorkingDirectoryNode workingDirectory = configuration.getNode(WorkingDirectoryNode.class);
		GitNode git = configuration.getNode(GitNode.class);

		ScmClient scmClient = new GitCommandScmClient(git.getRemoteGitUrl(), git.getCommitterName(), git.getCommitterEmail());
		
		Workflow workflow = new Workflow();
		
		workflow.addAction(new ReleaseAction(maven, workingDirectory, scmClient));
		
		workflow.start();
	}

}
