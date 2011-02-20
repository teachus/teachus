package dk.teachus.tools.actions;

import dk.teachus.tools.config.SshNode;

public class SshDeleteDirectoryAction implements Action {

	private final SshNode host;
	private final String remoteDirectory;
	private SshRemoteCommandAction deleteCommand;

	public SshDeleteDirectoryAction(SshNode host, String remoteDirectory) {
		this.host = host;
		this.remoteDirectory = remoteDirectory;
	}
	
	public void init() throws Exception {
		deleteCommand = new SshRemoteCommandAction(host, "rm -rf "+remoteDirectory);
		deleteCommand.init();
	}

	public void check() throws Exception {
		deleteCommand.check();
	}

	public void execute() throws Exception {
		deleteCommand.execute();
	}

	public void cleanup() throws Exception {
		deleteCommand.cleanup();
	}

}
