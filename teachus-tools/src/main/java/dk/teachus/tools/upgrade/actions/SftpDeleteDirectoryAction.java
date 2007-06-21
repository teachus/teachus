package dk.teachus.tools.upgrade.actions;

import java.util.Vector;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ch.ethz.ssh2.SFTPException;
import ch.ethz.ssh2.SFTPv3Client;
import ch.ethz.ssh2.SFTPv3DirectoryEntry;
import ch.ethz.ssh2.sftp.ErrorCodes;
import dk.teachus.tools.upgrade.config.SshNode;

public class SftpDeleteDirectoryAction extends AbstractSftpAction {
	private static final Log log = LogFactory.getLog(SftpDeleteDirectoryAction.class);

	private String remoteDirectory;

	public SftpDeleteDirectoryAction(SshNode host, String remoteDirectory) {
		super(host);
		this.remoteDirectory = remoteDirectory;
	}

	@Override
	protected void executeSftp(SFTPv3Client client) throws Exception {
		log.info("Deleting remote directory: "+host.getHost()+":"+remoteDirectory);
		
		// Check that the remote directory exists at all
		try {
			client.ls(remoteDirectory);
			
			// Traverse the directory to delete them all
			deleteDirectory(client, remoteDirectory);
		} catch (SFTPException e) {
			if (e.getServerErrorCode() != ErrorCodes.SSH_FX_NO_SUCH_FILE) {
				throw new RuntimeException(e);
			}
		}
	}
	
	private void deleteDirectory(SFTPv3Client client, String directory) throws Exception {
		Vector directoryContent = client.ls(directory);
		for (Object object : directoryContent) {
			SFTPv3DirectoryEntry entry = (SFTPv3DirectoryEntry) object;
			
			if (entry.filename.equals(".") == false && entry.filename.equals("..") == false) {
				String absolutePath = directory+"/"+entry.filename;
				
				if (entry.attributes.isDirectory()) {
					deleteDirectory(client, absolutePath);
				} else {
					client.rm(absolutePath);
				}
			}
		}
		
		client.rmdir(directory);
	}

}
