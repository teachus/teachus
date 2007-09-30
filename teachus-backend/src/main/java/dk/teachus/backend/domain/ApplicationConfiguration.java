package dk.teachus.backend.domain;

import java.beans.PropertyChangeListener;
import java.io.Serializable;

public interface ApplicationConfiguration extends Serializable {

	public static final String SERVER_URL = "SERVER_URL";
	public static final String VERSION = "VERSION";
	
	void setConfiguration(String configurationKey, String configurationValue);
	
	void setConfigurationInteger(String configurationKey, int configurationValue);
	
	String getConfiguration(String configurationKey);
	
	int getConfigurationInteger(String configurationKey);
	
	void addPropertyListener(PropertyChangeListener listener);
	
	void removePropertyListener(PropertyChangeListener listener);
	
}
