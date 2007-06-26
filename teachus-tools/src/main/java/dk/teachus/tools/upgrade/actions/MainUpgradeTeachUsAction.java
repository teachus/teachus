package dk.teachus.tools.upgrade.actions;

import java.io.File;

import dk.teachus.tools.upgrade.config.MainDeploymentNode;
import dk.teachus.tools.upgrade.config.MavenNode;
import dk.teachus.tools.upgrade.config.SubversionReleaseNode;

public class MainUpgradeTeachUsAction extends UpgradeTeachUsAction {

	public MainUpgradeTeachUsAction(MavenNode maven, SubversionReleaseNode subversion, File workingDirectory, MainDeploymentNode deployment, String version) {
		super(maven, subversion, workingDirectory, deployment, version);
	}
	
	@Override
	protected String getName() {
		return "main";
	}
	
	@Override
	protected void beforeUpgradeDatabase(File projectDirectory) throws Exception {
		File backupFile = new File(workingDirectory, "backup.sql");
		
		BackupDatabaseAction backupDatabase = new BackupDatabaseAction(deployment.getDatabase(), backupFile);
		backupDatabase.execute();
	}
	
	@Override
	protected int getMaxHeap() {
		return 128;
	}
	
	@Override
	protected int getMaxPerm() {
		return 128;
	}

}
