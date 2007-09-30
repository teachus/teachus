package dk.teachus.frontend.models;

import org.apache.wicket.model.IModel;
import org.apache.wicket.util.string.Strings;

import dk.teachus.frontend.TeachUsApplication;

public class ApplicationConfigurationModel implements IModel {
	private static final long serialVersionUID = 1L;
	
	private String configurationKey;
	
	public ApplicationConfigurationModel(String configurationKey) {
		if (Strings.isEmpty(configurationKey)) {
			throw new IllegalArgumentException("Configuration key must not be null.");
		}
		
		this.configurationKey = configurationKey;
	}

	public Object getObject() {
		return TeachUsApplication.get().getConfiguration().getConfiguration(configurationKey);
	}

	public void setObject(Object object) {
		TeachUsApplication.get().getConfiguration().setConfiguration(configurationKey, String.valueOf(object));
	}

	public void detach() {
	}

}
