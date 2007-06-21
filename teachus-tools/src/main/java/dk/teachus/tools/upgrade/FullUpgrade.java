package dk.teachus.tools.upgrade;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import dk.teachus.tools.upgrade.actions.DemoUpgradeTeachUsAction;
import dk.teachus.tools.upgrade.actions.MainUpgradeTeachUsAction;
import dk.teachus.tools.upgrade.config.Configuration;
import dk.teachus.tools.upgrade.config.DemoDeploymentNode;
import dk.teachus.tools.upgrade.config.MainDeploymentNode;
import dk.teachus.tools.upgrade.config.MavenNode;
import dk.teachus.tools.upgrade.config.SubversionNode;

public class FullUpgrade {
	
	public static void main(String[] args) throws Exception {
		String version = getInput("Version number: ");
		
		File workingDirectory = new File("/home/fb/sandbox/teachus-tools");
		
		File preferenceFile = new File(System.getProperty("user.home"), ".teachus/upgrade.xml");
		
		Configuration configuration = new Configuration();
		configuration.add(new MavenNode());
		configuration.add(new SubversionNode());
		configuration.add(new MainDeploymentNode());
		configuration.add(new DemoDeploymentNode());
		configuration.initialize(preferenceFile);
		
		MavenNode maven = configuration.getNode(MavenNode.class);
		SubversionNode subversion = configuration.getNode(SubversionNode.class);
		
		MainDeploymentNode mainDeployment = configuration.getNode(MainDeploymentNode.class);
		
		MainUpgradeTeachUsAction mainUpgrade = new MainUpgradeTeachUsAction(maven, subversion, workingDirectory, mainDeployment, version);
		mainUpgrade.execute();
		
		DemoDeploymentNode demoDeployment = configuration.getNode(DemoDeploymentNode.class);
		
		DemoUpgradeTeachUsAction demoUpgrade = new DemoUpgradeTeachUsAction(maven, subversion, workingDirectory, demoDeployment, version);
		demoUpgrade.execute();
	}
	
	private static String getInput(String label) {
		System.out.print(label);
		
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		
		String line = null;
		
		try {
			line = br.readLine();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		
		return line;
	}

}
