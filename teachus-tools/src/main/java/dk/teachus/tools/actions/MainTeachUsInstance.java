package dk.teachus.tools.actions;

import java.io.File;

import dk.teachus.tools.config.MainDeploymentNode;
import dk.teachus.tools.config.MavenNode;
import dk.teachus.tools.config.SshNode;

public class MainTeachUsInstance extends ReleaseBasedTeachUsInstance<MainDeploymentNode> {

	private BackupDatabaseAction backupDatabase;

	public MainTeachUsInstance(MavenNode maven, File workingDirectory,
			MainDeploymentNode deployment, SshNode databaseHost,
			String version, ScmClient scmClient) throws Exception {
		super(maven, workingDirectory, deployment, databaseHost, version, scmClient);
	}

	public String getInstanceName() {
		return "main";
	}	
	
	@Override
	public void init() throws Exception {
		super.init();

		File backupFile = new File(workingDirectory, "backup.sql");
		backupDatabase = new BackupDatabaseAction(databaseHost, deployment.getDatabase(), deployment.getBackupNode(), backupFile);
		backupDatabase.init();
	}
	
	@Override
	public void check() throws Exception {
		super.check();
		
		backupDatabase.check();
	}
	
	@Override
	public void onBeforeUpgradeDatabase() throws Exception {
		backupDatabase.execute();
	}
	
	@Override
	public void cleanup() throws Exception {
		super.cleanup();
		
		backupDatabase.cleanup();
	}
	
}