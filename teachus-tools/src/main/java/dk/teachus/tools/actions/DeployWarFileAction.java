package dk.teachus.tools.actions;

import java.io.File;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import dk.teachus.tools.actions.ScpAction.SourceProvider;
import dk.teachus.tools.config.TomcatNode;

public class DeployWarFileAction implements Action {
	private static final Log log = LogFactory.getLog(DeployWarFileAction.class);

	private SftpDeleteFileAction deleteFile;

	private SftpDeleteDirectoryAction deleteDirectory;

	private ScpAction scpAction;

	private final File projectDirectory;

	private final TomcatNode tomcat;

	private final VersionProvider versionProvider;

	private final String warName;

	public DeployWarFileAction(File projectDirectory, TomcatNode tomcat, VersionProvider versionProvider, String warName) {
		this.projectDirectory = projectDirectory;
		this.tomcat = tomcat;
		this.versionProvider = versionProvider;
		this.warName = warName;
	}
	
	public void init() throws Exception {
		deleteFile = new SftpDeleteFileAction(tomcat.getHost(), tomcat.getHome()+"/wars/"+warName+".war");
		deleteDirectory = new SftpDeleteDirectoryAction(tomcat.getHost(), tomcat.getHome()+"/webapps/"+warName+"/ROOT");
		
		SourceProvider sourceProvider = new SourceProvider() {
			public File getSourceFile() {
				return new File(projectDirectory, "teachus-frontend/target/teachus-frontend-"+versionProvider.getVersion()+".war");
			}
		};
		String destinationFile = warName+".war";
		String destinationDirectory = tomcat.getHome()+"/wars";
		scpAction = new ScpAction(tomcat.getHost(), sourceProvider, destinationDirectory, destinationFile);
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
