package dk.teachus.tools.upgrade.actions;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

abstract class AbstractConfigurePropertiesAction implements Action {
	private static final Log log = LogFactory.getLog(AbstractConfigurePropertiesAction.class);

	public final void execute() throws Exception {		
		File propertiesFile = getPropertiesFile();
		
		Properties properties = new Properties();
		properties.load(new FileInputStream(propertiesFile));
		
		configureProperties(properties);
		
		properties.store(new FileOutputStream(propertiesFile), "Modified by the upgrade tool.");
		
		log.info("Modified properties file: "+propertiesFile);
	}
	
	protected abstract File getPropertiesFile();
	
	protected abstract void configureProperties(Properties properties);

}
