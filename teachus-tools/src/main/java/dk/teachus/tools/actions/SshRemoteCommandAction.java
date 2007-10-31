package dk.teachus.tools.actions;

import java.io.InputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.Session;

import dk.teachus.tools.config.SshNode;

public class SshRemoteCommandAction extends AbstractSshAction {
	private static final Log log = LogFactory
			.getLog(SshRemoteCommandAction.class);

	private final String command;

	private final long sleep;

	public SshRemoteCommandAction(SshNode host, String command) {
		this(host, command, 0);
	}

	public SshRemoteCommandAction(SshNode host, String command, long sleep) {
		super(host);
		this.command = command;
		this.sleep = sleep;
	}

	@Override
	protected void doExecute(Session session) throws Exception {
		log.info("Executing command: " + command);

		Channel channel = session.openChannel("exec");
		ChannelExec channelExec = (ChannelExec) channel;
		channelExec.setCommand(command);

		channel.setInputStream(null);

		channelExec.setErrStream(System.err);

		InputStream in = channel.getInputStream();

		channel.connect();

		byte[] tmp = new byte[1024];
		while (true) {
			while (in.available() > 0) {
				int i = in.read(tmp, 0, 1024);
				if (i < 0) {
					break;
				}
				
				log.info(new String(tmp, 0, i));
			}
			if (channel.isClosed()) {
				if (channel.getExitStatus() != 0) {
					throw new RuntimeException("The remote command exited with a not-ok status: "+channel.getExitStatus());
				}
				break;
			}
			try {
				Thread.sleep(1000);
			} catch (Exception ee) {
			}
		}
		
		if (sleep > 0) {
			log.info("Sleeping for "+(sleep/1000.0)+" seconds.");
			Thread.sleep(sleep);
		}
		channel.disconnect();
	}

}
