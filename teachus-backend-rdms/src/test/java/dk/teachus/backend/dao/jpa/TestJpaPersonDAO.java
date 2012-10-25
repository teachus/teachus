package dk.teachus.backend.dao.jpa;

import static org.junit.Assert.assertNotNull;

import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import dk.teachus.backend.dao.PersonDAO;
import dk.teachus.backend.domain.Pupil;
import dk.teachus.backend.rdms.test.AbstractSpringTests;

public class TestJpaPersonDAO extends AbstractSpringTests {

	@Autowired
	private PersonDAO personDao;

	@Test
	public void testSavePupil() {
		Pupil pupil = personDao.createPupilObject();
		pupil.setName("The Name");
		
		personDao.save(pupil);
		assertNotNull(pupil.getId());
		
		Map<String, Object> personMap = jdbcTemplate.queryForMap("SELECT * FROM person WHERE id=?", pupil.getId());
		assertNotNull(personMap);
		Assert.assertEquals(pupil.getName(), personMap.get("name"));
	}
	
}
