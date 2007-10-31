package dk.teachus.tools.actions;

import java.io.File;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.jcraft.jsch.ChannelSftp;

import dk.teachus.tools.config.SshNode;

public class ScpAction extends AbstractSftpAction {
	private static final Log log = LogFactory.getLog(ScpAction.class);

	public static interface SourceProvider {
		File getSourceFile();
	}
	
	private SourceProvider sourceProvider;
	private String destinationDirectory;
	private String destinationFile;
	
	public ScpAction(SshNode host, SourceProvider sourceProvider, String destinationDirectory, String destinationFile) {
		super(host);
		this.sourceProvider = sourceProvider;
		this.destinationDirectory = destinationDirectory;
		this.destinationFile = destinationFile;
	}
	
	@Override
	protected void doCheck() throws Exception {
	}

	@Override
	protected void executeSftp(ChannelSftp client) throws Exception {
		File sourceFile = sourceProvider.getSourceFile();
		log.info("Copying file "+sourceFile.getName()+" to "+host.getHost()+":"+destinationDirectory+"/"+destinationFile);
		
		String dst = destinationDirectory+"/"+destinationFile;
		
		client.put(sourceFile.getAbsolutePath(), dst, ChannelSftp.OVERWRITE);
	}
	
}
