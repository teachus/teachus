package dk.teachus.tools.upgrade.actions;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import dk.teachus.tools.upgrade.config.AbstractDeploymentNode;
import dk.teachus.tools.upgrade.config.MavenNode;
import dk.teachus.tools.upgrade.config.SubversionReleaseNode;
import dk.teachus.tools.upgrade.config.TomcatNode;

abstract class UpgradeTeachUsAction implements Action {
	private static final Log log = LogFactory.getLog(UpgradeTeachUsAction.class);

	protected MavenNode maven;
	protected AbstractDeploymentNode deployment;
	protected TomcatNode tomcat;
	protected File workingDirectory;
	protected String version;
	protected SubversionReleaseNode subversion;

	public UpgradeTeachUsAction(MavenNode maven, SubversionReleaseNode subversion, File workingDirectory, AbstractDeploymentNode deployment, TomcatNode tomcat, String version) {
		this.maven = maven;
		this.deployment = deployment;
		this.workingDirectory = workingDirectory;
		this.tomcat = tomcat;
		this.version = version;
		this.subversion = subversion;
	}

	public void execute() throws Exception {
		log.info("Start upgrade of "+getName());
		
		// Create a temporary directory inside the working directory
		File projectDirectory = File.createTempFile("teachus", "", workingDirectory);
		projectDirectory.delete();
		projectDirectory.mkdir();
		
		// Checkout version from subversion
		SubversionCheckoutReleaseAction subversionCheckout = new SubversionCheckoutReleaseAction(subversion, version, projectDirectory);
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
//		UpgradeDatabaseAction upgradeDatabase = new UpgradeDatabaseAction(deployment.getDatabase(), projectDirectory, version);
//		upgradeDatabase.execute();

		// Deploy
		DeployWarFileAction deployWarFile = new DeployWarFileAction(projectDirectory, tomcat, version, getName());
		deployWarFile.execute();
		
		afterDeployment(projectDirectory);
		
		// Clean up 
		log.info("Cleaning up the project directory: "+projectDirectory);
		FileUtils.deleteDirectory(projectDirectory);
		
		log.info("Upgrade completed!!");
	}

	protected abstract String getName();
	
	protected void beforeUpgradeDatabase(File projectDirectory) throws Exception {
	}

	protected void beforePackage(File projectDirectory) throws Exception {
	}
	
	protected void afterDeployment(File projectDirectory) throws Exception {
	}

}
