package dk.teachus.tools.actions;

import java.io.File;

import dk.teachus.tools.config.DemoDeploymentNode;
import dk.teachus.tools.config.MavenNode;
import dk.teachus.tools.config.SubversionReleaseNode;
import dk.teachus.tools.config.TomcatNode;

public class DemoUpgradeTeachUsAction extends UpgradeTeachUsAction {

	private ConfigureMailBeanAction configureMailBean;
	private LoadTestDataAction loadTestData;

	public DemoUpgradeTeachUsAction(MavenNode maven, SubversionReleaseNode subversion, File workingDirectory, DemoDeploymentNode deployment, TomcatNode tomcat, String version) throws Exception {
		super(maven, subversion, workingDirectory, deployment, tomcat, version);

		configureMailBean = new ConfigureMailBeanAction(projectDirectory);
		loadTestData = new LoadTestDataAction(tomcat.getHost(), projectDirectory, deployment.getDatabase(), maven);
	}
	
	@Override
	protected void doCheck() throws Exception {
		configureMailBean.check();
		loadTestData.check();
	}
	
	@Override
	protected String getName() {
		return "demo";
	}
	
	@Override
	protected void beforePackage() throws Exception {
		configureMailBean.execute();
	}
	
	@Override
	protected void afterDeployment() throws Exception {
		loadTestData.execute();
	}

}
