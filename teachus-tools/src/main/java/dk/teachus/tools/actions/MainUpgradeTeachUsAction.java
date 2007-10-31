package dk.teachus.tools.actions;

import java.io.File;

import dk.teachus.tools.config.MainDeploymentNode;
import dk.teachus.tools.config.MavenNode;
import dk.teachus.tools.config.SubversionReleaseNode;
import dk.teachus.tools.config.TomcatNode;

public class MainUpgradeTeachUsAction extends ReleaseBasedUpgradeTeachUsAction {

	public MainUpgradeTeachUsAction(MavenNode maven, SubversionReleaseNode subversion, File workingDirectory, MainDeploymentNode deployment, TomcatNode tomcat, String version) throws Exception {
		super(maven, subversion, workingDirectory, deployment, tomcat, version);
	}
	
	@Override
	protected String getName() {
		return "main";
	}
	
	@Override
	protected void beforeDatabase() throws Exception {
		File backupFile = new File(workingDirectory, "backup.sql");
		
		BackupDatabaseAction backupDatabase = new BackupDatabaseAction(tomcat.getHost(), deployment.getDatabase(), backupFile);
		backupDatabase.execute();
	}

}
