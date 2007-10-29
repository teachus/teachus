package dk.teachus.tools;

import java.io.File;

import dk.teachus.tools.actions.ReleaseAction;
import dk.teachus.tools.config.Configuration;
import dk.teachus.tools.config.MavenNode;
import dk.teachus.tools.config.SubversionReleaseNode;
import dk.teachus.tools.config.SubversionTrunkNode;
import dk.teachus.tools.config.WorkingDirectoryNode;

public class Release {

	public static void main(String[] args) throws Exception {
		File preferenceFile = new File(System.getProperty("user.home"), ".teachus/release.xml");
		
		Configuration configuration = new Configuration();
		configuration.add(new MavenNode());
		configuration.add(new WorkingDirectoryNode());
		configuration.add(new SubversionReleaseNode());
		configuration.add(new SubversionTrunkNode());
		configuration.initialize(preferenceFile);
		
		MavenNode maven = configuration.getNode(MavenNode.class);
		WorkingDirectoryNode workingDirectory = configuration.getNode(WorkingDirectoryNode.class);
		SubversionReleaseNode subversionRelease = configuration.getNode(SubversionReleaseNode.class);
		SubversionTrunkNode subversionTrunk = configuration.getNode(SubversionTrunkNode.class);
		
		Workflow workflow = new Workflow();
		
		workflow.addAction(new ReleaseAction(maven, workingDirectory, subversionRelease, subversionTrunk));
		
		workflow.start();
	}

}
