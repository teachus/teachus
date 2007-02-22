package dk.frankbille.teachus.dao.hibernate;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.DistinctRootEntityResultTransformer;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import dk.frankbille.teachus.bean.MailBean;
import dk.frankbille.teachus.dao.PersonDAO;
import dk.frankbille.teachus.domain.Admin;
import dk.frankbille.teachus.domain.Person;
import dk.frankbille.teachus.domain.Pupil;
import dk.frankbille.teachus.domain.Teacher;
import dk.frankbille.teachus.domain.impl.AdminImpl;
import dk.frankbille.teachus.domain.impl.PersonImpl;
import dk.frankbille.teachus.domain.impl.PupilImpl;
import dk.frankbille.teachus.domain.impl.TeacherImpl;

@Transactional(propagation=Propagation.REQUIRED)
public class PersonDAOHibernate extends HibernateDaoSupport implements PersonDAO {
	private static final long serialVersionUID = 1L;

	private MailBean mailBean;
	
	public PersonDAOHibernate(MailBean mailBean) {
		this.mailBean = mailBean;
	}

	public void save(Person person) {
		getHibernateTemplate().saveOrUpdate(person);
	}
	
	@SuppressWarnings("unchecked")
	@Transactional(readOnly=true)
	public Person authenticatePerson(String username, String password) {
		DetachedCriteria c = DetachedCriteria.forClass(PersonImpl.class);
		
		c.add(Restrictions.eq("username", username));
		c.add(Restrictions.eq("password", password));
		c.add(Restrictions.eq("active", true));
		c.setResultTransformer(new DistinctRootEntityResultTransformer());
		
		List<Person> persons = getHibernateTemplate().findByCriteria(c);
		
		Person person = null;
		if (persons.size() == 1) {
			person = persons.get(0);
		}
		
		return person;
	}

	@SuppressWarnings("unchecked")
	@Transactional(readOnly=true)
	public <P extends Person> List<P> getPersons(Class<P> personClass) {
		Class clazz = null;
		
		if (Admin.class.equals(personClass)) {
			clazz = AdminImpl.class;
		} else if (Teacher.class.equals(personClass)) {
			clazz = TeacherImpl.class;
		} else if (Pupil.class.equals(personClass)) {
			clazz = PupilImpl.class;
		}
		
		DetachedCriteria c = DetachedCriteria.forClass(clazz);
		
		c.setResultTransformer(new DistinctRootEntityResultTransformer());

		return getHibernateTemplate().findByCriteria(c);
	}

	@SuppressWarnings("unchecked")
	@Transactional(readOnly=true)
	public List<Pupil> getPupils(Teacher teacher) {
		DetachedCriteria c = DetachedCriteria.forClass(PupilImpl.class);
		
		c.add(Restrictions.eq("active", true));
		c.add(Restrictions.eq("teacher", teacher));
		c.setResultTransformer(new DistinctRootEntityResultTransformer());

		return getHibernateTemplate().findByCriteria(c);
	}

	@Transactional(readOnly=true)
	public Person getPerson(Long personId) {
		return (Person) getHibernateTemplate().load(PersonImpl.class, personId);
	}
	
	public void sendWelcomeMail(Long pupilId, String serverName) {
		Person person = getPerson(pupilId);
		
		if (person instanceof Pupil == false) {
			throw new IllegalArgumentException("pupilId doesn't resolve a pupil object");
		}
		
		Pupil pupil = (Pupil) person;
		
		mailBean.sendWelcomeMail(pupil, serverName);
	}
	
	public void setInactive(Long personId) {
		Person person = getPerson(personId);
		person.setActive(false);
		save(person);
	}
}
