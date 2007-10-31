package dk.teachus.tools.actions;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.SftpException;

import dk.teachus.tools.config.SshNode;

public class SftpDeleteFileAction extends AbstractSftpAction {
	private static final Log log = LogFactory.getLog(SftpDeleteFileAction.class);

	private String remoteFile;

	public SftpDeleteFileAction(SshNode host, String remoteFile) {
		super(host);
		this.remoteFile = remoteFile;
	}

	@Override
	protected void executeSftp(ChannelSftp client) throws Exception {
		log.info("Deleting remote file: "+host.getHost()+":"+remoteFile);
		
		try {
			client.rm(remoteFile);
		} catch (SftpException e) {
			// Check if the error is an file doesn't exist error. Because then we can continue
			if (e.id != ChannelSftp.SSH_FX_NO_SUCH_FILE) {
				throw new RuntimeException(e);
			}
		}
	}

}
