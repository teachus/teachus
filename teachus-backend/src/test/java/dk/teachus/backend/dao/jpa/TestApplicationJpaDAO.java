package dk.teachus.backend.dao.jpa;

import javax.sql.DataSource;

import junit.framework.Assert;

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

@ContextConfiguration(locations = { "/dk/teachus/backend/applicationContext.xml" })
public class TestApplicationJpaDAO extends AbstractTransactionalJUnit4SpringContextTests {
	
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
	public void testGetApplicationConfiguration() {
		final ApplicationConfiguration configuration = applicationDAO.loadConfiguration();
		Assert.assertNotNull(configuration);
	}
	
}
