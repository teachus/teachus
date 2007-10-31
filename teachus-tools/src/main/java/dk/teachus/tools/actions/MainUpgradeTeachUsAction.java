package dk.teachus.tools.actions;

import java.io.File;

import dk.teachus.tools.config.MainDeploymentNode;
import dk.teachus.tools.config.MavenNode;
import dk.teachus.tools.config.SubversionReleaseNode;
import dk.teachus.tools.config.TomcatNode;

public class MainUpgradeTeachUsAction extends ReleaseBasedUpgradeTeachUsAction {

	private BackupDatabaseAction backupDatabase;

	public MainUpgradeTeachUsAction(MavenNode maven, SubversionReleaseNode subversion, File workingDirectory, MainDeploymentNode deployment, TomcatNode tomcat, String version) throws Exception {
		super(maven, subversion, workingDirectory, deployment, tomcat, version);
	}
	
	@Override
	protected String getName() {
		return "main";
	}
	
	@Override
	public void init() throws Exception {
		super.init();

		File backupFile = new File(workingDirectory, "backup.sql");
		backupDatabase = new BackupDatabaseAction(tomcat.getHost(), deployment.getDatabase(), backupFile);
		backupDatabase.init();
	}
	
	@Override
	protected void doCheck() throws Exception {
		super.doCheck();
		
		backupDatabase.check();
	}
	
	@Override
	protected void beforeDatabase() throws Exception {		
		backupDatabase.execute();
	}
	
	@Override
	public void cleanup() throws Exception {
		super.cleanup();
		
		backupDatabase.cleanup();
	}

}
