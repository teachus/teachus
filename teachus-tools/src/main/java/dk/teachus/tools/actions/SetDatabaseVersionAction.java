package dk.teachus.tools.actions;

import java.sql.Connection;
import java.sql.Statement;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import dk.teachus.tools.config.DatabaseNode;
import dk.teachus.tools.config.SshNode;

public class SetDatabaseVersionAction extends AbstractTunnelledDatabaseAction {
	private static final Log log = LogFactory.getLog(SetDatabaseVersionAction.class);

	private final VersionProvider versionProvider;

	public SetDatabaseVersionAction(SshNode host, DatabaseNode database, VersionProvider versionProvider) {
		super(host, database);
		this.versionProvider = versionProvider;
	}

	@Override
	protected void doExecute(Connection connection) throws Exception {
		Statement statement = connection.createStatement();
		
		String version = versionProvider.getVersion();
		log.info("Setting database version to: "+version);
		statement.executeUpdate("REPLACE INTO application_configuration (name, version, value) VALUES ('VERSION', 0, '"+version+"')");
		
		statement.close();
	}

}
