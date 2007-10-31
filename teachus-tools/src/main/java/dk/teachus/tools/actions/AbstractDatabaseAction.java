package dk.teachus.tools.actions;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import dk.teachus.tools.config.DatabaseNode;

abstract class AbstractDatabaseAction implements Action {
	private static final Log log = LogFactory.getLog(AbstractDatabaseAction.class);

	protected DatabaseNode database;
	
	public AbstractDatabaseAction(DatabaseNode database) {
		this.database = database;
	}
	
	public void init() throws Exception {
	}

	public void execute() throws Exception {		
		String jdbcUrl = createJdbcUrl();
		log.info("Connection to database: "+jdbcUrl);
		
		Class.forName("com.mysql.jdbc.Driver");
		
		Connection connection = DriverManager.getConnection(jdbcUrl, database.getUsername(), database.getPassword());
		
		doExecute(connection);
		
		connection.close();
	}
	
	public void check() throws Exception {
	}
	
	public void cleanup() throws Exception {
	}
	
	protected void addJdbcConnectionParamaters(Map<String, String> parameters) {
	}
	
	protected abstract void doExecute(Connection connection) throws Exception;

	private String createJdbcUrl() {
		DatabaseNode db = new DatabaseNode(getHost(), getPort(), getDatabase(), null, null);
		
		Map<String, String> parameters = new HashMap<String, String>();
		addJdbcConnectionParamaters(parameters);
		
		return db.getJdbcUrl(parameters);
	}
	
	protected String getHost() {
		return database.getHost();
	}
	
	protected int getPort() {
		return database.getPort();
	}
	
	protected String getDatabase() {
		return database.getDatabase();
	}

}
