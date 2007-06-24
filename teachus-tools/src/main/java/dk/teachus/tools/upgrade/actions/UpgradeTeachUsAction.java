package dk.teachus.tools.upgrade.actions;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import dk.teachus.tools.upgrade.actions.TomcatAction.ProcessAction;
import dk.teachus.tools.upgrade.config.AbstractDeploymentNode;
import dk.teachus.tools.upgrade.config.MavenNode;
import dk.teachus.tools.upgrade.config.SubversionNode;

abstract class UpgradeTeachUsAction implements Action {
	private static final Log log = LogFactory.getLog(UpgradeTeachUsAction.class);

	protected MavenNode maven;
	protected AbstractDeploymentNode deployment;
	protected File workingDirectory;
	protected String version;
	protected SubversionNode subversion;

	public UpgradeTeachUsAction(MavenNode maven, SubversionNode subversion, File workingDirectory, AbstractDeploymentNode deployment, String version) {
		this.maven = maven;
		this.deployment = deployment;
		this.workingDirectory = workingDirectory;
		this.version = version;
		this.subversion = subversion;
	}

	public void execute() throws Exception {
		log.info("Start upgrade of "+getName());
		
		// Stop tomcat
		TomcatAction stopTomcat = new TomcatAction(deployment.getTomcat(), ProcessAction.STOP);
		stopTomcat.execute();
		
		// Create a temporary directory inside the working directory
		File projectDirectory = File.createTempFile("teachus", "", workingDirectory);
		projectDirectory.delete();
		projectDirectory.mkdir();
		
		// Checkout version from subversion
		SubversionCheckoutAction subversionCheckout = new SubversionCheckoutAction(subversion, version, projectDirectory);
		subversionCheckout.execute();
		
		// Configure database
		ConfigureTeachUsDatabaseAction configureDatabase = new ConfigureTeachUsDatabaseAction(projectDirectory, deployment.getDatabase());
		configureDatabase.execute();
		
		beforePackage(projectDirectory);
		
		// Package
		MavenPackageAction mavenPackage = new MavenPackageAction(maven, projectDirectory);
		mavenPackage.execute();
		
		beforeUpgradeDatabase(projectDirectory);
		
		// Upgrade database
		UpgradeDatabaseAction upgradeDatabase = new UpgradeDatabaseAction(deployment.getDatabase(), projectDirectory, version);
		upgradeDatabase.execute();

		// Deploy
		DeployWarFileAction deployWarFile = new DeployWarFileAction(projectDirectory, deployment.getTomcat(), version);
		deployWarFile.execute();
		
		afterDeployment(projectDirectory);

		// Start tomcat
		TomcatAction startTomcat = new TomcatAction(deployment.getTomcat(), ProcessAction.START);
		startTomcat.setMaxHeap(getMaxHeap());
		startTomcat.setMaxPerm(getMaxPerm());
		startTomcat.execute();
		
		// Clean up 
		log.info("Cleaning up the project directory: "+projectDirectory);
		FileUtils.deleteDirectory(projectDirectory);
		
		log.info("Upgrade completed!!");
	}
	
	protected int getMaxPerm() {
		return 64;
	}

	protected int getMaxHeap() {
		return 64;
	}

	protected abstract String getName();
	
	protected void beforeUpgradeDatabase(File projectDirectory) throws Exception {
	}

	protected void beforePackage(File projectDirectory) throws Exception {
	}
	
	protected void afterDeployment(File projectDirectory) throws Exception {
	}

}
