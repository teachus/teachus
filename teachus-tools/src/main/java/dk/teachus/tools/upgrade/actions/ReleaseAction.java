package dk.teachus.tools.upgrade.actions;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import dk.teachus.tools.upgrade.config.MavenNode;
import dk.teachus.tools.upgrade.config.SubversionReleaseNode;
import dk.teachus.tools.upgrade.config.SubversionTrunkNode;
import dk.teachus.tools.upgrade.config.WorkingDirectoryNode;

public class ReleaseAction implements Action {
	private static final Log log = LogFactory.getLog(ReleaseAction.class);
	
	private MavenNode maven;
	private WorkingDirectoryNode workingDirectory;
	private SubversionReleaseNode subversionRelease;
	private SubversionTrunkNode subversionTrunk;

	public ReleaseAction(MavenNode maven, WorkingDirectoryNode workingDirectory, SubversionReleaseNode subversionRelease, SubversionTrunkNode subversionTrunk) {
		this.maven = maven;
		this.workingDirectory = workingDirectory;
		this.subversionRelease = subversionRelease;
		this.subversionTrunk = subversionTrunk;
	}

	public void execute() throws Exception {
		log.info("Creating project directory");
		File projectDirectory = File.createTempFile("teachus", "", workingDirectory.getWorkingDirectoryFile());
		projectDirectory.delete();
		projectDirectory.mkdir();
				
		SubversionCheckoutTrunkAction checkoutTrunk = new SubversionCheckoutTrunkAction(projectDirectory, subversionTrunk);
		checkoutTrunk.execute();
		
		// Run the tests to see if the quality is high enough to release
		MavenPackageAction mavenPackage = new MavenPackageAction(maven, projectDirectory);
		mavenPackage.execute();
		
		// Lets detect the version from the checked out project
		DetermineVersionAction determineVersion  = new DetermineVersionAction(projectDirectory);
		determineVersion.execute();
		String version = determineVersion.getVersion();
		String newVersion = determineVersion.getNextVersion();		
		
		ModifyPomVersionAction modifyPomVersion = new ModifyPomVersionAction(projectDirectory, version);
		modifyPomVersion.execute();
		
		SubversionCopyAction subversionCopy = new SubversionCopyAction(subversionRelease, subversionTrunk, version);
		subversionCopy.execute();
		
		modifyPomVersion = new ModifyPomVersionAction(projectDirectory, newVersion);
		modifyPomVersion.execute();
		
		log.info("Deleting temporary project directory");
		FileUtils.deleteDirectory(projectDirectory);
		
		log.info("Finished");
	}

}
