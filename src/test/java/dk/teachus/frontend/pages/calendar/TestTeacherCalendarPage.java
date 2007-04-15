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
package dk.teachus.frontend.pages.calendar;

import wicket.Page;
import wicket.util.tester.ITestPageSource;
import dk.teachus.frontend.TeachUsSession;
import dk.teachus.frontend.components.calendar.CalendarPanel;
import dk.teachus.test.WicketSpringTestCase;

public class TestTeacherCalendarPage extends WicketSpringTestCase {
	private static final long serialVersionUID = 1L;

	public void testRender() {
		tester.startPage(new ITestPageSource() {
			private static final long serialVersionUID = 1L;

			public Page getTestPage() {
				return new TeacherCalendarPage();
			}			
		});
		
		tester.assertRenderedPage(TeacherCalendarPage.class);
		
		tester.assertComponent("calendar", CalendarPanel.class);
		
		tester.assertContains(TeachUsSession.get().getPerson().getName());
	}
	
	public void testBookTeacher() {
		tester.startPage(new ITestPageSource() {
			private static final long serialVersionUID = 1L;

			public Page getTestPage() {
				return new TeacherCalendarPage();
			}			
		});
		
		tester.assertRenderedPage(TeacherCalendarPage.class);
		
		String timePath = "calendar:calendar:weeks:1:days:2:periods:1:period:rows:3";
		
		assertTimeNotSelected(timePath);

		// Book time
		tester.clickLink(timePath+":contentContainer:content:link");
		
		// Show the page again to check that it is displaying
		tester.startPage(new ITestPageSource() {
			private static final long serialVersionUID = 1L;

			public Page getTestPage() {
				return new TeacherCalendarPage();
			}			
		});
		
		assertTimeSelected(timePath);		
	}
}
