package dk.teachus.tools.actions;

import java.io.File;

import dk.teachus.tools.config.DemoDeploymentNode;
import dk.teachus.tools.config.MavenNode;
import dk.teachus.tools.config.SshNode;
import dk.teachus.tools.config.SubversionReleaseNode;

public class DemoTeachUsInstance extends ReleaseBasedTeachUsInstance {

	private ConfigureMailBeanAction configureMailBean;
	private LoadTestDataAction loadTestData;

	public DemoTeachUsInstance(MavenNode maven, File workingDirectory,
			DemoDeploymentNode deployment, SshNode databaseHost,
			String version, SubversionReleaseNode subversion) throws Exception {
		super(maven, workingDirectory, deployment, databaseHost, version, subversion);
	}

	public String getInstanceName() {
		return "demo";
	}
	
	@Override
	public void init() throws Exception {
		super.init();

		configureMailBean = new ConfigureMailBeanAction(projectDirectory);
		configureMailBean.init();
		loadTestData = new LoadTestDataAction(databaseHost, projectDirectory, deployment.getDatabase(), maven);
		loadTestData.init();
	}
	
	@Override
	public void check() throws Exception {
		super.check();
		
		configureMailBean.check();
		loadTestData.check();
	}
	
	@Override
	public void cleanup() throws Exception {
		super.cleanup();
		
		configureMailBean.cleanup();
		loadTestData.cleanup();
	}
	
	@Override
	protected void beforePackage() throws Exception {
		configureMailBean.execute();
	}
	
	@Override
	public void onAfterUpgradeDatabase() throws Exception {
		loadTestData.execute();
	}

}
