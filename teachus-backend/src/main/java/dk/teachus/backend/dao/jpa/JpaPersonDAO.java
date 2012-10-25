package dk.teachus.backend.dao.jpa;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.transaction.annotation.Transactional;

import dk.teachus.backend.dao.PersonDAO;
import dk.teachus.backend.domain.Admin;
import dk.teachus.backend.domain.Person;
import dk.teachus.backend.domain.Pupil;
import dk.teachus.backend.domain.Teacher;
import dk.teachus.backend.domain.TeacherAttribute;
import dk.teachus.backend.domain.impl.AdminImpl;
import dk.teachus.backend.domain.impl.PupilImpl;
import dk.teachus.backend.domain.impl.TeacherImpl;

public class JpaPersonDAO implements PersonDAO {
	private static final long serialVersionUID = 1L;
	
	private EntityManager entityManager;
	
	@Transactional
	@Override
	public void save(final Person person) {
		entityManager.persist(person);
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
	public Person getPerson(final Long personId) {
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
	
	@PersistenceContext
	public void setEntityManager(final EntityManager entityManager) {
		this.entityManager = entityManager;
	}
	
}
