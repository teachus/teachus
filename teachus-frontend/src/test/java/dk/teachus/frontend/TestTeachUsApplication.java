package dk.teachus.frontend;

import junit.framework.TestCase;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.webapp.WebAppContext;

/**
 * Smoke test to see that the web.xml is configured correctly.. that the application
 * comes up.
 */
public class TestTeachUsApplication extends TestCase {

	public void testConfiguration() throws Exception {
		Server server = new Server(18081);
		WebAppContext webAppContext = new WebAppContext("src/main/webapp", "/");
		server.addHandler(webAppContext);
		server.start();
		
		GetMethod getWsdl = new GetMethod("http://localhost:18081/");
		
		int status = new HttpClient().executeMethod(getWsdl);
		
		assertEquals(getWsdl.getResponseBodyAsString(), 200, status);
		
		server.stop();
	}
	
}
