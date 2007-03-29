package dk.teachus.dao;

import java.io.Serializable;
import java.util.List;

import dk.teachus.domain.Person;
import dk.teachus.domain.Pupil;
import dk.teachus.domain.Teacher;
import dk.teachus.domain.TeacherAttribute;

public interface PersonDAO extends Serializable {

	void save(Person person);

	Person authenticatePerson(String username, String password);

	<P extends Person> List<P> getPersons(Class<P> personClass);

	List<Pupil> getPupils(Teacher teacher);
	
	Person getPerson(Long personId);

	void setInactive(Long personId);
	
	void saveAttribute(TeacherAttribute attribute);
	
	List<TeacherAttribute> getAttributes(Teacher teacher);

	<A extends TeacherAttribute> A getAttribute(Class<A> attributeClass, Teacher teacher);

	Person usernameExists(String username);

}
