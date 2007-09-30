package dk.teachus.backend.dao.hibernate;

import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import dk.teachus.backend.dao.ApplicationDAO;
import dk.teachus.backend.domain.ApplicationConfiguration;
import dk.teachus.backend.domain.impl.ApplicationConfigurationEntry;
import dk.teachus.backend.domain.impl.ApplicationConfigurationImpl;

@Transactional(propagation=Propagation.REQUIRED)
public class HibernateApplicationDAO extends HibernateDaoSupport implements ApplicationDAO {

	@SuppressWarnings("unchecked")
	@Transactional(readOnly=true)
	public ApplicationConfiguration loadConfiguration() {
		List<ApplicationConfigurationEntry> list = getHibernateTemplate().loadAll(ApplicationConfigurationEntry.class);
				
		return new ApplicationConfigurationImpl(list);
	}

	@Transactional(rollbackFor=DataAccessException.class)
	public void saveConfiguration(ApplicationConfiguration configuration) {
		if (configuration instanceof ApplicationConfigurationImpl == false) {
			throw new IllegalArgumentException("Unsupported "+ApplicationConfiguration.class.getName()+" implementation: "+configuration);
		}
		
		ApplicationConfigurationImpl configurationImpl = (ApplicationConfigurationImpl) configuration;
		
		List<ApplicationConfigurationEntry> entries = configurationImpl.getEntries();
		
		if (entries != null) {
			for (ApplicationConfigurationEntry entry : entries) {
				getHibernateTemplate().saveOrUpdate(entry);
			}
		}
	}

}
