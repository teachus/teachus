package dk.teachus.tools.actions;

import java.io.File;
import java.sql.Connection;
import java.sql.Statement;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import dk.teachus.tools.config.DatabaseNode;
import dk.teachus.tools.config.SshNode;

public abstract class LoadSqlFileAction extends AbstractTunnelledDatabaseAction {
	private static final Log log = LogFactory.getLog(LoadSqlFileAction.class);

	public LoadSqlFileAction(SshNode host, DatabaseNode database) {
		super(host, database);
	}

	@Override
	protected void doExecute(Connection connection) throws Exception {
		Statement statement = connection.createStatement();
		
		File sqlFile = getSqlFile();
		log.info("Loading the following SQL file into the database: "+sqlFile);
		
		String sql = FileUtils.readFileToString(sqlFile, "UTF-8");
		statement.execute(sql);
				
		statement.close();
	}
	
	@Override
	protected void addJdbcConnectionParamaters(Map<String, String> parameters) {
		parameters.put("allowMultiQueries", "true");
	}

	protected abstract File getSqlFile();
	
}
