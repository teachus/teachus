package dk.teachus.tools.actions;

import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Map;

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
	protected void addJdbcConnectionParamaters(Map<String, String> parameters) {
		parameters.put("allowMultiQueries", "true");
	}
	
	@Override
	protected void doExecute(Connection connection) throws Exception {
		// First check the version in the database
		String existingVersion = "";
		Statement statement = connection.createStatement();
		ResultSet resultSet = statement.executeQuery("SELECT value FROM application_configuration WHERE name='VERSION'");
		if (resultSet.next()) {
			existingVersion = resultSet.getString("value");
		}
		statement.close();
		
		// Only upgrade if the version is not the same
		if (version.equals(existingVersion) == false) {
			// Do the upgrade
			statement = connection.createStatement();
			statement.execute(getSqlScript());
			
			statement.close();
		} else {
			log.warn("Skipping upgrade of the database. It's already version: "+version);
		}
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
