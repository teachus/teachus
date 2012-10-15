package dk.teachus.backend.service;

import dk.teachus.backend.domain.ApplicationConfiguration;

public interface ApplicationConfigurationService {
	
	ApplicationConfiguration loadConfiguration();
	
	void saveConfiguration(ApplicationConfiguration configuration);
	
}
