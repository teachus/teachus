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
