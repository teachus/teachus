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

import java.util.ArrayList;
import java.util.List;

import org.jmock.Expectations;

import wicket.extensions.markup.html.repeater.data.grid.DataGridView;
import dk.teachus.backend.dao.PersonDAO;
import dk.teachus.backend.domain.Admin;
import dk.teachus.backend.domain.Pupil;
import dk.teachus.backend.domain.Teacher;
import dk.teachus.frontend.test.WicketTestCase;

public class TestPersonsPage extends WicketTestCase {
	private static final long serialVersionUID = 1L;

	public void testPupilPageRender() {
		final TeachUsWicketTester tester = createTester();
		
		final Teacher teacher = createTeacher(2L);
		final List<Pupil> pupils = new ArrayList<Pupil>();
		pupils.add(createPupil(3L, teacher));
		pupils.add(createPupil(4L, teacher));
		pupils.add(createPupil(5L, teacher));
		pupils.add(createPupil(6L, teacher));
		pupils.add(createPupil(7L, teacher));
		pupils.add(createPupil(8L, teacher));
		pupils.add(createPupil(9L, teacher));
		
		checking(new Expectations() {{
			PersonDAO personDAO = createPersonDAO();
			
			one(personDAO).getPerson(2L);
			will(returnValue(teacher));
			
			one(personDAO).getPupils(teacher);
			will(returnValue(pupils));
			
			tester.setPersonDAO(personDAO);
		}});
			
		tester.startPage(PupilsPage.class);
		
		tester.assertRenderedPage(PupilsPage.class);
		
		DataGridView dataGridView = (DataGridView) tester.getComponentFromLastRenderedPage("list:table:rows");
				
		assertEquals(pupils.size(), dataGridView.size());
	}
	
	public void testTeachersPageRender() {
		final TeachUsWicketTester tester = createTester();
		
		final List<Teacher> teachers = new ArrayList<Teacher>();
		teachers.add(createTeacher(2L));
		teachers.add(createTeacher(3L));
		teachers.add(createTeacher(4L));
		teachers.add(createTeacher(5L));
		teachers.add(createTeacher(6L));
		
		checking(new Expectations() {{
			PersonDAO personDAO = createPersonDAO();
			
			one(personDAO).getPerson(1L);
			will(returnValue(createAdmin()));
			
			one(personDAO).getPersons(Teacher.class);
			will(returnValue(teachers));
			
			tester.setPersonDAO(personDAO);
		}});
			
		// Log in as admin
		TesterTeachUsSession.get().setPerson(1l);
		
		tester.startPage(TeachersPage.class);
		
		tester.assertRenderedPage(TeachersPage.class);
		
		DataGridView dataGridView = (DataGridView) tester.getComponentFromLastRenderedPage("list:table:rows");
				
		assertEquals(teachers.size(), dataGridView.size());
	}
	
	public void testAdminsPageRender() {
		final TeachUsWicketTester tester = createTester();
		
		final List<Admin> admins = new ArrayList<Admin>();
		admins.add(createAdmin(1L));
		admins.add(createAdmin(2L));
		admins.add(createAdmin(3L));
		
		checking(new Expectations() {{
			PersonDAO personDAO = createPersonDAO();
			
			one(personDAO).getPerson(1L);
			will(returnValue(createAdmin()));
			
			one(personDAO).getPersons(Admin.class);
			will(returnValue(admins));
			
			tester.setPersonDAO(personDAO);
		}});
			
		// Log in as admin
		TesterTeachUsSession.get().setPerson(1l);
		
		tester.startPage(AdminsPage.class);
		
		tester.assertRenderedPage(AdminsPage.class);
		
		DataGridView dataGridView = (DataGridView) tester.getComponentFromLastRenderedPage("list:table:rows");
		
		assertEquals(admins.size(), dataGridView.size());
	}
}
