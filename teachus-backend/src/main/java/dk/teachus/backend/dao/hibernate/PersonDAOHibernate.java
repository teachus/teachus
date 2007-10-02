/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package dk.teachus.backend.dao.hibernate;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.DistinctRootEntityResultTransformer;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import dk.teachus.backend.dao.PersonDAO;
import dk.teachus.backend.domain.Admin;
import dk.teachus.backend.domain.Person;
import dk.teachus.backend.domain.Pupil;
import dk.teachus.backend.domain.Teacher;
import dk.teachus.backend.domain.TeacherAttribute;
import dk.teachus.backend.domain.impl.AbstractTeacherAttribute;
import dk.teachus.backend.domain.impl.AdminImpl;
import dk.teachus.backend.domain.impl.PersonImpl;
import dk.teachus.backend.domain.impl.PupilImpl;
import dk.teachus.backend.domain.impl.TeacherImpl;

@Transactional(propagation=Propagation.REQUIRED)
public class PersonDAOHibernate extends HibernateDaoSupport implements PersonDAO {
	private static final long serialVersionUID = 1L;

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

		c.addOrder(Order.asc("name"));
		if (Pupil.class.equals(personClass)) {
			c.add(Restrictions.eq("active", true));
		}
		
		c.setResultTransformer(new DistinctRootEntityResultTransformer());

		return getHibernateTemplate().findByCriteria(c);
	}

	@SuppressWarnings("unchecked")
	@Transactional(readOnly=true)
	public List<Pupil> getPupils(Teacher teacher) {
		DetachedCriteria c = DetachedCriteria.forClass(PupilImpl.class);
		
		c.add(Restrictions.eq("active", true));
		c.createCriteria("teacher").add(Restrictions.eq("active", true));
		c.add(Restrictions.eq("teacher", teacher));

		c.addOrder(Order.asc("name"));
		
		c.setResultTransformer(new DistinctRootEntityResultTransformer());
		
		return getHibernateTemplate().findByCriteria(c);
	}

	@Transactional(readOnly=true)
	public Person getPerson(Long personId) {
		return (Person) getHibernateTemplate().get(PersonImpl.class, personId);
	}
	
	public void setInactive(Long personId) {
		Person person = getPerson(personId);
		person.setActive(false);
		save(person);
	}
	
	public void saveAttribute(TeacherAttribute attribute) {
		if (attribute.getValue() == null 
				|| attribute.getValue().length() == 0) {
			if (attribute.getId() != null) {
				attribute = (TeacherAttribute) getHibernateTemplate().load(AbstractTeacherAttribute.class, attribute.getId());
				getHibernateTemplate().delete(attribute);
				getHibernateTemplate().flush();
			}
		} else {
			getHibernateTemplate().saveOrUpdate(attribute);
		}
	}
	
	@SuppressWarnings("unchecked")
	@Transactional(readOnly=true)
	public List<TeacherAttribute> getAttributes(Teacher teacher) {
		DetachedCriteria c = DetachedCriteria.forClass(AbstractTeacherAttribute.class);
		
		c.add(Restrictions.eq("teacher", teacher));
		c.createCriteria("teacher").add(Restrictions.eq("active", true));
		
		return getHibernateTemplate().findByCriteria(c);
	}
	
	@SuppressWarnings("unchecked")
	@Transactional(readOnly=true)
	public <A extends TeacherAttribute> A getAttribute(Class<A> attributeClass, Teacher teacher) {
		DetachedCriteria c = DetachedCriteria.forClass(attributeClass);
		
		c.add(Restrictions.eq("teacher", teacher));
		c.createCriteria("teacher").add(Restrictions.eq("active", true));
		
		List<A> attributes = getHibernateTemplate().findByCriteria(c);
		
		A attribute = null;
		
		if (attributes.size() == 1) {
			attribute = attributes.get(0);
		} else if (attributes.size() > 1) {
			throw new IllegalStateException("Invalid state in database. Only one value per attribute per teacher");
		}
		
		return attribute; 
	}

	@Transactional(readOnly=true)
	public Person usernameExists(String username) {
		Person existingPerson = null;
		
		DetachedCriteria c = DetachedCriteria.forClass(PersonImpl.class);
		c.add(Restrictions.eq("username", username));
		
		List<?> result = getHibernateTemplate().findByCriteria(c);
		
		if (result.size() > 0) {
			existingPerson = (Person) result.get(0);
		}
		
		return existingPerson;
	}
	
	public void deleteTeacher(Teacher teacher) {
		// Delete all teacher attributes
		getHibernateTemplate().bulkUpdate("DELETE AbstractTeacherAttribute WHERE teacher = ?", teacher);
		
		// Delete all teacher bookings
		getHibernateTemplate().bulkUpdate("DELETE TeacherBookingImpl WHERE teacher = ?", teacher);
		
		// Delete all pupil bookings
		getHibernateTemplate().bulkUpdate("DELETE PupilBookingImpl WHERE teacher = ?", teacher);
		
		// Delete all periods
		getHibernateTemplate().bulkUpdate("DELETE PeriodImpl WHERE teacher = ?", teacher);
		
		// Delete all pupils
		getHibernateTemplate().bulkUpdate("DELETE PupilImpl WHERE teacher = ?", teacher);
		
		// Finally delete the teacher
		getHibernateTemplate().delete(teacher);
	}

	@Transactional(readOnly=true)
	public Admin createAdminObject() {
		return new AdminImpl();
	}

	@Transactional(readOnly=true)
	public Teacher createTeacherObject() {
		return new TeacherImpl();
	}

	@Transactional(readOnly=true)
	public Pupil createPupilObject() {
		return new PupilImpl();
	}
	
	public void changeActiveState(Long personId) {
		Person person = getPerson(personId);
		
		person.setActive(person.isActive() == false);
		
		save(person);
	}
	
}
