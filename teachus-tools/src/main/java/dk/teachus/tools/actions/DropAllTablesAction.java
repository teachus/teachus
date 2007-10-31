package dk.teachus.tools.actions;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.Statement;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import dk.teachus.tools.config.DatabaseNode;
import dk.teachus.tools.config.SshNode;

public class DropAllTablesAction extends AbstractTunnelledDatabaseAction {
	private static final Log log = LogFactory.getLog(DropAllTablesAction.class);

	public DropAllTablesAction(SshNode host, DatabaseNode database) {
		super(host, database);
	}

	@Override
	protected void doExecute(Connection connection) throws Exception {
		log.info("Dropping all tables");
		
		DatabaseMetaData metaData = connection.getMetaData();
		ResultSet tables = metaData.getTables(null, database.getDatabase(), null, null);
		
		Statement statement = connection.createStatement();
		
		statement.addBatch("SET FOREIGN_KEY_CHECKS=0;");
		
		while(tables.next()) {
			StringBuilder b = new StringBuilder();
			
			String tableName = tables.getString("TABLE_NAME");
			b.append("DROP TABLE ").append(tableName);
			statement.addBatch(b.toString());
			
			log.debug("Dropping table: "+tableName);
		}
		
		statement.addBatch("SET FOREIGN_KEY_CHECKS=1;");
		
		statement.executeBatch();
		
		statement.close();
	}

}
