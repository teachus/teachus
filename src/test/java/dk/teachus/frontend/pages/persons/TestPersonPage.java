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
