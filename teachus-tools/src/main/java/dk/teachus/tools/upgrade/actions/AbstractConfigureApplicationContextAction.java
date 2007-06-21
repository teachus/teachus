package dk.teachus.tools.upgrade.actions;

import java.io.File;

import org.apache.commons.io.FileUtils;

abstract class AbstractConfigureApplicationContextAction implements Action {
	private File projectDirectory;
	
	public AbstractConfigureApplicationContextAction(File projectDirectory) {
		this.projectDirectory = projectDirectory;
	}

	public void execute() throws Exception {
		String appContextFile = "teachus-backend/src/main/java/dk/teachus/backend/applicationContext.xml";
		File applicationContextFile = new File(projectDirectory, appContextFile);
		
		assert applicationContextFile.exists();
		
		String applicationContext = FileUtils.readFileToString(applicationContextFile, "UTF-8");
		
		applicationContext = doExecute(applicationContext);
		
		FileUtils.writeStringToFile(applicationContextFile, applicationContext, "UTF-8");
	}

	protected abstract String doExecute(String applicationContext);
	
}
