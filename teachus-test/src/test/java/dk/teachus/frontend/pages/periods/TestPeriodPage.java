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

import org.apache.wicket.Page;
import org.apache.wicket.util.tester.FormTester;
import org.apache.wicket.util.tester.ITestPageSource;
import org.apache.wicket.util.tester.WicketTester;

import dk.teachus.backend.domain.impl.PeriodImpl;
import dk.teachus.frontend.test.WicketSpringTestCase;

public class TestPeriodPage extends WicketSpringTestCase {
	private static final long serialVersionUID = 1L;

	public void testEndDateValidation_newPeriod() {
		WicketTester tester = createTester();
		
		tester.startPage(new ITestPageSource() {
			private static final long serialVersionUID = 1L;

			public Page getTestPage() {
				PeriodPage page = new PeriodPage(new PeriodImpl());
				return page;
			}
		});
		
		tester.assertRenderedPage(PeriodPage.class);
		
		// Enter an end date
		FormTester formTester = tester.newFormTester("form:form");
		formTester.setValue("elements:3:element:input:dateField", "2007-10-06");
		formTester.submit();
	}
	
}
