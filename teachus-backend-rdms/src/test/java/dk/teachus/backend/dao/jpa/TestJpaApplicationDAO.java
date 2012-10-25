package dk.teachus.backend.dao.jpa;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;

import javax.sql.DataSource;

import org.hsqldb.jdbcDriver;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.mock.jndi.SimpleNamingContextBuilder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

import dk.teachus.backend.dao.ApplicationDAO;
import dk.teachus.backend.domain.ApplicationConfiguration;
import dk.teachus.backend.domain.impl.ApplicationConfigurationEntry;
import dk.teachus.backend.domain.impl.ApplicationConfigurationImpl;

@ContextConfiguration(locations = {
		"/dk/teachus/backend/applicationContext.xml",
		"/dk/teachus/backend/rdms/applicationContext-rdms.xml",
		"/dk/teachus/backend/rdms/test/applicationContext-rdms.xml" })
public class TestJpaApplicationDAO extends AbstractTransactionalJUnit4SpringContextTests {

	@BeforeClass
	public static void setupDataSource() throws Exception {
		final DataSource dataSource = new SimpleDriverDataSource(new jdbcDriver(), "jdbc:hsqldb:mem:teachus", "sa", "");

		final SimpleNamingContextBuilder contextBuilder = new SimpleNamingContextBuilder();
		contextBuilder.bind("java:comp/env/jdbc/teachus", dataSource);
		contextBuilder.activate();
	}

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
