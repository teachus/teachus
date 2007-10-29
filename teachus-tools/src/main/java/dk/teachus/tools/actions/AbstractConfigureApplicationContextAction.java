package dk.teachus.tools.actions;

import java.io.File;

import org.apache.commons.io.FileUtils;

abstract class AbstractConfigureApplicationContextAction implements Action {
	private static final String BACKEND_APPLICATION_CONTEXT = "teachus-backend/src/main/java/dk/teachus/backend/applicationContext.xml";
	private File projectDirectory;
	
	public AbstractConfigureApplicationContextAction(File projectDirectory) {
		this.projectDirectory = projectDirectory;
	}
	
	public void check() throws Exception {
	}
	
	public void cleanup() throws Exception {
	}

	public void execute() throws Exception {
		File applicationContextFile = createApplicationContextFile();
		
		assert applicationContextFile.exists();
		
		String applicationContext = FileUtils.readFileToString(applicationContextFile, "UTF-8");
		
		applicationContext = doExecute(applicationContext);
		
		FileUtils.writeStringToFile(applicationContextFile, applicationContext, "UTF-8");
	}

	private File createApplicationContextFile() {
		String appContextFile = BACKEND_APPLICATION_CONTEXT;
		File applicationContextFile = new File(projectDirectory, appContextFile);
		return applicationContextFile;
	}

	protected abstract String doExecute(String applicationContext);
	
}
