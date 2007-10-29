package dk.teachus.tools.actions;

import java.io.File;
import java.util.Properties;

abstract class AbstractConfigureTeachUsPropertiesAction extends AbstractConfigurePropertiesAction {

	private File projectDirectory;
	
	public AbstractConfigureTeachUsPropertiesAction(File projectDirectory) {
		this.projectDirectory = projectDirectory;
	}
	
	@Override
	protected File getPropertiesFile() {
		return new File(projectDirectory, "teachus-frontend/src/main/webapp/WEB-INF/teachus.properties");
	}
	
	protected abstract void configureProperties(Properties properties);

}
