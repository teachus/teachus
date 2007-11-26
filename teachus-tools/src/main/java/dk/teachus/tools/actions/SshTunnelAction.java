package dk.teachus.tools.actions;

import java.net.ServerSocket;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.jcraft.jsch.Session;

import dk.teachus.tools.config.SshNode;

public class SshTunnelAction extends AbstractSshAction {
	private static final Log log = LogFactory.getLog(SshTunnelAction.class);

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
		if (log.isDebugEnabled()) {
			log.debug("Creating a tunnel from localhost:"+localPort+" to "+tunnelHost+":"+tunnelPort);
		}
		session.setPortForwardingL(localPort, tunnelHost, tunnelPort);
	}
	
	@Override
	protected void doCheck() throws Exception {
		// Check that the local port is available
		ServerSocket socket = new ServerSocket(localPort);
		socket.close();
	}

}
