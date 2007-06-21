package dk.teachus.tools.upgrade.actions;

import java.io.File;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import dk.teachus.tools.upgrade.config.TomcatNode;

public class DeployWarFileAction implements Action {
	private static final Log log = LogFactory.getLog(DeployWarFileAction.class);
	
	private File projectDirectory;
	private TomcatNode tomcat;
	private String version;

	public DeployWarFileAction(File projectDirectory, TomcatNode tomcat, String version) {
		this.projectDirectory = projectDirectory;
		this.tomcat = tomcat;
		this.version = version;
	}
	
	public void execute() throws Exception {
		// Remove old war and directory
		SftpDeleteFileAction deleteFile = new SftpDeleteFileAction(tomcat.getHost(), tomcat.getHome()+"/webapps/ROOT.war");
		deleteFile.execute();
		
		SftpDeleteDirectoryAction deleteDirectory = new SftpDeleteDirectoryAction(tomcat.getHost(), tomcat.getHome()+"/webapps/ROOT");
		deleteDirectory.execute();
		
		// Deploy war
		File warFile = new File(projectDirectory, "teachus-frontend/target/teachus-frontend-"+version+".war");
		String destinationFile = "ROOT.war";
		String destinationDirectory = tomcat.getHome()+"/webapps";
		
		log.info("Deploying war file");
		
		ScpAction scpAction = new ScpAction(tomcat.getHost(), warFile, destinationDirectory, destinationFile);
		scpAction.execute();
	}

}
