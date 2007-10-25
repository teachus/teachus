package dk.teachus.tools.upgrade.actions;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ch.ethz.ssh2.Connection;
import dk.teachus.tools.upgrade.config.SshNode;

abstract class AbstractSshAction implements Action {
	private static final Log log = LogFactory.getLog(AbstractSshAction.class);
	
	protected SshNode host;

	private Connection connection;

	public AbstractSshAction(SshNode host) {
		this.host = host;
	}
	
	public void execute() throws Exception {
		log.info("Connecting to remote host: "+host.getHost());
		
		connection = new Connection(host.getHost());
		connection.connect();
		connection.authenticateWithPassword(host.getUsername(), host.getPassword());
		
		doExecute(connection);
	}
	
	public final void check() throws Exception {
		// Check that the host is there and listens on the port,
		// and that the username/password is valid
		Connection connection = new Connection(host.getHost());
		connection.connect();
		connection.authenticateWithPassword(host.getUsername(), host.getPassword());
		connection.close();
		
		doCheck();
	}
	
	protected void doCheck() throws Exception {}
	
	public final void cleanup() throws Exception {
		doCleanup();
		
		if (connection != null) {
			connection.close();
		}
	}
	
	protected void doCleanup() throws Exception {}
	
	protected abstract void doExecute(Connection connection) throws Exception;

}
