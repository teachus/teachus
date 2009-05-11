package dk.teachus.tools.actions;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import dk.teachus.tools.config.MavenNode;
import dk.teachus.tools.config.WorkingDirectoryNode;

public class ReleaseAction implements Action {
	private static final Log log = LogFactory.getLog(ReleaseAction.class);
	
	private ScmCheckoutAction checkoutAction;

	private MavenPackageAction mavenPackage;

	private DetermineVersionAction determineVersion;

	private File projectDirectory;

	private ModifyPomVersionAction modifyPomVersion;

	private ScmTagAction tagAction;
	
	private AddUpgradeSqlStubAction addUpgradeSqlStubAction;

	private final MavenNode maven;

	private final WorkingDirectoryNode workingDirectory;

	private final ScmClient scmClient;

	public ReleaseAction(MavenNode maven, WorkingDirectoryNode workingDirectory, ScmClient scmClient) throws Exception {
		this.maven = maven;
		this.workingDirectory = workingDirectory;
		this.scmClient = scmClient;
	}
	
	public void init() throws Exception {
		log.info("Creating project directory");
		projectDirectory = File.createTempFile("teachus", "", workingDirectory.getWorkingDirectoryFile());
		projectDirectory.delete();
		projectDirectory.mkdir();
		checkoutAction = new ScmCheckoutAction(scmClient, projectDirectory);
		mavenPackage = new MavenPackageAction(maven, projectDirectory);
		determineVersion = new DetermineVersionAction(projectDirectory);
		modifyPomVersion = new ModifyPomVersionAction(projectDirectory, scmClient);
		tagAction = new ScmTagAction(scmClient, projectDirectory);
		addUpgradeSqlStubAction = new AddUpgradeSqlStubAction(projectDirectory, scmClient);
	}

	public void execute() throws Exception {
		checkoutAction.execute();
		
		// Run the tests to see if the quality is high enough to release
		mavenPackage.execute();
		
		// Lets detect the version from the checked out project
		determineVersion.execute();
		String version = determineVersion.getVersion();
		String newVersion = determineVersion.getNextVersion();		
		
		modifyPomVersion.setVersion(version);
		modifyPomVersion.execute();
		
		tagAction.setVersion(version);
		tagAction.execute();
		
		modifyPomVersion.setVersion(newVersion);
		modifyPomVersion.execute();
		
		addUpgradeSqlStubAction.setVersion(newVersion);
		addUpgradeSqlStubAction.execute();
		
		log.info("Finished");		
	}
	
	public void check() throws Exception {
		checkoutAction.check();
		mavenPackage.check();
		determineVersion.check();
		modifyPomVersion.check();
		tagAction.check();
		modifyPomVersion.check();
		addUpgradeSqlStubAction.check();
	}

	public void cleanup() throws Exception {
		checkoutAction.cleanup();
		mavenPackage.cleanup();
		determineVersion.cleanup();
		modifyPomVersion.cleanup();
		tagAction.cleanup();
		modifyPomVersion.cleanup();
		addUpgradeSqlStubAction.cleanup();
		
		log.info("Deleting temporary project directory");
		FileUtils.deleteDirectory(projectDirectory);
	}	

}
