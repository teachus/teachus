package dk.teachus.tools.upgrade.actions;

import dk.teachus.tools.upgrade.config.DatabaseNode;
import dk.teachus.tools.upgrade.config.SshNode;

abstract class AbstractTunnelledDatabaseAction extends AbstractDatabaseAction {

	private SshTunnelAction dbTunnel;

	public AbstractTunnelledDatabaseAction(SshNode host, DatabaseNode database) {
		super(database);
		
		dbTunnel = new SshTunnelAction(host, 13306, database.getHost(), database.getPort());
	}

	@Override
	public void execute() throws Exception {
		// Start a tunnel first
		dbTunnel.execute();
		
		super.execute();
		
		// Close port again
		dbTunnel.cleanup();
	}
	
	@Override
	protected String getJdbcUrl() {
		DatabaseNode localTunnelDb = database.withHostPort("localhost", 13306);
		
		return localTunnelDb.getJdbcUrl();
	}
	
	@Override
	public void check() throws Exception {
		super.check();
		
		dbTunnel.check();
	}
	
	@Override
	public void cleanup() throws Exception {
		super.cleanup();
		
		dbTunnel.cleanup();
	}

}
