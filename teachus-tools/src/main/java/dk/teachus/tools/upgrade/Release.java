package dk.teachus.tools.upgrade;

import java.io.File;

import dk.teachus.tools.upgrade.actions.ReleaseAction;
import dk.teachus.tools.upgrade.config.Configuration;
import dk.teachus.tools.upgrade.config.MavenNode;
import dk.teachus.tools.upgrade.config.SubversionReleaseNode;
import dk.teachus.tools.upgrade.config.SubversionTrunkNode;
import dk.teachus.tools.upgrade.config.WorkingDirectoryNode;

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
		
		ReleaseAction release = new ReleaseAction(maven, workingDirectory, subversionRelease, subversionTrunk);
		release.execute();
	}

}
