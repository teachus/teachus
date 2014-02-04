package dk.teachus.backend.dao.jpa;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.TimeZone;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import dk.teachus.backend.dao.PersonDAO;
import dk.teachus.backend.domain.Admin;
import dk.teachus.backend.domain.Person;
import dk.teachus.backend.domain.Pupil;
import dk.teachus.backend.domain.Teacher;
import dk.teachus.backend.domain.TeacherAttribute;
import dk.teachus.backend.domain.impl.TeacherAttributeImpl;
import dk.teachus.backend.rdms.test.AbstractSpringTests;

public class TestJdoPersonDAO extends AbstractSpringTests {

	@Autowired
	private PersonDAO personDao;

	@Test
	public void savePupil() {
		Pupil pupil = personDao.createPupilObject();
		pupil.setName("The Name");
		pupil.setUsername("thename");
		
		personDao.save(pupil);
		assertNotNull(pupil.getId());

		Person persistedPerson = personDao.getPerson(pupil.getId());
		assertTrue(persistedPerson instanceof Pupil);
		assertEquals(pupil.getName(), persistedPerson.getName());
		assertEquals(pupil.getUsername(), persistedPerson.getUsername());
	}
	
	@Test
	public void authenticateWithUsernameAndPassword() {
		Pupil pupil = personDao.createPupilObject();
		pupil.setName("The Name");
		pupil.setUsername("username");
		pupil.setPassword("password");
		pupil.setActive(true);
		
		personDao.save(pupil);
		
		Person authenticatePerson = personDao.authenticatePerson(pupil.getUsername(), pupil.getPassword());
		
		assertNotNull(authenticatePerson);
		assertEquals(pupil.getId(), authenticatePerson.getId());
	}
	
	@Test
	public void authenticateWithUsernameAndPrivateKey() {
		Pupil pupil = personDao.createPupilObject();
		pupil.setName("The Name");
		pupil.setUsername("username");
		pupil.setPassword("password");
		pupil.setActive(true);
		
		personDao.save(pupil);
		
		Person authenticatePerson = personDao.authenticatePersonWithPrivateKey(pupil.getUsername(), pupil.getHashedPassword());
		
		assertNotNull(authenticatePerson);
		assertEquals(pupil.getId(), authenticatePerson.getId());
	}
	
	@Test
	public void getPersons() {
		// For now just check that it doesn't fail
		personDao.getPersons(Pupil.class);
		personDao.getPersons(Teacher.class);
		personDao.getPersons(Admin.class);
		
		// TODO implement better when we have proper test data
	}
	
	@Test
	public void getPupils() {
		Teacher teacher = personDao.createTeacherObject();
		teacher.setName("T1");
		teacher.setUsername("t1");
		teacher.setActive(true);
		personDao.save(teacher);
		
		Pupil p1 = personDao.createPupilObject();
		p1.setName("P1");
		p1.setUsername("p1");
		p1.setActive(true);
		p1.setTeacher(teacher);
		personDao.save(p1);
		
		Pupil p2 = personDao.createPupilObject();
		p2.setName("P2");
		p2.setUsername("p2");
		p2.setActive(true);
		p2.setTeacher(teacher);
		personDao.save(p2);
		
		Pupil p3 = personDao.createPupilObject();
		p3.setName("P3");
		p3.setUsername("p3");
		p3.setActive(true);
		p3.setTeacher(teacher);
		personDao.save(p3);
		
		List<Pupil> pupils = personDao.getPupils(teacher);
		assertEquals(3, pupils.size());
	}
	
	@Test
	public void setInactive() {
		Teacher teacher = personDao.createTeacherObject();
		teacher.setName("T1");
		teacher.setUsername("t1");
		teacher.setActive(true);
		personDao.save(teacher);
		
		personDao.setInactive(teacher.getId());
		
		Person persistedPerson = personDao.getPerson(teacher.getId());
		assertFalse(persistedPerson.isActive());
	}
	
	@Test
	public void saveAttribute() {
		Teacher teacher = personDao.createTeacherObject();
		teacher.setName("T1");
		teacher.setUsername("t1");
		teacher.setActive(true);
		personDao.save(teacher);
		
		TeacherAttribute ta = new TeacherAttributeImpl();
		ta.setTeacher(teacher);
		ta.setWelcomeIntroduction("Hello World");
		ta.setTimeZone(TimeZone.getDefault());
		ta.setCalendarNarrowTimes(true);
		
		personDao.saveAttribute(ta);
		
		TeacherAttribute persistedAttribute = personDao.getAttribute(teacher);
		assertNotNull(persistedAttribute);
		assertEquals(ta.getWelcomeIntroduction(), persistedAttribute.getWelcomeIntroduction());
		assertEquals(ta.getTimeZone(), persistedAttribute.getTimeZone());
		assertEquals(ta.getCalendarNarrowTimes(), persistedAttribute.getCalendarNarrowTimes());
	}
	
	@Test
	public void usernameExists() {
		Teacher teacher = personDao.createTeacherObject();
		teacher.setName("T1");
		teacher.setUsername("t1");
		teacher.setActive(true);
		personDao.save(teacher);
		
		assertEquals(teacher.getId(), personDao.usernameExists(teacher.getUsername()).getId());
		assertNull(personDao.usernameExists("adsfasdfasdf"));
	}
	
	@Test
	public void changeActiveState() {
		Teacher teacher = personDao.createTeacherObject();
		teacher.setName("T1");
		teacher.setUsername("t1");
		teacher.setActive(true);
		personDao.save(teacher);
		
		personDao.changeActiveState(teacher.getId());
		assertFalse(personDao.getPerson(teacher.getId()).isActive());
		
		personDao.changeActiveState(teacher.getId());
		assertTrue(personDao.getPerson(teacher.getId()).isActive());
		
		personDao.changeActiveState(teacher.getId());
		assertFalse(personDao.getPerson(teacher.getId()).isActive());
	}
	
}
