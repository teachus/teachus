package dk.teachus.tools.upgrade.actions;

import java.io.File;

import dk.teachus.tools.upgrade.config.DemoDeploymentNode;
import dk.teachus.tools.upgrade.config.MavenNode;
import dk.teachus.tools.upgrade.config.SubversionNode;

public class DemoUpgradeTeachUsAction extends UpgradeTeachUsAction {

	public DemoUpgradeTeachUsAction(MavenNode maven, SubversionNode subversion, File workingDirectory, DemoDeploymentNode deployment, String version) {
		super(maven, subversion, workingDirectory, deployment, version);
	}
	
	@Override
	protected String getName() {
		return "demo";
	}
	
	@Override
	protected void beforePackage(File projectDirectory) throws Exception {
		ConfigureMailBeanAction configureMailBean = new ConfigureMailBeanAction(projectDirectory);
		configureMailBean.execute();
	}
	
	@Override
	protected void afterDeployment(File projectDirectory) throws Exception {
		LoadTestDataAction loadTestData = new LoadTestDataAction(projectDirectory, deployment.getDatabase(), maven);
		loadTestData.execute();
	}

}
