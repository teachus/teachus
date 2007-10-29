package dk.teachus.tools.actions;

import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.SFTPv3Client;
import dk.teachus.tools.config.SshNode;

abstract class AbstractSftpAction extends AbstractSshAction {

	public AbstractSftpAction(SshNode host) {
		super(host);
	}

	@Override
	protected void doExecute(Connection connection) throws Exception {
		SFTPv3Client client = new SFTPv3Client(connection);
		
		executeSftp(client);
		
		client.close();
	}
	
	protected abstract void executeSftp(SFTPv3Client client) throws Exception;

}
