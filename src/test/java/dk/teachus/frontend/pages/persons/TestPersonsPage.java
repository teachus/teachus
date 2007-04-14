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
