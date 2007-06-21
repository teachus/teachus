package dk.teachus.tools.upgrade.actions;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ch.ethz.ssh2.Connection;
import dk.teachus.tools.upgrade.config.SshNode;

abstract class AbstractSshAction implements Action {
	private static final Log log = LogFactory.getLog(AbstractSshAction.class);
	
	protected SshNode host;

	public AbstractSshAction(SshNode host) {
		this.host = host;
	}
	
	public void execute() throws Exception {
		log.info("Connecting to remote host: "+host.getHost());
		
		Connection connection = new Connection(host.getHost());
		connection.connect();
		connection.authenticateWithPassword(host.getUsername(), host.getPassword());
		
		doExecute(connection);
		
		connection.close();
	}
	
	protected abstract void doExecute(Connection connection) throws Exception;

}
