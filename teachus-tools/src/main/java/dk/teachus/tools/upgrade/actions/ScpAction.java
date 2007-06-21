package dk.teachus.tools.upgrade.actions;

import java.io.File;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.SCPClient;
import dk.teachus.tools.upgrade.config.SshNode;

public class ScpAction extends AbstractSshAction {
	private static final Log log = LogFactory.getLog(ScpAction.class);

	private File sourceFile;
	private String destinationDirectory;
	private String destinationFile;
	
	public ScpAction(SshNode host, File sourceFile, String destinationDirectory, String destinationFile) {
		super(host);
		this.sourceFile = sourceFile;
		this.destinationDirectory = destinationDirectory;
		this.destinationFile = destinationFile;
	}

	@Override
	protected void doExecute(Connection connection) throws Exception {
		log.info("Copying file "+sourceFile.getName()+" to "+host.getHost()+":"+destinationDirectory+"/"+destinationFile);
		
		SCPClient scpClient = connection.createSCPClient();
		
		scpClient.put(sourceFile.getAbsolutePath(), destinationFile, destinationDirectory, "0600");
	}
	
}
