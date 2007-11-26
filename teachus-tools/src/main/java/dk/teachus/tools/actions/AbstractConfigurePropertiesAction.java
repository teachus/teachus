package dk.teachus.tools.actions;

import java.io.File;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

abstract class AbstractConfigurePropertiesAction implements Action {
	private static final Log log = LogFactory.getLog(AbstractConfigurePropertiesAction.class);

	public final void execute() throws Exception {		
		File propertiesFile = getPropertiesFile();
		
		if (log.isDebugEnabled()) {
			log.debug("Modifying properties file: "+propertiesFile);
		}
		
		Properties properties = new Properties();
		configureProperties(properties);
		
		if (properties.size() > 0) {
			String content = FileUtils.readFileToString(propertiesFile, "UTF-8");
			
			for (Object keyObject : properties.keySet()) {
				String key = keyObject.toString();
				String value = properties.getProperty(key);
				
				String[] parts = content.split("\n|\r|\r\n");
				for (String part : parts) {
					if (part.matches("^"+key+"[\\s]*=.*$")) {
						String newPart = part.replaceAll("^"+key+"[\\s]*=.*$", key+"="+value);
						content = content.replace(part, newPart);
					}
				}
			}
			
			FileUtils.writeStringToFile(propertiesFile, content, "UTF-8");
		}
		
	}
	
	public void init() throws Exception {
	}
	
	public void check() throws Exception {
	}
	
	public void cleanup() throws Exception {
	}
	
	protected abstract File getPropertiesFile();
	
	protected abstract void configureProperties(Properties properties);

}
