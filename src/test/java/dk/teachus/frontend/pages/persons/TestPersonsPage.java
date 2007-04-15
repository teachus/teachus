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
package dk.teachus.frontend.pages.persons;

import java.util.List;

import wicket.extensions.markup.html.repeater.data.grid.DataGridView;
import dk.teachus.domain.Admin;
import dk.teachus.domain.Pupil;
import dk.teachus.domain.Teacher;
import dk.teachus.frontend.TeachUsSession;
import dk.teachus.test.WicketSpringTestCase;

public class TestPersonsPage extends WicketSpringTestCase {
	private static final long serialVersionUID = 1L;

	public void testPupilPageRender() {
		tester.startPage(PupilsPage.class);
		
		tester.assertRenderedPage(PupilsPage.class);
		
		DataGridView dataGridView = (DataGridView) tester.getComponentFromLastRenderedPage("list:table:rows");
		
		Teacher teacher = (Teacher) TeachUsSession.get().getPerson();
		List<Pupil> pupils = tester.getTeachUsApplication().getPersonDAO().getPupils(teacher);
		
		assertEquals(pupils.size(), dataGridView.size());
	}
	
	public void testTeachersPageRender() {
		// Log in as admin
		TesterTeachUsSession.get().setPerson(1l);
		
		tester.startPage(TeachersPage.class);
		
		tester.assertRenderedPage(TeachersPage.class);
		
		DataGridView dataGridView = (DataGridView) tester.getComponentFromLastRenderedPage("list:table:rows");
		
		List<Teacher> teachers = tester.getTeachUsApplication().getPersonDAO().getPersons(Teacher.class);
		
		assertEquals(teachers.size(), dataGridView.size());
	}
	
	public void testAdminsPageRender() {
		// Log in as admin
		TesterTeachUsSession.get().setPerson(1l);
		
		tester.startPage(AdminsPage.class);
		
		tester.assertRenderedPage(AdminsPage.class);
		
		DataGridView dataGridView = (DataGridView) tester.getComponentFromLastRenderedPage("list:table:rows");
		
		List<Admin> admins = tester.getTeachUsApplication().getPersonDAO().getPersons(Admin.class);
		
		assertEquals(admins.size(), dataGridView.size());
	}
}
