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

import org.apache.wicket.Page;
import org.apache.wicket.util.tester.ITestPageSource;
import org.jmock.Expectations;

import dk.teachus.backend.dao.PersonDAO;
import dk.teachus.backend.domain.Admin;
import dk.teachus.backend.domain.Pupil;
import dk.teachus.backend.domain.Teacher;
import dk.teachus.backend.domain.impl.AdminImpl;
import dk.teachus.backend.domain.impl.PupilImpl;
import dk.teachus.backend.domain.impl.TeacherAttributeImpl;
import dk.teachus.backend.domain.impl.TeacherImpl;
import dk.teachus.frontend.models.AdminModel;
import dk.teachus.frontend.models.PupilModel;
import dk.teachus.frontend.models.TeacherModel;
import dk.teachus.frontend.test.WicketTestCase;

public class TestPersonPage extends WicketTestCase {
	private static final long serialVersionUID = 1L;
	
	public void testNewPupilPage() {
		final TeachUsWicketTester tester = createTester();
		
		checking(new Expectations() {
			{
				final PersonDAO personDAO = createPersonDAO();
				
				one(personDAO).getPerson(2L);
				will(Expectations.returnValue(createTeacher(2L)));
				
				one(personDAO).createPupilObject();
				will(Expectations.returnValue(new PupilImpl()));
				
				tester.setPersonDAO(personDAO);
			}
		});
		
		tester.startPage(new ITestPageSource() {
			private static final long serialVersionUID = 1L;
			
			@Override
			public Page getTestPage() {
				return new PupilPage(new PupilModel(null));
			}
			
		});
		
		tester.assertRenderedPage(PupilPage.class);
	}
	
	public void testPupilPage() {
		final TeachUsWicketTester tester = createTester();
		
		final Pupil pupil = createPupil(11L);
		
		checking(new Expectations() {
			{
				final PersonDAO personDAO = createPersonDAO();
				
				one(personDAO).getPerson(2L);
				will(Expectations.returnValue(createTeacher(2L)));
				
				one(personDAO).getPerson(pupil.getId());
				will(Expectations.returnValue(pupil));
				
				tester.setPersonDAO(personDAO);
			}
		});
		
		tester.startPage(new ITestPageSource() {
			private static final long serialVersionUID = 1L;
			
			@Override
			public Page getTestPage() {
				return new PupilPage(new PupilModel(11l));
			}
			
		});
		
		tester.assertRenderedPage(PupilPage.class);
		
		tester.assertContains(pupil.getName());
	}
	
	public void testNewTeacherPage() {
		// Log in as admin
		final TeachUsWicketTester tester = createTester(1);
		
		checking(new Expectations() {
			{
				final PersonDAO personDAO = createPersonDAO();
				
				one(personDAO).getPerson(1L);
				will(Expectations.returnValue(createAdmin()));
				
				one(personDAO).createTeacherObject();
				will(Expectations.returnValue(new TeacherImpl()));
				
				tester.setPersonDAO(personDAO);
			}
		});
		
		tester.startPage(new ITestPageSource() {
			private static final long serialVersionUID = 1L;
			
			@Override
			public Page getTestPage() {
				return new TeacherPage(new TeacherModel(null));
			}
			
		});
		
		tester.assertRenderedPage(TeacherPage.class);
	}
	
	public void testTeacherPage() {
		// Log in as admin
		final TeachUsWicketTester tester = createTester(1);
		
		final Teacher teacher = createTeacher();
		
		checking(new Expectations() {
			{
				final PersonDAO personDAO = createPersonDAO();
				
				one(personDAO).getPerson(1L);
				will(Expectations.returnValue(createAdmin()));
				
				one(personDAO).getPerson(teacher.getId());
				will(Expectations.returnValue(teacher));
				
				exactly(1).of(personDAO).getAttribute(teacher);
				will(Expectations.returnValue(new TeacherAttributeImpl()));
				
				tester.setPersonDAO(personDAO);
			}
		});
		
		tester.startPage(new ITestPageSource() {
			private static final long serialVersionUID = 1L;
			
			@Override
			public Page getTestPage() {
				return new TeacherPage(new TeacherModel(2l));
			}
			
		});
		
		tester.assertRenderedPage(TeacherPage.class);
		
		tester.assertContains(teacher.getName());
	}
	
	public void testNewAdminPage() {
		// Log in as admin
		final TeachUsWicketTester tester = createTester(1);
		
		checking(new Expectations() {
			{
				final PersonDAO personDAO = createPersonDAO();
				
				one(personDAO).getPerson(1L);
				will(Expectations.returnValue(createAdmin()));
				
				one(personDAO).createAdminObject();
				will(Expectations.returnValue(new AdminImpl()));
				
				tester.setPersonDAO(personDAO);
			}
		});
		
		tester.startPage(new ITestPageSource() {
			private static final long serialVersionUID = 1L;
			
			@Override
			public Page getTestPage() {
				return new AdminPage(new AdminModel(null));
			}
			
		});
		
		tester.assertRenderedPage(AdminPage.class);
	}
	
	public void testAdminPage() {
		// Log in as admin
		final TeachUsWicketTester tester = createTester(1);
		
		final Admin admin = createAdmin(10L);
		
		checking(new Expectations() {
			{
				final PersonDAO personDAO = createPersonDAO();
				
				one(personDAO).getPerson(1L);
				will(Expectations.returnValue(createAdmin()));
				
				one(personDAO).getPerson(admin.getId());
				will(Expectations.returnValue(admin));
				
				tester.setPersonDAO(personDAO);
			}
		});
		
		tester.startPage(new ITestPageSource() {
			private static final long serialVersionUID = 1L;
			
			@Override
			public Page getTestPage() {
				return new AdminPage(new AdminModel(admin.getId()));
			}
			
		});
		
		tester.assertRenderedPage(AdminPage.class);
		
		tester.assertContains(admin.getName());
	}
}
