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
package dk.teachus.frontend.test;

import org.apache.wicket.Request;
import org.apache.wicket.Response;
import org.apache.wicket.Session;
import org.apache.wicket.util.tester.WicketTester;
import org.springframework.context.ApplicationContext;

import dk.teachus.backend.test.SpringTestCase;
import dk.teachus.frontend.TeachUsApplication;
import dk.teachus.frontend.TeachUsSession;

public abstract class WicketSpringTestCase extends SpringTestCase {
	private static final long serialVersionUID = 1L;

	private class TestTeachUsApplication extends TeachUsApplication {
		private Long personId;
		
		public TestTeachUsApplication(Long personId) {
			this.personId = personId;
		}

		@Override
		protected ApplicationContext getApplicationContext() {
			return applicationContext;
		}
		
		@Override
		public Session newSession(Request request, Response response) {
			return new TeachUsSession(request) {
				private static final long serialVersionUID = 1L;
				
				{
					if (personId != null) {
						person = getPersonDAO().getPerson(personId);
					}
				}
			};
		}
	}

	protected WicketTester createTester() {
		return createTester(2L);
	}
	
	protected WicketTester createTester(Long personId) {
		TestTeachUsApplication application = new TestTeachUsApplication(personId);
		return new WicketTester(application);
	}
	
}
