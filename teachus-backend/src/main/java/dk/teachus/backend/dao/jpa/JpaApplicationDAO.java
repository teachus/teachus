package dk.teachus.backend.dao.jpa;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;

import org.springframework.transaction.annotation.Transactional;

import dk.teachus.backend.dao.ApplicationDAO;
import dk.teachus.backend.domain.ApplicationConfiguration;
import dk.teachus.backend.domain.impl.ApplicationConfigurationEntry;
import dk.teachus.backend.domain.impl.ApplicationConfigurationImpl;

public class JpaApplicationDAO implements ApplicationDAO {
	
	private EntityManager entityManager;
	
	@Transactional
	@Override
	public ApplicationConfiguration loadConfiguration() {
		final CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		final CriteriaQuery<ApplicationConfigurationEntry> query = criteriaBuilder.createQuery(ApplicationConfigurationEntry.class);
		query.select(query.from(ApplicationConfigurationEntry.class));
		final TypedQuery<ApplicationConfigurationEntry> result = entityManager.createQuery(query);
		return new ApplicationConfigurationImpl(result.getResultList());
	}
	
	@Transactional
	@Override
	public void saveConfiguration(final ApplicationConfiguration configuration) {
		if (configuration instanceof ApplicationConfigurationImpl == false) {
			throw new IllegalArgumentException("Unsupported " + ApplicationConfiguration.class.getName() + " implementation: " + configuration);
		}
		
		final ApplicationConfigurationImpl applicationConfiguration = (ApplicationConfigurationImpl) configuration;
		final List<ApplicationConfigurationEntry> entries = applicationConfiguration.getEntries();
		if (entries != null) {
			for (final ApplicationConfigurationEntry entry : entries) {
				entityManager.persist(entry);
			}
		}
	}
	
	@PersistenceContext
	public void setEntityManager(final EntityManager entityManager) {
		this.entityManager = entityManager;
	}
	
}
