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
	
	private SubversionCheckoutTrunkAction checkoutTrunk;

	private MavenPackageAction mavenPackage;

	private DetermineVersionAction determineVersion;

	private File projectDirectory;

	private ModifyPomVersionAction modifyPomVersion;

	private SubversionCopyAction subversionCopy;

	public ReleaseAction(MavenNode maven, WorkingDirectoryNode workingDirectory, SubversionReleaseNode subversionRelease, SubversionTrunkNode subversionTrunk) throws Exception {
		log.info("Creating project directory");
		projectDirectory = File.createTempFile("teachus", "", workingDirectory.getWorkingDirectoryFile());
		projectDirectory.delete();
		projectDirectory.mkdir();
		checkoutTrunk = new SubversionCheckoutTrunkAction(projectDirectory, subversionTrunk);
		mavenPackage = new MavenPackageAction(maven, projectDirectory);
		determineVersion = new DetermineVersionAction(projectDirectory);
		modifyPomVersion = new ModifyPomVersionAction(projectDirectory);
		subversionCopy = new SubversionCopyAction(subversionRelease, subversionTrunk);
	}

	public void execute() throws Exception {
		checkoutTrunk.execute();
		
		// Run the tests to see if the quality is high enough to release
		mavenPackage.execute();
		
		// Lets detect the version from the checked out project
		determineVersion.execute();
		String version = determineVersion.getVersion();
		String newVersion = determineVersion.getNextVersion();		
		
		modifyPomVersion.setVersion(version);
		modifyPomVersion.execute();
		
		subversionCopy.setVersion(version);
		subversionCopy.execute();
		
		modifyPomVersion.setVersion(newVersion);
		modifyPomVersion.execute();
		
		log.info("Finished");		
	}
	
	public void check() throws Exception {
		checkoutTrunk.check();
		mavenPackage.check();
		determineVersion.check();
		modifyPomVersion.check();
		subversionCopy.check();
		modifyPomVersion.check();
	}

	public void cleanup() throws Exception {
		checkoutTrunk.cleanup();
		mavenPackage.cleanup();
		determineVersion.cleanup();
		modifyPomVersion.cleanup();
		subversionCopy.cleanup();
		modifyPomVersion.cleanup();		
		
		log.info("Deleting temporary project directory");
		FileUtils.deleteDirectory(projectDirectory);
	}	

}
