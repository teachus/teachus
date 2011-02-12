package dk.teachus.tools.actions;

import java.io.File;

import dk.teachus.tools.config.AbstractDeploymentNode;
import dk.teachus.tools.config.MavenNode;
import dk.teachus.tools.config.SshNode;

public abstract class ReleaseBasedTeachUsInstance<D extends AbstractDeploymentNode> extends AbstractTeachUsInstance<D> {

	private final ScmClient scmClient;

	private UpgradeDatabaseAction upgradeDatabase;

	public ReleaseBasedTeachUsInstance(MavenNode maven, File workingDirectory,
			D deployment, SshNode databaseHost, String version, ScmClient scmClient) throws Exception {
		super(maven, workingDirectory, deployment, databaseHost, version);
		this.scmClient = scmClient;
	}
	
	@Override
	protected ScmCheckoutAction getCheckoutAction() {
		return new ScmCheckoutAction(scmClient, version, projectDirectory);
	}
	
	@Override
	public void init() throws Exception {
		super.init();
		
		upgradeDatabase = new UpgradeDatabaseAction(databaseHost, deployment.getDatabase(), projectDirectory, version);
		upgradeDatabase.init();
	}
	
	@Override
	public void check() throws Exception {
		super.check();
		
		upgradeDatabase.check();
	}
	
	@Override
	public void cleanup() throws Exception {
		super.cleanup();
		
		upgradeDatabase.cleanup();
	}

	public void onAfterUpgradeDatabase() throws Exception {
	}

	public void onBeforeUpgradeDatabase() throws Exception {
	}

	public void upgradeDatabase() throws Exception {
		upgradeDatabase.execute();
	}

}
