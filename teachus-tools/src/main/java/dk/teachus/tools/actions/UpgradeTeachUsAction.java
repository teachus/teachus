package dk.teachus.tools.actions;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import dk.teachus.tools.config.AbstractDeploymentNode;
import dk.teachus.tools.config.MavenNode;
import dk.teachus.tools.config.SubversionReleaseNode;
import dk.teachus.tools.config.TomcatNode;

abstract class UpgradeTeachUsAction implements Action {
	private static final Log log = LogFactory.getLog(UpgradeTeachUsAction.class);

	private SubversionCheckoutReleaseAction subversionCheckout;

	protected File projectDirectory;

	private ConfigureTeachUsDatabaseAction configureDatabase;

	private ConfigureSmtpServerAction configureSmtpServer;

	private MavenPackageAction mavenPackage;

	private UpgradeDatabaseAction upgradeDatabase;

	private DeployWarFileAction deployWarFile;

	protected final File workingDirectory;

	protected final AbstractDeploymentNode deployment;

	protected final TomcatNode tomcat;
	
	public UpgradeTeachUsAction(MavenNode maven, SubversionReleaseNode subversion, File workingDirectory, AbstractDeploymentNode deployment, TomcatNode tomcat, String version) throws Exception {
		this.workingDirectory = workingDirectory;
		this.deployment = deployment;
		this.tomcat = tomcat;
		
		projectDirectory = File.createTempFile("teachus", "", workingDirectory);
		projectDirectory.delete();
		projectDirectory.mkdir();
		
		subversionCheckout = new SubversionCheckoutReleaseAction(subversion, version, projectDirectory);
		configureDatabase = new ConfigureTeachUsDatabaseAction(projectDirectory, deployment.getDatabase());
		configureSmtpServer = new ConfigureSmtpServerAction(projectDirectory, deployment.getSmtpServer());
		mavenPackage = new MavenPackageAction(maven, projectDirectory);
		upgradeDatabase = new UpgradeDatabaseAction(tomcat.getHost(), deployment.getDatabase(), projectDirectory, version);
		deployWarFile = new DeployWarFileAction(projectDirectory, tomcat, version, getName());
	}

	public void execute() throws Exception {
		log.info("Start upgrade of "+getName());
		
		subversionCheckout.execute();
		
		configureDatabase.execute();
		
		configureSmtpServer.execute();
		
		beforePackage();
		
		mavenPackage.execute();
		
		beforeUpgradeDatabase();
		
		upgradeDatabase.execute();

		deployWarFile.execute();
		
		afterDeployment();		
		
		log.info("Upgrade completed!!");
	}
	
	public final void check() throws Exception {
		subversionCheckout.check();
		configureDatabase.check();
		configureSmtpServer.check();
		mavenPackage.check();
		upgradeDatabase.check();
		deployWarFile.check();
		
		doCheck();
	}
	
	protected void doCheck() throws Exception {
	}
	
	public void cleanup() throws Exception {
		subversionCheckout.cleanup();
		configureDatabase.cleanup();
		configureSmtpServer.cleanup();
		mavenPackage.cleanup();
		upgradeDatabase.cleanup();
		deployWarFile.cleanup();
		
		// Clean up 
		log.info("Cleaning up the project directory: "+projectDirectory);
		FileUtils.deleteDirectory(projectDirectory);		
	}

	protected abstract String getName();
	
	protected void beforeUpgradeDatabase() throws Exception {
	}

	protected void beforePackage() throws Exception {
	}
	
	protected void afterDeployment() throws Exception {
	}

}
