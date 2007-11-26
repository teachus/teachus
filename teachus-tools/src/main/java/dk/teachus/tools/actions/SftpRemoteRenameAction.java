package dk.teachus.tools.actions;

import com.jcraft.jsch.ChannelSftp;

import dk.teachus.tools.config.SshNode;

public class SftpRemoteRenameAction extends AbstractSftpAction {

	private final String oldPath;
	private final String newPath;

	public SftpRemoteRenameAction(SshNode host, String oldPath, String newPath) {
		super(host);
		this.oldPath = oldPath;
		this.newPath = newPath;
	}

	@Override
	protected void executeSftp(ChannelSftp client) throws Exception {
		client.rename(oldPath, newPath);
	}

}
