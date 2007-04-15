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

import wicket.Page;
import wicket.util.tester.ITestPageSource;
import dk.teachus.domain.Admin;
import dk.teachus.domain.Pupil;
import dk.teachus.domain.Teacher;
import dk.teachus.frontend.models.AdminModel;
import dk.teachus.frontend.models.PupilModel;
import dk.teachus.frontend.models.TeacherModel;
import dk.teachus.test.WicketSpringTestCase;

public class TestPersonPage extends WicketSpringTestCase {
	private static final long serialVersionUID = 1L;

	public void testNewPupilPage() {		
		tester.startPage(new ITestPageSource() {
			private static final long serialVersionUID = 1L;

			public Page getTestPage() {
				return new PupilPage(new PupilModel(null));
			}
			
		});
		
		tester.assertRenderedPage(PupilPage.class);
	}
	
	public void testPupilPage() {
		final Pupil pupil = (Pupil) tester.getPerson(11l);
		
		tester.startPage(new ITestPageSource() {
			private static final long serialVersionUID = 1L;

			public Page getTestPage() {
				return new PupilPage(new PupilModel(11l));
			}
			
		});
		
		tester.assertRenderedPage(PupilPage.class);
		
		tester.assertContains(pupil.getName());
	}
	
	public void testNewTeacherPage() {
		// Log in as admin
		TesterTeachUsSession.get().setPerson(1l);
		
		tester.startPage(new ITestPageSource() {
			private static final long serialVersionUID = 1L;

			public Page getTestPage() {
				return new TeacherPage(new TeacherModel(null));
			}
			
		});
		
		tester.assertRenderedPage(TeacherPage.class);
	}
	
	public void testTeacherPage() {
		// Log in as admin
		TesterTeachUsSession.get().setPerson(1l);
		
		final Teacher teacher = (Teacher) tester.getPerson(2l);
		
		tester.startPage(new ITestPageSource() {
			private static final long serialVersionUID = 1L;

			public Page getTestPage() {
				return new TeacherPage(new TeacherModel(2l));
			}
			
		});
		
		tester.assertRenderedPage(TeacherPage.class);
		
		tester.assertContains(teacher.getName());
	}
	
	public void testNewAdminPage() {
		// Log in as admin
		TesterTeachUsSession.get().setPerson(1l);
		
		tester.startPage(new ITestPageSource() {
			private static final long serialVersionUID = 1L;

			public Page getTestPage() {
				return new AdminPage(new AdminModel(null));
			}
			
		});
		
		tester.assertRenderedPage(AdminPage.class);
	}
	
	public void testAdminPage() {
		// Log in as admin
		TesterTeachUsSession.get().setPerson(1l);
		
		final Admin admin = (Admin) tester.getPerson(1l);
		
		tester.startPage(new ITestPageSource() {
			private static final long serialVersionUID = 1L;

			public Page getTestPage() {
				return new AdminPage(new AdminModel(1l));
			}
			
		});
		
		tester.assertRenderedPage(AdminPage.class);
		
		tester.assertContains(admin.getName());
	}
}
