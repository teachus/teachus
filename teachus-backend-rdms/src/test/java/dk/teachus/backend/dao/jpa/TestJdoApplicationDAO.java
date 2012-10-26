package dk.teachus.backend.dao.jpa;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import dk.teachus.backend.dao.ApplicationDAO;
import dk.teachus.backend.domain.ApplicationConfiguration;
import dk.teachus.backend.domain.impl.ApplicationConfigurationEntry;
import dk.teachus.backend.domain.impl.ApplicationConfigurationImpl;
import dk.teachus.backend.rdms.test.AbstractSpringTests;

public class TestJdoApplicationDAO extends AbstractSpringTests {

	@Autowired
	private ApplicationDAO applicationDAO;

	@Test
	public void testLoadApplicationConfiguration() {
		final ApplicationConfiguration configuration = applicationDAO.loadConfiguration();
		assertNotNull(configuration);
	}

	@Test
	public void testSaveApplicationConfiguration() {
		ApplicationConfiguration configuration = new ApplicationConfigurationImpl(new ArrayList<ApplicationConfigurationEntry>());
		configuration.setConfiguration("something", "other");
		
		applicationDAO.saveConfiguration(configuration);
		
		final ApplicationConfiguration persistedConfiguration = applicationDAO.loadConfiguration();
		assertEquals("other", persistedConfiguration.getConfiguration("something"));
	}

}
