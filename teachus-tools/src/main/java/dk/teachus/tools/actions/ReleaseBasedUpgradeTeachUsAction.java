package dk.teachus.tools.actions;

import java.io.File;

import dk.teachus.tools.config.AbstractDeploymentNode;
import dk.teachus.tools.config.MavenNode;
import dk.teachus.tools.config.SubversionReleaseNode;
import dk.teachus.tools.config.TomcatNode;

public abstract class ReleaseBasedUpgradeTeachUsAction extends
		UpgradeTeachUsAction {

	private final SubversionReleaseNode subversion;

	private UpgradeDatabaseAction upgradeDatabase;

	public ReleaseBasedUpgradeTeachUsAction(MavenNode maven,
			SubversionReleaseNode subversion, File workingDirectory, AbstractDeploymentNode deployment,
			TomcatNode tomcat, String version) throws Exception {
		super(maven, workingDirectory, deployment, tomcat, version);
		this.subversion = subversion;
	}
	
	@Override
	public void init() throws Exception {
		super.init();
		
		upgradeDatabase = new UpgradeDatabaseAction(tomcat.getHost(), deployment.getDatabase(), projectDirectory, version);
	}
	
	@Override
	protected AbstractSubversionCheckoutAction getCheckoutAction() {
		return new SubversionCheckoutReleaseAction(subversion, version, projectDirectory);
	}

	@Override
	protected void doDatabase() throws Exception {
		upgradeDatabase.execute();
	}
	
	@Override
	protected void doCheck() throws Exception {
		upgradeDatabase.check();
	}
	
	@Override
	public void cleanup() throws Exception {
		super.cleanup();
		
		upgradeDatabase.cleanup();
	}

}
