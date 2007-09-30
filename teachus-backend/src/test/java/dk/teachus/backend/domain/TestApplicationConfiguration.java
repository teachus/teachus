package dk.teachus.backend.domain;

import junit.framework.TestCase;
import dk.teachus.backend.domain.impl.ApplicationConfigurationImpl;

public class TestApplicationConfiguration extends TestCase {

	public void testSetConfigurationInteger() {
		ApplicationConfigurationImpl conf = new ApplicationConfigurationImpl(null);

		assertEntryCount(conf, 0);

		conf.setConfigurationInteger("intKey", 100);

		assertEntryCount(conf, 1);

		assertEquals("100", conf.getEntries().get(0).getValue());
	}

	public void testGetConfigurationInteger() {
		ApplicationConfigurationImpl conf = new ApplicationConfigurationImpl(null);
		conf.setConfigurationInteger("intKey", 100);
		conf.setConfiguration("stringBasedIntKey", "200");
		conf.setConfiguration("noIntKey", "qwerty");

		assertEquals(100, conf.getConfigurationInteger("intKey"));
		assertEquals(200, conf.getConfigurationInteger("stringBasedIntKey"));
		try {
			conf.getConfigurationInteger("noIntKey");
			fail();
		} catch (NumberFormatException e) {
			// Expected
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}

	private void assertEntryCount(ApplicationConfigurationImpl conf, int expectedCount) {
		int actualCount = 0;

		if (conf.getEntries() != null) {
			actualCount = conf.getEntries().size();
		}

		assertEquals(expectedCount, actualCount);
	}

}
