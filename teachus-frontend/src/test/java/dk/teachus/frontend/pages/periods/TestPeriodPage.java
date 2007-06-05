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
package dk.teachus.frontend.pages.periods;

import org.jmock.Expectations;

import wicket.Page;
import wicket.util.tester.ITestPageSource;
import dk.teachus.backend.dao.PersonDAO;
import dk.teachus.backend.domain.Period;
import dk.teachus.frontend.test.WicketTestCase;

public class TestPeriodPage extends WicketTestCase {
	private static final long serialVersionUID = 1L;

	public void testRender() {
		final TeachUsWicketTester tester = createTester();
		
		checking(new Expectations() {{
			PersonDAO personDAO = createPersonDAO();
			
			one(personDAO).getPerson(2L);
			will(returnValue(createTeacher(2L)));
			
			tester.setPersonDAO(personDAO);
		}});
			
		final Period period = createPeriod();
		
		tester.startPage(new ITestPageSource() {
			private static final long serialVersionUID = 1L;

			public Page getTestPage() {
				return new PeriodPage(period);
			}
		});
		
		tester.assertRenderedPage(PeriodPage.class);
		
		tester.assertContains(period.getName());
	}
	
}
