package dk.teachus.backend.dao.jdo;

import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.jdo.Query;

import dk.teachus.backend.dao.ApplicationDAO;
import dk.teachus.backend.domain.ApplicationConfiguration;
import dk.teachus.backend.domain.impl.ApplicationConfigurationEntry;
import dk.teachus.backend.domain.impl.ApplicationConfigurationImpl;

public class JdoApplicationDAO implements ApplicationDAO {
	
	private PersistenceManagerFactory persistenceManagerFactory;
	
	@Override
	public ApplicationConfiguration loadConfiguration() {
		final PersistenceManager pm = persistenceManagerFactory.getPersistenceManager();
		final Query query = pm.newQuery(ApplicationConfigurationEntry.class);
		query.compile();
		@SuppressWarnings("unchecked")
		final List<ApplicationConfigurationEntry> entries = (List<ApplicationConfigurationEntry>) query.execute();
		return new ApplicationConfigurationImpl(entries);
	}
	
	@Override
	public void saveConfiguration(final ApplicationConfiguration configuration) {
		final PersistenceManager pm = persistenceManagerFactory.getPersistenceManager();
		if (configuration instanceof ApplicationConfigurationImpl == false) {
			throw new IllegalArgumentException("Unsupported " + ApplicationConfiguration.class.getName() + " implementation: " + configuration);
		}
		
		final ApplicationConfigurationImpl configurationImpl = (ApplicationConfigurationImpl) configuration;
		
		final List<ApplicationConfigurationEntry> entries = configurationImpl.getEntries();
		
		if (entries != null) {
			for (final ApplicationConfigurationEntry entry : entries) {
				pm.makePersistent(entry);
			}
		}
	}
	
	public void setPersistenceManagerFactory(final PersistenceManagerFactory persistenceManagerFactory) {
		this.persistenceManagerFactory = persistenceManagerFactory;
	}
	
}
