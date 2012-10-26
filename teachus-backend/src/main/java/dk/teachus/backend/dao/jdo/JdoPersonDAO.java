package dk.teachus.backend.dao.jdo;

import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;

import dk.teachus.backend.dao.PersonDAO;
import dk.teachus.backend.domain.Admin;
import dk.teachus.backend.domain.Person;
import dk.teachus.backend.domain.Pupil;
import dk.teachus.backend.domain.Teacher;
import dk.teachus.backend.domain.TeacherAttribute;
import dk.teachus.backend.domain.impl.AdminImpl;
import dk.teachus.backend.domain.impl.PersonImpl;
import dk.teachus.backend.domain.impl.PupilImpl;
import dk.teachus.backend.domain.impl.TeacherImpl;

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
		return pm.getObjectById(PersonImpl.class, personId);
	}
	
	@Override
	public Person authenticatePerson(final String username, final String password) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public Person authenticatePersonWithPrivateKey(final String username, final String privateKey) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public <P extends Person> List<P> getPersons(final Class<P> personClass) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public List<Pupil> getPupils(final Teacher teacher) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void setInactive(final Long personId) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void saveAttribute(final TeacherAttribute attribute) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public List<TeacherAttribute> getAttributes(final Teacher teacher) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public Person usernameExists(final String username) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void deleteTeacher(final Teacher teacher) {
		// TODO Auto-generated method stub
		
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
	
	@Override
	public void changeActiveState(final Long personId) {
		// TODO Auto-generated method stub
		
	}
	
	public void setPersistenceManagerFactory(final PersistenceManagerFactory persistenceManagerFactory) {
		this.persistenceManagerFactory = persistenceManagerFactory;
	}
	
}
