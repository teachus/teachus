package dk.teachus.utils;

import dk.teachus.backend.MailException;
import junit.framework.TestCase;

public class TestClassUtils extends TestCase {
	
	public void testGetAsResourcePath() {
		String path = ClassUtils.getAsResourcePath(MailException.class, "applicationContext.xml");
		assertNotNull(path);
		assertEquals("dk/teachus/backend/applicationContext.xml", path);
	}
	
	public void testGetAsResourceBundlePath() {
		String path = ClassUtils.getAsResourceBundlePath(TestClassUtils.class, "somebundle");
		assertNotNull(path);
		assertEquals("dk.teachus.utils.somebundle", path);
	}
	
}
