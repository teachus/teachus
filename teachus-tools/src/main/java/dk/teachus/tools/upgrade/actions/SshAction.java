package dk.teachus.tools.upgrade.actions;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.Session;
import dk.teachus.tools.upgrade.config.SshNode;

public class SshAction implements Action {
	
	private SshNode host;
	private Connection connection;

	public SshAction(SshNode host) {
		this.host = host;
	}
	
	public Session openSession() throws IOException {
		if (connection == null) {
			throw new IllegalStateException("You have to execute this action before you can use it");
		}
		
		Session session = connection.openSession();
		
		handleOutput(session.getStdout());
		handleOutput(session.getStderr());
		
		return session;
	}

	public void execute() throws Exception {
		connection = new Connection(host.getHost());
		connection.connect();
		connection.authenticateWithPassword(host.getUsername(), host.getPassword());
	}
	
	private void handleOutput(final InputStream inputStream) {
		new Thread(new Runnable() {
			public void run() {
				try {
					BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
					
					String line = null;
					while((line = reader.readLine()) != null) {
						System.out.println(line);
					}
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			}
		});
	}

}
