package dk.teachus.tools.actions;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import dk.teachus.tools.actions.UpgradeTeachUsInstancesAction.TeachUsInstance;
import dk.teachus.tools.config.AbstractDeploymentNode;
import dk.teachus.tools.config.MavenNode;
import dk.teachus.tools.config.SshNode;

public abstract class AbstractTeachUsInstance<D extends AbstractDeploymentNode> implements TeachUsInstance {
	private static final Log log = LogFactory.getLog(AbstractTeachUsInstance.class);

	private ScmCheckoutAction scmCheckout;

	protected File projectDirectory;

	private ConfigureTeachUsDatabaseAction configureDatabase;

	private ConfigureSmtpServerAction configureSmtpServer;

	private MavenPackageAction mavenPackage;

	protected final File workingDirectory;

	protected final D deployment;

	protected final MavenNode maven;

	protected String version;

	protected final SshNode databaseHost;
	
	public AbstractTeachUsInstance(MavenNode maven, File workingDirectory, D deployment, SshNode databaseHost, String version) throws Exception {
		this.maven = maven;
		this.workingDirectory = workingDirectory;
		this.deployment = deployment;
		this.databaseHost = databaseHost;
		this.version = version;
	}
	
	public void check() throws Exception {
		scmCheckout.check();
		configureDatabase.check();
		configureSmtpServer.check();
		mavenPackage.check();
	}
	
	public void cleanup() throws Exception {
		scmCheckout.cleanup();
		configureDatabase.cleanup();
		configureSmtpServer.cleanup();
		mavenPackage.cleanup();
		
		// Clean up
		if (log.isDebugEnabled()) {
			log.debug("Cleaning up the project directory: "+projectDirectory);
		}
		FileUtils.deleteDirectory(projectDirectory);		
	}

	public void init() throws Exception {
		projectDirectory = File.createTempFile("teachus", "", workingDirectory);
		projectDirectory.delete();
		projectDirectory.mkdir();
		
		scmCheckout = getCheckoutAction();
		scmCheckout.init();
		configureDatabase = new ConfigureTeachUsDatabaseAction(projectDirectory, deployment.getDatabase());
		configureDatabase.init();
		configureSmtpServer = new ConfigureSmtpServerAction(projectDirectory, deployment.getSmtpServer());
		configureSmtpServer.init();
		mavenPackage = new MavenPackageAction(maven, projectDirectory, false);
		mavenPackage.init();
	}

	public File prepareWarFile() throws Exception {
		scmCheckout.execute();
		
		configureDatabase.execute();
		
		configureSmtpServer.execute();
		
		beforePackage();
		
		mavenPackage.execute();
		
		return new File(projectDirectory, "teachus-frontend/target/teachus-frontend-"+version+".war");
	}
	
	protected abstract ScmCheckoutAction getCheckoutAction();
	
	protected void beforePackage() throws Exception {
	}

}
