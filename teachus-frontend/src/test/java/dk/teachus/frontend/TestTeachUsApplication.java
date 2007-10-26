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
