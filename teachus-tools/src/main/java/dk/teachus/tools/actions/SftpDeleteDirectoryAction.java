package dk.teachus.tools.actions;

import java.util.Vector;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.SftpException;
import com.jcraft.jsch.ChannelSftp.LsEntry;

import dk.teachus.tools.config.SshNode;

public class SftpDeleteDirectoryAction extends AbstractSftpAction {
	private static final Log log = LogFactory.getLog(SftpDeleteDirectoryAction.class);

	private String remoteDirectory;

	public SftpDeleteDirectoryAction(SshNode host, String remoteDirectory) {
		super(host);
		this.remoteDirectory = remoteDirectory;
	}

	@Override
	protected void executeSftp(ChannelSftp client) throws Exception {
		log.info("Deleting remote directory: "+host.getHost()+":"+remoteDirectory);
		
		// Check that the remote directory exists at all
		try {
			client.ls(remoteDirectory);
			
			// Traverse the directory to delete them all
			deleteDirectory(client, remoteDirectory);
		} catch (SftpException e) {
			e.printStackTrace();
			if (e.id != ChannelSftp.SSH_FX_NO_SUCH_FILE) {
				throw new RuntimeException(e);
			}
		}
	}
	
	private void deleteDirectory(ChannelSftp client, String directory) throws Exception {
		Vector<?> directoryContent = client.ls(directory);
		for (Object object : directoryContent) {
			LsEntry entry = (LsEntry) object;
			
			if (entry.getFilename().equals(".") == false && entry.getFilename().equals("..") == false) {
				String absolutePath = directory+"/"+entry.getFilename();
				
				if (entry.getAttrs().isDir()) {
					deleteDirectory(client, absolutePath);
				} else {
					client.rm(absolutePath);
				}
			}
		}
		
		client.rmdir(directory);
	}

}
