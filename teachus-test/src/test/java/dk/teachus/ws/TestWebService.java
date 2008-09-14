/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
		server = new Server(18180);
		server.addHandler(new WebAppContext("src/test/ws", "/"));
		server.start();
	}
	
	@Override
	protected void tearDown() throws Exception {
		server.stop();
		server = null;
	}
	
	public void testFinanceServiceComesUp() throws Exception {
		GetMethod getWsdl = new GetMethod("http://localhost:18180/services/finance?wsdl");
		
		int status = new HttpClient().executeMethod(getWsdl);
		
		assertEquals(200, status);
		
		Header contentType = getWsdl.getResponseHeader("Content-Type");
		assertEquals("text/xml", contentType.getValue());
		
		String response = getWsdl.getResponseBodyAsString();
		assertTrue(response.contains("wsdl"));
	}
	
}
