package dk.teachus.tools.upgrade.actions;

import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.Session;
import dk.teachus.tools.upgrade.config.SshNode;

public class SshRemoteCommandAction extends AbstractSshAction {

	private final String command;

	public SshRemoteCommandAction(SshNode host, String command) {
		super(host);
		this.command = command;
	}
	
	@Override
	protected void doExecute(Connection connection) throws Exception {
		Session session = connection.openSession();
		session.execCommand(command.toString());
		session.close();
	}

}
