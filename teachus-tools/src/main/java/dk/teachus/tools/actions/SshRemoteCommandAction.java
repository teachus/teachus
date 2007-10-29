package dk.teachus.tools.actions;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.Session;
import dk.teachus.tools.config.SshNode;

public class SshRemoteCommandAction extends AbstractSshAction {
	private static final Log log = LogFactory.getLog(SshRemoteCommandAction.class);

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
	protected void doExecute(Connection connection) throws Exception {
		log.info("Executing command: "+command);
		
		final Session session = connection.openSession();

		new Thread(new Runnable() {
			public void run() {
				try {
					InputStream stdErr = session.getStderr();
					byte[] data = new byte[128];
					int read = 0;
					while((read = stdErr.read(data)) > 0) {
						System.err.write(data, 0, read);
					}
					
					InputStream stdOut = session.getStdout();
					read = 0;
					while((read = stdOut.read(data)) > 0) {
						System.out.write(data, 0, read);
					}
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			}
		}).start();
		
		session.execCommand(command);
		Thread.sleep(sleep);
		session.close();
	}

}
