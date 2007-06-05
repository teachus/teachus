package dk.teachus.ws.service.impl;

import dk.teachus.backend.dao.PersonDAO;
import dk.teachus.backend.domain.Person;

public abstract class AbstractService {

	protected PersonDAO personDAO;

	public AbstractService(PersonDAO personDAO) {
		this.personDAO = personDAO;
	}
	
	protected Person authenticate(String username, String password) {
		Person person = personDAO.authenticatePerson(username, password);
		
		if (person == null) {
			throw new IllegalArgumentException("Username/Password is incorrect");
		}
		
		return person;
	}
	
}
