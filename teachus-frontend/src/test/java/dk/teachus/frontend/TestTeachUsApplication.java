package dk.teachus.frontend;

import junit.framework.TestCase;

/**
 * Smoke test to see that the web.xml is configured correctly.. that the application
 * comes up.
 */
public class TestTeachUsApplication extends TestCase {

	public void testConfiguration() throws Exception {
		// DO nothing for now. We need to figure out how we can test this
		// without having a mysql server available
		
//		Server server = new Server(18081);
//		WebAppContext webAppContext = new WebAppContext("src/main/webapp", "/");
//		server.addHandler(webAppContext);
//		server.start();
//		
//		GetMethod getWsdl = new GetMethod("http://localhost:18081/");
//		
//		int status = new HttpClient().executeMethod(getWsdl);
//		
//		assertEquals(getWsdl.getResponseBodyAsString(), 200, status);
//		
//		server.stop();
	}
	
}
