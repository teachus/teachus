package dk.teachus.dao.hibernate;

import dk.teachus.domain.Person;
import dk.teachus.frontend.WicketSpringTestCase;

public class TestPersonDAO extends WicketSpringTestCase {
	private static final long serialVersionUID = 1L;

	public void testPasswordUserType() {
		Person person = getPersonDAO().authenticatePerson("admin", "admin");
		endTransaction();
		assertNotNull(person);
	}
	
	public void testUsernameExists() {
		Person usernameExists = getPersonDAO().usernameExists("admin");
		assertNotNull(usernameExists);
		assertEquals(new Long(1), usernameExists.getId());
		
		assertNull(getPersonDAO().usernameExists("admin2"));
		
		assertNull(getPersonDAO().usernameExists("admi"));
	}
	
}
