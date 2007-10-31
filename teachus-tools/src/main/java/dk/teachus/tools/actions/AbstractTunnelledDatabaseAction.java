package dk.teachus.tools.actions;

import dk.teachus.tools.config.DatabaseNode;
import dk.teachus.tools.config.SshNode;

abstract class AbstractTunnelledDatabaseAction extends AbstractDatabaseAction {

	private SshTunnelAction dbTunnel;
	private final SshNode host;

	public AbstractTunnelledDatabaseAction(SshNode host, DatabaseNode database) {
		super(database);
		this.host = host;
	}
	
	@Override
	public void init() throws Exception {
		super.init();
		
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
	protected String getHost() {
		return "localhost";
	}
	
	@Override
	protected int getPort() {
		return 13306;
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
