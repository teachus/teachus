package dk.teachus.tools.actions;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import dk.teachus.tools.config.AbstractDeploymentNode;
import dk.teachus.tools.config.MavenNode;
import dk.teachus.tools.config.TomcatNode;
;

abstract class UpgradeTeachUsAction implements Action {
	private static final Log log = LogFactory.getLog(UpgradeTeachUsAction.class);

	private AbstractSubversionCheckoutAction subversionCheckout;

	protected File projectDirectory;

	private ConfigureTeachUsDatabaseAction configureDatabase;

	private ConfigureSmtpServerAction configureSmtpServer;

	private MavenPackageAction mavenPackage;

	private DeployWarFileAction deployWarFile;

	protected final File workingDirectory;

	protected final AbstractDeploymentNode deployment;

	protected final TomcatNode tomcat;

	protected final MavenNode maven;

	protected String version;
	
	public UpgradeTeachUsAction(MavenNode maven, File workingDirectory, AbstractDeploymentNode deployment, TomcatNode tomcat, String version) throws Exception {
		this.maven = maven;
		this.workingDirectory = workingDirectory;
		this.deployment = deployment;
		this.tomcat = tomcat;
		this.version = version;
	}
	
	public void init() throws Exception {
		projectDirectory = File.createTempFile("teachus", "", workingDirectory);
		projectDirectory.delete();
		projectDirectory.mkdir();
		
		subversionCheckout = getCheckoutAction();
		subversionCheckout.init();
		configureDatabase = new ConfigureTeachUsDatabaseAction(projectDirectory, deployment.getDatabase());
		configureDatabase.init();
		configureSmtpServer = new ConfigureSmtpServerAction(projectDirectory, deployment.getSmtpServer());
		configureSmtpServer.init();
		mavenPackage = new MavenPackageAction(maven, projectDirectory);
		mavenPackage.init();
		VersionProvider versionProvider = new VersionProvider() {
			public String getVersion() {
				return version;
			}
		};
		deployWarFile = new DeployWarFileAction(projectDirectory, tomcat, versionProvider, getName());
		deployWarFile.init();
	}

	public void execute() throws Exception {
		log.info("Start upgrade of "+getName());
		
		subversionCheckout.execute();
		
		configureDatabase.execute();
		
		configureSmtpServer.execute();
		
		beforePackage();
		
		mavenPackage.execute();
		
		beforeDatabase();
		
		doDatabase();

		deployWarFile.execute();
		
		afterDeployment();		
		
		log.info("Upgrade completed!!");
	}
	
	public final void check() throws Exception {
		subversionCheckout.check();
		configureDatabase.check();
		configureSmtpServer.check();
		mavenPackage.check();
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
		deployWarFile.cleanup();
		
		// Clean up 
		log.info("Cleaning up the project directory: "+projectDirectory);
		FileUtils.deleteDirectory(projectDirectory);		
	}

	protected abstract String getName();
	
	protected abstract AbstractSubversionCheckoutAction getCheckoutAction();
	
	protected abstract void doDatabase() throws Exception;
	
	protected void beforeDatabase() throws Exception {
	}

	protected void beforePackage() throws Exception {
	}
	
	protected void afterDeployment() throws Exception {
	}

}
