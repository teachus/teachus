package dk.teachus.tools.actions;

import java.io.File;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import dk.teachus.tools.actions.ScpAction.SimpleSourceProvider;
import dk.teachus.tools.config.TomcatNode;

public class UploadWarFileAction implements Action {
	private static final Log log = LogFactory.getLog(UploadWarFileAction.class);

	private SftpDeleteFileAction deleteFile;

	private ScpAction scpAction;

	private final TomcatNode tomcat;

	private final String instanceName;

	private final File warFile;

	public UploadWarFileAction(TomcatNode tomcat, File warFile, String instanceName) {
		this.tomcat = tomcat;
		this.warFile = warFile;
		this.instanceName = instanceName;
	}
	
	public void init() throws Exception {
		deleteFile = new SftpDeleteFileAction(tomcat.getHost(), tomcat.getHome()+"/wars/"+instanceName+".war");

		String destinationFile = instanceName+".war";
		String destinationDirectory = tomcat.getHome()+"/wars";
		scpAction = new ScpAction(tomcat.getHost(), new SimpleSourceProvider(warFile), destinationDirectory, destinationFile);
	}
	
	public void execute() throws Exception {
		deleteFile.execute();
		
		// Upload war
		if (log.isDebugEnabled()) {
			log.debug("UPloading war file");
		}
		scpAction.execute();
	}

	public void check() throws Exception {
		deleteFile.check();
		scpAction.check();
	}
	
	public void cleanup() throws Exception {
		deleteFile.cleanup();
		scpAction.cleanup();
	}
	
}
