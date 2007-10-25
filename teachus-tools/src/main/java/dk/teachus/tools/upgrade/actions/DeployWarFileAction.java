package dk.teachus.tools.upgrade.actions;

import java.io.File;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import dk.teachus.tools.upgrade.config.TomcatNode;

public class DeployWarFileAction implements Action {
	private static final Log log = LogFactory.getLog(DeployWarFileAction.class);

	private SftpDeleteFileAction deleteFile;

	private SftpDeleteDirectoryAction deleteDirectory;

	private ScpAction scpAction;

	public DeployWarFileAction(File projectDirectory, TomcatNode tomcat, String version, String warName) {
		deleteFile = new SftpDeleteFileAction(tomcat.getHost(), tomcat.getHome()+"/wars/"+warName+".war");
		deleteDirectory = new SftpDeleteDirectoryAction(tomcat.getHost(), tomcat.getHome()+"/webapps/"+warName+"/ROOT");
		
		File warFile = new File(projectDirectory, "teachus-frontend/target/teachus-frontend-"+version+".war");
		String destinationFile = warName+".war";
		String destinationDirectory = tomcat.getHome()+"/wars";
		scpAction = new ScpAction(tomcat.getHost(), warFile, destinationDirectory, destinationFile);
	}
	
	public void execute() throws Exception {
		deleteFile.execute();
		
		deleteDirectory.execute();
		
		// Deploy war
		log.info("Deploying war file");
		scpAction.execute();
	}

	public void check() throws Exception {
		deleteFile.check();
		deleteDirectory.check();
		scpAction.check();
	}
	
	public void cleanup() throws Exception {
		deleteFile.cleanup();
		deleteDirectory.cleanup();
		scpAction.cleanup();
	}
	
}
