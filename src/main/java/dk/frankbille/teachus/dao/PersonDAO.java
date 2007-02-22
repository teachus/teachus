package dk.frankbille.teachus.dao;

import java.io.Serializable;
import java.util.List;

import dk.frankbille.teachus.domain.Person;
import dk.frankbille.teachus.domain.Pupil;
import dk.frankbille.teachus.domain.Teacher;

public interface PersonDAO extends Serializable {

	void save(Person person);

	Person authenticatePerson(String username, String password);

	<P extends Person> List<P> getPersons(Class<P> personClass);

	List<Pupil> getPupils(Teacher teacher);
	
	Person getPerson(Long personId);
	
	void sendWelcomeMail(Long pupilId, String serverName);

	void setInactive(Long personId);

}
