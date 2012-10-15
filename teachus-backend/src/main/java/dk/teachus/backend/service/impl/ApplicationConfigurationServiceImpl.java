package dk.teachus.backend.service.impl;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import dk.teachus.backend.dao.ApplicationDAO;
import dk.teachus.backend.domain.ApplicationConfiguration;
import dk.teachus.backend.service.ApplicationConfigurationService;

@Transactional(propagation = Propagation.REQUIRED)
public class ApplicationConfigurationServiceImpl implements ApplicationConfigurationService {
	
	private final ApplicationDAO applicationDAO;
	
	public ApplicationConfigurationServiceImpl(final ApplicationDAO applicationDAO) {
		this.applicationDAO = applicationDAO;
	}
	
	@Transactional(readOnly = true)
	@Override
	public ApplicationConfiguration loadConfiguration() {
		return applicationDAO.loadConfiguration();
	}
	
	@Override
	public void saveConfiguration(final ApplicationConfiguration configuration) {
		applicationDAO.saveConfiguration(configuration);
	}
	
}
