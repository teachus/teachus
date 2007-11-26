package dk.teachus.tools.actions;

import java.io.File;

import dk.teachus.tools.config.AbstractDeploymentNode;
import dk.teachus.tools.config.MavenNode;
import dk.teachus.tools.config.SshNode;
import dk.teachus.tools.config.SubversionReleaseNode;

public abstract class ReleaseBasedTeachUsInstance extends AbstractTeachUsInstance {

	private final SubversionReleaseNode subversion;

	private UpgradeDatabaseAction upgradeDatabase;

	public ReleaseBasedTeachUsInstance(MavenNode maven, File workingDirectory,
			AbstractDeploymentNode deployment, SshNode databaseHost, String version, SubversionReleaseNode subversion) throws Exception {
		super(maven, workingDirectory, deployment, databaseHost, version);
		this.subversion = subversion;
	}
	
	@Override
	protected AbstractSubversionCheckoutAction getCheckoutAction() {
		return new SubversionCheckoutReleaseAction(subversion, version, projectDirectory);
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
