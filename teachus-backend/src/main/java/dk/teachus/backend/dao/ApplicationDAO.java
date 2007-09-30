package dk.teachus.backend.dao;

import dk.teachus.backend.domain.ApplicationConfiguration;

public interface ApplicationDAO {

	ApplicationConfiguration loadConfiguration();
	
	void saveConfiguration(ApplicationConfiguration configuration);
	
}
