package dk.teachus.tools.upgrade.actions;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ch.ethz.ssh2.SFTPException;
import ch.ethz.ssh2.SFTPv3Client;
import ch.ethz.ssh2.sftp.ErrorCodes;
import dk.teachus.tools.upgrade.config.SshNode;

public class SftpDeleteFileAction extends AbstractSftpAction {
	private static final Log log = LogFactory.getLog(SftpDeleteFileAction.class);

	private String remoteFile;

	public SftpDeleteFileAction(SshNode host, String remoteFile) {
		super(host);
		this.remoteFile = remoteFile;
	}

	@Override
	protected void executeSftp(SFTPv3Client client) throws Exception {
		log.info("Deleting remote file: "+host.getHost()+":"+remoteFile);
		
		try {
			client.rm(remoteFile);
		} catch (SFTPException e) {
			// Check if the error is an file doesn't exist error. Because then we can continue
			if (e.getServerErrorCode() != ErrorCodes.SSH_FX_NO_SUCH_FILE) {
				throw new RuntimeException(e);
			}
		}
	}

}
