package dk.teachus.backend.dao.jpa;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import dk.teachus.backend.dao.PersonDAO;
import dk.teachus.backend.domain.Person;
import dk.teachus.backend.domain.Pupil;
import dk.teachus.backend.rdms.test.AbstractSpringTests;

public class TestJdoPersonDAO extends AbstractSpringTests {

	@Autowired
	private PersonDAO personDao;

	@Test
	public void testSavePupil() {
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
	
}
