package dk.teachus.tools;

import java.io.File;

import dk.teachus.tools.actions.ScmClient;
import dk.teachus.tools.actions.SubversionScmClient;
import dk.teachus.tools.actions.TestTeachUsInstance;
import dk.teachus.tools.actions.UpgradeTeachUsInstancesAction;
import dk.teachus.tools.config.Configuration;
import dk.teachus.tools.config.MavenNode;
import dk.teachus.tools.config.SubversionTrunkNode;
import dk.teachus.tools.config.TestDeploymentNode;
import dk.teachus.tools.config.TomcatNode;
import dk.teachus.tools.config.WorkingDirectoryNode;

public class NightlySnapshot {

	public static void main(String[] args) throws Exception {
		File preferenceFile = new File(System.getProperty("user.home"), ".teachus/nightly_snapshot.xml");
		
		Configuration configuration = new Configuration();
		configuration.add(new MavenNode());
		configuration.add(new WorkingDirectoryNode());
		configuration.add(new SubversionTrunkNode());
		configuration.add(new TomcatNode());
		configuration.add(new TestDeploymentNode());
		configuration.initialize(preferenceFile);
		
		MavenNode maven = configuration.getNode(MavenNode.class);
		SubversionTrunkNode subversion = configuration.getNode(SubversionTrunkNode.class);
		
		TomcatNode tomcat = configuration.getNode(TomcatNode.class);
		
		TestDeploymentNode testDeployment = configuration.getNode(TestDeploymentNode.class);
		
		WorkingDirectoryNode workingDirectoryNode = configuration.getNode(WorkingDirectoryNode.class);
		File workingDirectory = workingDirectoryNode.getWorkingDirectoryFile();
		
		ScmClient scmClient = new SubversionScmClient(subversion);
		
		/*
		 * Build up workflow
		 */
		Workflow workflow = new Workflow();

		UpgradeTeachUsInstancesAction upgradeTeachUsInstances = new UpgradeTeachUsInstancesAction(tomcat);
		upgradeTeachUsInstances.addTeachUsInstance(new TestTeachUsInstance(maven, workingDirectory, testDeployment, tomcat.getHost(), scmClient));
		workflow.addAction(upgradeTeachUsInstances);
		
		/*
		 * Start work flow
		 */
		workflow.start();
		
	}

}
