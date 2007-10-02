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
package dk.teachus.frontend.pages;

import org.apache.wicket.Page;
import org.apache.wicket.RestartResponseAtInterceptPageException;

import dk.teachus.backend.domain.Admin;
import dk.teachus.backend.domain.Person;
import dk.teachus.backend.domain.Pupil;
import dk.teachus.backend.domain.Teacher;
import dk.teachus.frontend.TeachUsSession;
import dk.teachus.frontend.pages.calendar.PupilCalendarPage;
import dk.teachus.frontend.pages.persons.PupilsPage;
import dk.teachus.frontend.pages.persons.TeachersPage;

public class HomePage extends Page {
	private static final long serialVersionUID = 1L;

	public HomePage() {
		Person person = TeachUsSession.get().getPerson();
		
		Class<?> page = null;
		
		if (person != null) {
			if (person instanceof Admin) {
				page = TeachersPage.class;
			} else if (person instanceof Teacher) {
				page = PupilsPage.class;
			} else if (person instanceof Pupil) {
				page = PupilCalendarPage.class;
			}
		} else {
			page = InfoPage.class;
		}
		
		throw new RestartResponseAtInterceptPageException(page);
	}

}
