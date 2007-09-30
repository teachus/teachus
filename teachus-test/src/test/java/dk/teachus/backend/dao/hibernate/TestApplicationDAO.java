package dk.teachus.backend.dao.hibernate;

import dk.teachus.backend.domain.ApplicationConfiguration;
import dk.teachus.backend.test.SpringTestCase;

public class TestApplicationDAO extends SpringTestCase {
	private static final long serialVersionUID = 1L;

	public void testLoadConfiguration() {
		ApplicationConfiguration configuration = getApplicationDAO().loadConfiguration();
		endTransaction();
		
		assertNotNull(configuration);
		
		assertNull(configuration.getConfiguration("not_existing_key"));
		assertNotNull(configuration.getConfiguration("SERVER_URL"));
		assertEquals("http://localhost:8080/", configuration.getConfiguration("SERVER_URL"));
	}
	
	public void testSaveConfiguration() {
		ApplicationConfiguration configuration = getApplicationDAO().loadConfiguration();
		endTransaction();
		
		assertNotNull(configuration);
		
		// Doesn't exist yet
		assertNull(configuration.getConfiguration("NEW_KEY"));
		
		configuration.setConfiguration("NEW_KEY", "keyValue");
		
		// Save
		getApplicationDAO().saveConfiguration(configuration);
		endTransaction();
		
		// Re-load
		configuration = getApplicationDAO().loadConfiguration();
		endTransaction();
		
		assertNotNull(configuration.getConfiguration("NEW_KEY"));
		assertEquals("keyValue", configuration.getConfiguration("NEW_KEY"));
	}
	
}
