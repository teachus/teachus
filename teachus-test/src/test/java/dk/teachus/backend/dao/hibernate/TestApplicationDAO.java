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
