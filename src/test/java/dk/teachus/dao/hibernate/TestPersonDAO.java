package dk.teachus.dao.hibernate;

import dk.teachus.domain.Person;
import dk.teachus.domain.Pupil;
import dk.teachus.domain.Teacher;
import dk.teachus.domain.impl.PupilImpl;
import dk.teachus.domain.impl.TeacherImpl;
import dk.teachus.frontend.WicketSpringTestCase;

public class TestPersonDAO extends WicketSpringTestCase {
	private static final long serialVersionUID = 1L;

	public void testPasswordUserType() {
		Person person = getPersonDAO().authenticatePerson("admin", "admin");
		endTransaction();
		assertNotNull(person);
	}
	
	public void testSave() {
		Teacher teacher = (Teacher) getPersonDAO().getPerson(2L);
		endTransaction();
		
		Pupil pupil = new PupilImpl();
		pupil.setName("Test");
		pupil.setUsername("test");
		pupil.setActive(true);
		pupil.setEmail("test@teachus.dk");
		pupil.setTeacher(teacher);
		
		getPersonDAO().save(pupil);
		endTransaction();
	}
	
	public void testSaveTeacher() {
		Teacher teacher = new TeacherImpl();
		teacher.setName("Test");
		teacher.setUsername("test");
		teacher.setActive(true);
		teacher.setEmail("test@teachus.dk");
		teacher.setCurrency("kr");
		
		getPersonDAO().save(teacher);
	}
	
	public void testUsernameExists() {
		Person usernameExists = getPersonDAO().usernameExists("admin");
		assertNotNull(usernameExists);
		assertEquals(new Long(1), usernameExists.getId());
		
		assertNull(getPersonDAO().usernameExists("admin2"));
		
		assertNull(getPersonDAO().usernameExists("admi"));
	}
	
}
