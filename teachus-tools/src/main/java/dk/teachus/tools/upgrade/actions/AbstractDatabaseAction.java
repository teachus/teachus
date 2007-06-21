package dk.teachus.tools.upgrade.actions;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import dk.teachus.tools.upgrade.config.DatabaseNode;

abstract class AbstractDatabaseAction implements Action {
	private static final Log log = LogFactory.getLog(AbstractDatabaseAction.class);

	private DatabaseNode database;
	
	public AbstractDatabaseAction(DatabaseNode database) {
		this.database = database;
	}

	public void execute() throws Exception {
		String sqlScript = getSqlScript();
		
		if (sqlScript != null && sqlScript.length() > 0) {			
			log.info("Connection to database: "+database.getJdbcUrl());
			
			Class.forName("com.mysql.jdbc.Driver");
			
			Connection connection = DriverManager.getConnection(database.getJdbcUrl(), database.getUsername(), database.getPassword());
			
			Statement statement = connection.createStatement();
			statement.addBatch(sqlScript);
			int[] batchResult = statement.executeBatch();
			
			for (int result : batchResult) {
				if (result == Statement.EXECUTE_FAILED) {
					throw new RuntimeException("Error when executing sql.");
				} else if (result != Statement.SUCCESS_NO_INFO) {
					System.out.println("Upgrade modifed: "+result+" rows.");
				}
			}
			
			statement.close();
			
			connection.close();
		}
	}
	
	protected abstract String getSqlScript() throws Exception;

}
