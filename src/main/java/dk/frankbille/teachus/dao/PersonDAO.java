package dk.frankbille.teachus.dao;

import java.io.Serializable;
import java.util.List;

import dk.frankbille.teachus.domain.Person;
import dk.frankbille.teachus.domain.Pupil;
import dk.frankbille.teachus.domain.Teacher;

public interface PersonDAO extends Serializable {

	void save(Person person);

	Person authenticatePerson(String username, String password);

	List<Person> getPersons(Class<? extends Person> personClass);

	List<Pupil> getPupils(Teacher teacher);
	
	Person getPerson(Long personId);

}
