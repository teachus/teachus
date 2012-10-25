package dk.teachus.backend.rdms.test;

import javax.sql.DataSource;

import org.hsqldb.jdbcDriver;
import org.junit.BeforeClass;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.mock.jndi.SimpleNamingContextBuilder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

@ContextConfiguration(locations = {
		"/dk/teachus/backend/applicationContext.xml",
		"/dk/teachus/backend/rdms/applicationContext-rdms.xml",
		"/dk/teachus/backend/rdms/test/applicationContext-rdms.xml" })
public class AbstractSpringTests extends AbstractTransactionalJUnit4SpringContextTests {

	protected JdbcTemplate jdbcTemplate;
	
	public void setDataSource(DataSource dataSource) {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	@BeforeClass
	public static void setupDataSource() throws Exception {
		final DataSource dataSource = new SimpleDriverDataSource(new jdbcDriver(), "jdbc:hsqldb:mem:teachus", "sa", "");

		final SimpleNamingContextBuilder contextBuilder = new SimpleNamingContextBuilder();
		contextBuilder.bind("java:comp/env/jdbc/teachus", dataSource);
		contextBuilder.activate();
	}

}
