package dk.teachus.tools.actions;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import dk.teachus.tools.actions.TomcatAction.ProcessAction;
import dk.teachus.tools.config.TomcatNode;

public class UpgradeTeachUsInstancesAction implements Action {
	private static final Log log = LogFactory.getLog(UpgradeTeachUsInstancesAction.class);

	public static interface TeachUsInstance {
		void init() throws Exception;
		
		void check() throws Exception;
		
		void cleanup() throws Exception;
		
		File prepareWarFile() throws Exception;
		
		String getInstanceName();
		
		void onBeforeUpgradeDatabase() throws Exception;
		
		void upgradeDatabase() throws Exception;
		
		void onAfterUpgradeDatabase() throws Exception;
	}
	
	private TomcatNode tomcat;
	private List<TeachUsInstance> teachUsInstances;
	private TomcatAction stopAction;
	private TomcatAction startAction;
	private boolean inited = false;
	
	public UpgradeTeachUsInstancesAction(TomcatNode tomcat) {
		this.tomcat = tomcat;
		teachUsInstances = new ArrayList<TeachUsInstance>();
	}
	
	public void addTeachUsInstance(TeachUsInstance teachUsInstance) {
		if (inited) {
			throw new IllegalStateException("Must add the instances before processing the workflow.");
		}
		
		teachUsInstances.add(teachUsInstance);
	}

	public void check() throws Exception {
		stopAction.check();
		startAction.check();
		
		for (TeachUsInstance teachUsInstance : teachUsInstances) {
			teachUsInstance.check();
		}
	}

	public void cleanup() throws Exception {
		stopAction.cleanup();
		startAction.cleanup();
		
		for (TeachUsInstance teachUsInstance : teachUsInstances) {
			teachUsInstance.cleanup();
		}
	}

	public void init() throws Exception {
		stopAction = new TomcatAction(tomcat, ProcessAction.STOP);
		stopAction.init();
		startAction = new TomcatAction(tomcat, ProcessAction.START);
		startAction.init();
		
		for (TeachUsInstance teachUsInstance : teachUsInstances) {
			teachUsInstance.init();
		}
		
		inited = true;
	}

	public void execute() throws Exception {
		log.info("Create war files");
		Map<String, File> warFiles = new LinkedHashMap<String, File>();
		for (TeachUsInstance teachUsInstance : teachUsInstances) {
			if (log.isDebugEnabled()) {
				log.debug("Creating war file for: "+teachUsInstance.getInstanceName());
			}
			warFiles.put(teachUsInstance.getInstanceName(), teachUsInstance.prepareWarFile());
		}
		
		log.info("Upload war files");
		for (String instanceName : warFiles.keySet()) {
			if (log.isDebugEnabled()) {
				log.debug("Uploading war file for: "+instanceName);
			}
			
			File warFile = warFiles.get(instanceName);
			UploadWarFileAction uploadWarFile = new UploadWarFileAction(tomcat, warFile, instanceName);
			uploadWarFile.init();
			uploadWarFile.check();
			uploadWarFile.execute();
			uploadWarFile.cleanup();
		}
		
		log.info("Stopping tomcat");
		stopAction.execute();
		
		log.info("Before upgrade of databases");
		for (TeachUsInstance teachUsInstance : teachUsInstances) {
			if (log.isDebugEnabled()) {
				log.debug("Before upgrade of database for: "+teachUsInstance.getInstanceName());
			}
			
			teachUsInstance.onBeforeUpgradeDatabase();
		}
		
		log.info("Upgrade databases");
		for (TeachUsInstance teachUsInstance : teachUsInstances) {
			if (log.isDebugEnabled()) {
				log.debug("Upgrade of database for: "+teachUsInstance.getInstanceName());
			}
			
			teachUsInstance.upgradeDatabase();
		}
		
		log.info("After upgrade of databases");
		for (TeachUsInstance teachUsInstance : teachUsInstances) {
			if (log.isDebugEnabled()) {
				log.debug("After upgrade of database for: "+teachUsInstance.getInstanceName());
			}
			
			teachUsInstance.onAfterUpgradeDatabase();
		}
		
		log.info("Remove old instances");
		for (String instanceName : warFiles.keySet()) {
			if (log.isDebugEnabled()) {
				log.debug("Remove old instance for: "+instanceName);
			}
			
			SftpDeleteFileAction deleteFile = new SftpDeleteFileAction(tomcat.getHost(), tomcat.getHome()+"/webapps/"+instanceName+".war");
			SshDeleteDirectoryAction deleteDirectory = new SshDeleteDirectoryAction(tomcat.getHost(), tomcat.getHome()+"/webapps/"+instanceName);
			
			deleteFile.init();
			deleteDirectory.init();
			deleteFile.check();
			deleteDirectory.check();
			deleteFile.execute();
			deleteDirectory.execute();
			deleteFile.cleanup();
			deleteDirectory.cleanup();
		}
		
		log.info("Remove tomcat work dir");
		SshDeleteDirectoryAction deleteTomcatWorkDir = new SshDeleteDirectoryAction(tomcat.getHost(), tomcat.getHome()+"/work/Catalina");
		deleteTomcatWorkDir.init();
		deleteTomcatWorkDir.check();
		deleteTomcatWorkDir.execute();
		deleteTomcatWorkDir.cleanup();
		
		log.info("Deploy war files");
		for (String instanceName : warFiles.keySet()) {
			if (log.isDebugEnabled()) {
				log.debug("Deploy war file for: "+instanceName);
			}
			
			String oldPath = tomcat.getHome()+"/wars/"+instanceName+".war";
			String newPath = tomcat.getHome()+"/webapps/"+instanceName+".war";
			
			SftpRemoteRenameAction rename = new SftpRemoteRenameAction(tomcat.getHost(), oldPath, newPath);
			rename.init();
			rename.check();
			rename.execute();
			rename.cleanup();
		}
		
		log.info("Starting tomcat");
		startAction.execute();
	}
	
}
