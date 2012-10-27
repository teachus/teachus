package dk.teachus.backend.dao.jdo;

import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.jdo.Query;

import dk.teachus.backend.dao.PersonDAO;
import dk.teachus.backend.domain.Admin;
import dk.teachus.backend.domain.Person;
import dk.teachus.backend.domain.Pupil;
import dk.teachus.backend.domain.Teacher;
import dk.teachus.backend.domain.TeacherAttribute;
import dk.teachus.backend.domain.impl.AdminImpl;
import dk.teachus.backend.domain.impl.PupilImpl;
import dk.teachus.backend.domain.impl.TeacherAttributeImpl;
import dk.teachus.backend.domain.impl.TeacherImpl;
import dk.teachus.utils.HashUtils;

public class JdoPersonDAO implements PersonDAO {
	private static final long serialVersionUID = 1L;
	
	private PersistenceManagerFactory persistenceManagerFactory;
	
	@Override
	public void save(final Person person) {
		final PersistenceManager pm = persistenceManagerFactory.getPersistenceManager();
		pm.makePersistent(person);
	}
	
	@Override
	public Person getPerson(final Long personId) {
		final PersistenceManager pm = persistenceManagerFactory.getPersistenceManager();
		Person foundPerson = pm.getObjectById(PupilImpl.class, personId);
		if (foundPerson == null) {
			foundPerson = pm.getObjectById(TeacherImpl.class, personId);
		}
		if (foundPerson == null) {
			foundPerson = pm.getObjectById(AdminImpl.class, personId);
		}
		return foundPerson;
	}
	
	@Override
	public Person authenticatePerson(final String username, final String password) {
		return authenticatePersonWithPrivateKey(username, HashUtils.hash(password));
	}
	
	@Override
	public Person authenticatePersonWithPrivateKey(final String username, final String privateKey) {
		Person foundPerson = findPersonByUsernameAndPrivateKey(PupilImpl.class, username, privateKey);
		if (foundPerson == null) {
			foundPerson = findPersonByUsernameAndPrivateKey(TeacherImpl.class, username, privateKey);
		}
		if (foundPerson == null) {
			foundPerson = findPersonByUsernameAndPrivateKey(AdminImpl.class, username, privateKey);
		}
		
		// Pupils, which are associated with a teacher, which is inactivated should not be allowed to log in
		if (foundPerson != null && foundPerson instanceof Pupil) {
			final Pupil pupil = (Pupil) foundPerson;
			
			if (pupil.getTeacher() != null) {
				if (pupil.getTeacher().isActive() == false) {
					foundPerson = null;
				}
			}
		}
		
		return foundPerson;
	}
	
	private <T extends Person> T findPersonByUsernameAndPrivateKey(final Class<T> personType, final String username, final String privateKey) {
		final PersistenceManager pm = persistenceManagerFactory.getPersistenceManager();
		final Query query = pm.newQuery(personType);
		query.setFilter("username == un && hashedPassword == hp && active");
		query.declareImports("import java.lang.String");
		query.declareParameters("String un, String hp");
		
		@SuppressWarnings("unchecked")
		final List<T> result = (List<T>) query.executeWithArray(username, privateKey);
		
		T person = null;
		
		if (result.size() == 1) {
			person = result.get(0);
		}
		
		return person;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public <P extends Person> List<P> getPersons(final Class<P> personClass) {
		Class<? extends Person> clazz = null;
		
		if (Admin.class.equals(personClass)) {
			clazz = AdminImpl.class;
		} else if (Teacher.class.equals(personClass)) {
			clazz = TeacherImpl.class;
		} else if (Pupil.class.equals(personClass)) {
			clazz = PupilImpl.class;
		} else {
			throw new IllegalArgumentException("Unsupported person implementation: " + personClass);
		}
		
		final PersistenceManager pm = persistenceManagerFactory.getPersistenceManager();
		final Query query = pm.newQuery(clazz);
		
		if (Pupil.class.equals(personClass)) {
			query.setFilter("active");
		}
		query.setOrdering("name");
		
		return (List<P>) query.execute();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Pupil> getPupils(final Teacher teacher) {
		final PersistenceManager pm = persistenceManagerFactory.getPersistenceManager();
		final Query query = pm.newQuery(PupilImpl.class);
		query.setFilter("teacher == tid && active");
		query.declareImports("import " + Teacher.class.getName());
		query.declareParameters(Teacher.class.getSimpleName() + " tid");
		query.setOrdering("name");
		
		return (List<Pupil>) query.execute(teacher);
	}
	
	@Override
	public void setInactive(final Long personId) {
		final Person person = getPerson(personId);
		person.setActive(false);
		save(person);
	}
	
	@Override
	public void saveAttribute(final TeacherAttribute attribute) {
		final PersistenceManager pm = persistenceManagerFactory.getPersistenceManager();
		pm.makePersistent(attribute);
	}
	
	@Override
	public TeacherAttribute getAttribute(final Teacher teacher) {
		final PersistenceManager pm = persistenceManagerFactory.getPersistenceManager();
		final Query query = pm.newQuery(TeacherAttributeImpl.class);
		query.setFilter("teacher == tid");
		query.declareImports("import " + Teacher.class.getName());
		query.declareParameters(Teacher.class.getSimpleName() + " tid");
		
		@SuppressWarnings("unchecked")
		final List<TeacherAttribute> result = (List<TeacherAttribute>) query.execute(teacher);
		
		TeacherAttribute teacherAttribute = null;
		if (result.size() == 1) {
			teacherAttribute = result.get(0);
		}
		
		return teacherAttribute;
	}
	
	@Override
	public Person usernameExists(final String username) {
		Person foundPerson = findPersonByUsername(PupilImpl.class, username);
		if (foundPerson == null) {
			foundPerson = findPersonByUsername(TeacherImpl.class, username);
		}
		if (foundPerson == null) {
			foundPerson = findPersonByUsername(AdminImpl.class, username);
		}
		return foundPerson;
	}
	
	private <T extends Person> T findPersonByUsername(final Class<T> personType, final String username) {
		final PersistenceManager pm = persistenceManagerFactory.getPersistenceManager();
		final Query query = pm.newQuery(personType);
		query.setFilter("username == un");
		query.declareImports("import java.lang.String");
		query.declareParameters("String un");
		
		@SuppressWarnings("unchecked")
		final List<T> result = (List<T>) query.executeWithArray(username);
		
		T person = null;
		
		if (result.size() == 1) {
			person = result.get(0);
		}
		
		return person;
	}
	
	@Override
	public void deleteTeacher(final Teacher teacher) {
		throw new UnsupportedOperationException("Not implemented yet");
	}
	
	@Override
	public void changeActiveState(final Long personId) {
		final Person person = getPerson(personId);
		person.setActive(person.isActive() == false);
		save(person);
	}
	
	@Override
	public Admin createAdminObject() {
		return new AdminImpl();
	}
	
	@Override
	public Teacher createTeacherObject() {
		return new TeacherImpl();
	}
	
	@Override
	public Pupil createPupilObject() {
		return new PupilImpl();
	}
	
	public void setPersistenceManagerFactory(final PersistenceManagerFactory persistenceManagerFactory) {
		this.persistenceManagerFactory = persistenceManagerFactory;
	}
	
}
