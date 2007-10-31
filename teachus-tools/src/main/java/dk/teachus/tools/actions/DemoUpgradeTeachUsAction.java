package dk.teachus.tools.actions;

import java.io.File;

import dk.teachus.tools.config.DemoDeploymentNode;
import dk.teachus.tools.config.MavenNode;
import dk.teachus.tools.config.SubversionReleaseNode;
import dk.teachus.tools.config.TomcatNode;

public class DemoUpgradeTeachUsAction extends ReleaseBasedUpgradeTeachUsAction {

	private ConfigureMailBeanAction configureMailBean;
	private LoadTestDataAction loadTestData;

	public DemoUpgradeTeachUsAction(MavenNode maven, SubversionReleaseNode subversion, File workingDirectory, DemoDeploymentNode deployment, TomcatNode tomcat, String version) throws Exception {
		super(maven, subversion, workingDirectory, deployment, tomcat, version);
	}
	
	@Override
	public void init() throws Exception {
		super.init();

		configureMailBean = new ConfigureMailBeanAction(projectDirectory);
		configureMailBean.init();
		loadTestData = new LoadTestDataAction(tomcat.getHost(), projectDirectory, deployment.getDatabase(), maven);
		loadTestData.init();
	}
	
	@Override
	protected void doCheck() throws Exception {
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
