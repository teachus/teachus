package dk.teachus.frontend.pages.persons;

import wicket.Page;
import wicket.util.tester.ITestPageSource;
import dk.teachus.domain.Admin;
import dk.teachus.domain.Pupil;
import dk.teachus.domain.Teacher;
import dk.teachus.frontend.WicketSpringTestCase;
import dk.teachus.frontend.pages.persons.AdminPage;
import dk.teachus.frontend.pages.persons.PupilPage;
import dk.teachus.frontend.pages.persons.TeacherPage;

public class TestPersonPage extends WicketSpringTestCase {
	private static final long serialVersionUID = 1L;

	public void testPupilPage() {
		final Pupil pupil = (Pupil) tester.getPerson(11l);
		
		tester.startPage(new ITestPageSource() {
			private static final long serialVersionUID = 1L;

			public Page getTestPage() {
				return new PupilPage(pupil);
			}
			
		});
		
		tester.assertRenderedPage(PupilPage.class);
		
		tester.assertContains(pupil.getName());
	}
	
	public void testTeacherPage() {
		// Log in as admin
		TesterTeachUsSession.get().setPerson(1l);
		
		final Teacher teacher = (Teacher) tester.getPerson(2l);
		
		tester.startPage(new ITestPageSource() {
			private static final long serialVersionUID = 1L;

			public Page getTestPage() {
				return new TeacherPage(teacher);
			}
			
		});
		
		tester.assertRenderedPage(TeacherPage.class);
		
		tester.assertContains(teacher.getName());
	}
	
	public void testAdminPage() {
		// Log in as admin
		TesterTeachUsSession.get().setPerson(1l);
		
		final Admin admin = (Admin) tester.getPerson(1l);
		
		tester.startPage(new ITestPageSource() {
			private static final long serialVersionUID = 1L;

			public Page getTestPage() {
				return new AdminPage(admin);
			}
			
		});
		
		tester.assertRenderedPage(AdminPage.class);
		
		tester.assertContains(admin.getName());
	}
}
