package dk.teachus.ws;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.webapp.WebAppContext;

import junit.framework.TestCase;

public class TestWebService extends TestCase {

	private Server server;
	
	@Override
	protected void setUp() throws Exception {
		server = new Server(18080);
		server.addHandler(new WebAppContext("src/test/ws", "/"));
		server.start();
	}
	
	@Override
	protected void tearDown() throws Exception {
		server.stop();
		server = null;
	}
	
	public void testFinanceServiceComesUp() throws Exception {
		GetMethod getWsdl = new GetMethod("http://localhost:18080/services/finance?wsdl");
		
		int status = new HttpClient().executeMethod(getWsdl);
		
		assertEquals(200, status);
		
		Header contentType = getWsdl.getResponseHeader("Content-Type");
		assertEquals("text/xml", contentType.getValue());
		
		String response = getWsdl.getResponseBodyAsString();
		assertTrue(response.contains("wsdl"));
	}
	
}
