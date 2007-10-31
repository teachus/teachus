package dk.teachus.tools.actions;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.Session;

import dk.teachus.tools.config.SshNode;

abstract class AbstractSftpAction extends AbstractSshAction {

	public AbstractSftpAction(SshNode host) {
		super(host);
	}

	@Override
	protected void doExecute(Session session) throws Exception {
		Channel channel = session.openChannel("sftp");
		channel.connect();
		ChannelSftp c = (ChannelSftp) channel;
		
		executeSftp(c);
		
		channel.disconnect();
	}
	
	protected abstract void executeSftp(ChannelSftp client) throws Exception;

}
