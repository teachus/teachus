package dk.teachus.tools.actions;

import java.io.File;
import java.sql.Connection;
import java.sql.Statement;

import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import dk.teachus.tools.config.DatabaseNode;
import dk.teachus.tools.config.SshNode;

public class UpgradeDatabaseAction extends AbstractTunnelledDatabaseAction {
	private static final Log log = LogFactory.getLog(UpgradeDatabaseAction.class);

	private File projectDirectory;
	private String version;

	public UpgradeDatabaseAction(SshNode tunnelHost, DatabaseNode database, File projectDirectory, String version) {
		super(tunnelHost, database);
		this.projectDirectory = projectDirectory;
		this.version = version;
	}
	
	@Override
	protected void doExecute(Connection connection) throws Exception {
		Statement statement = connection.createStatement();
		statement.addBatch(getSqlScript());
		int[] batchResult = statement.executeBatch();
		
		for (int result : batchResult) {
			if (result == Statement.EXECUTE_FAILED) {
				throw new RuntimeException("Error when executing sql.");
			} else if (result != Statement.SUCCESS_NO_INFO) {
				System.out.println("Upgrade modifed: "+result+" rows.");
			}
		}
		
		statement.close();
	}
	
	private String getSqlScript() throws Exception {		
		String sql = null;
		
		// Check if there is an upgrade file for this version
		File upgradeScript = new File(projectDirectory, "teachus-backend/src/main/database/upgrade/"+version+".sql");
		
		if (upgradeScript.exists()) {
			log.info("Upgrading database with script: "+upgradeScript);
			
			sql = FileUtils.readFileToString(upgradeScript, "UTF-8");
		}
		
		return sql;
	}

}
