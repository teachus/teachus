package dk.teachus.tools.actions;

import java.net.ServerSocket;

import com.jcraft.jsch.Session;

import dk.teachus.tools.config.SshNode;

public class SshTunnelAction extends AbstractSshAction {

	private final int localPort;
	private final String tunnelHost;
	private final int tunnelPort;

	public SshTunnelAction(SshNode host, int localPort, String tunnelHost, int tunnelPort) {
		super(host);
		this.localPort = localPort;
		this.tunnelHost = tunnelHost;
		this.tunnelPort = tunnelPort;
	}
	
	@Override
	protected void doExecute(Session session) throws Exception {
		session.setPortForwardingL(localPort, tunnelHost, tunnelPort);
	}
	
	@Override
	protected void doCheck() throws Exception {
		// Check that the local port is available
		ServerSocket socket = new ServerSocket(localPort);
		socket.close();
	}

}
