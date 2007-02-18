package dk.frankbille.teachus.dao.hibernate;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.DistinctRootEntityResultTransformer;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.HtmlUtils;

import dk.frankbille.teachus.bean.VelocityBean;
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
	
	private JavaMailSender mailSender;
	private VelocityBean velocityBean;

	public PersonDAOHibernate(JavaMailSender mailSender, VelocityBean velocityBean) {
		this.mailSender = mailSender;
		this.velocityBean = velocityBean;
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
	public List<Person> getPersons(Class<? extends Person> personClass) {
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
		
		c.add(Restrictions.eq("teacher", teacher));
		c.setResultTransformer(new DistinctRootEntityResultTransformer());

		return getHibernateTemplate().findByCriteria(c);
	}

	@Transactional(readOnly=true)
	public Person getPerson(Long personId) {
		return (Person) getHibernateTemplate().load(PersonImpl.class, personId);
	}
	
	public void sendWelcomeMail(Long personId, final Locale locale) {
		final Person person = getPerson(personId);
		
		mailSender.send(new MimeMessagePreparator() {
			public void prepare(MimeMessage mimeMessage) throws Exception {
				MimeMessageHelper message = new MimeMessageHelper(mimeMessage, false, "UTF-8");

				// Sender and recipient
				String email = null;
				String fromName = null;
				if (person instanceof Pupil) {
					Pupil pupil = (Pupil) person;
					fromName = pupil.getTeacher().getName();
					email = pupil.getTeacher().getEmail();
				} else {
					fromName = "TeachUs";
					email = "teachus@billen.dk";
				}
				
				message.setFrom(new InternetAddress(email, fromName));
				message.setTo(new InternetAddress(person.getEmail(), person.getName()));
				
				// Parse the template
				Map<String, Object> model = new HashMap<String, Object>();
				model.put("server", "http://teachus.billen.dk/");
				model.put("from", HtmlUtils.htmlEscape(fromName));
				model.put("name", HtmlUtils.htmlEscape(person.getName()));
				model.put("username", HtmlUtils.htmlEscape(person.getUsername()));
				model.put("password", HtmlUtils.htmlEscape(person.getPassword()));
				String template = velocityBean.mergeTemplate("dk/frankbille/teachus/dao/hibernate/WelcomeMail", model, locale);
				
				// Subject
				// First line in parsed template is the subject
				String subject = template.substring(0, template.indexOf('\n'));
				message.setSubject(subject);
				
				// Text
				String text = template.substring(template.indexOf('\n'));
				message.setText(text, true);

				mimeMessage.addHeader("X-Mailer", "TeachUs");
			}
		});
	}
}
