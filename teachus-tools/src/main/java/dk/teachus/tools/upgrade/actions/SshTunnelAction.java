package dk.teachus.tools.upgrade.actions;

import java.net.ServerSocket;

import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.LocalPortForwarder;
import dk.teachus.tools.upgrade.config.SshNode;

public class SshTunnelAction extends AbstractSshAction {

	private final int localPort;
	private final String tunnelHost;
	private final int tunnelPort;
	private LocalPortForwarder localPortForwarder;

	public SshTunnelAction(SshNode host, int localPort, String tunnelHost, int tunnelPort) {
		super(host);
		this.localPort = localPort;
		this.tunnelHost = tunnelHost;
		this.tunnelPort = tunnelPort;
	}
	
	@Override
	protected void doExecute(Connection connection) throws Exception {
		localPortForwarder = connection.createLocalPortForwarder(localPort, tunnelHost, tunnelPort);
	}
	
	@Override
	protected void doCheck() throws Exception {
		// Check that the local port is available
		ServerSocket socket = new ServerSocket(localPort);
		socket.close();
	}
	
	@Override
	protected void doCleanup() throws Exception {
		if (localPortForwarder != null) {
			localPortForwarder.close();
		}
	}

}
