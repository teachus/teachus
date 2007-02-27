package dk.teachus.utils;

import java.io.IOException;
import java.util.Properties;


public abstract class ApplicationUtils {
	private static final String version;

	static {
		try {
			Properties properties = new Properties();
			properties.load(ApplicationUtils.class.getResourceAsStream("version.properties"));
			version = properties.getProperty("version");
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	public static String getVersion() {
		return version;
	}
	
}
