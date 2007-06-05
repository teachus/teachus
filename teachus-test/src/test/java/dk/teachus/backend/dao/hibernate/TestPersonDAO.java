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

import java.util.Date;
import java.util.List;

import org.joda.time.DateTime;

import dk.teachus.backend.domain.Admin;
import dk.teachus.backend.domain.Period;
import dk.teachus.backend.domain.Person;
import dk.teachus.backend.domain.Pupil;
import dk.teachus.backend.domain.Teacher;
import dk.teachus.backend.domain.TeacherAttribute;
import dk.teachus.backend.domain.impl.AbstractTeacherAttribute;
import dk.teachus.backend.domain.impl.PeriodImpl;
import dk.teachus.backend.domain.impl.PupilImpl;
import dk.teachus.backend.domain.impl.TeacherImpl;
import dk.teachus.backend.domain.impl.WelcomeIntroductionTeacherAttribute;
import dk.teachus.backend.domain.impl.PeriodImpl.WeekDay;
import dk.teachus.backend.test.SpringTestCase;

public class TestPersonDAO extends SpringTestCase {
	private static final long serialVersionUID = 1L;

	public void testPasswordUserType() {
		Person person = getPersonDAO().authenticatePerson("admin", "admin");
		endTransaction();
		assertNotNull(person);
	}
	
	public void testSave() {
		Teacher teacher = getTeacher();
		
		Pupil pupil = new PupilImpl();
		pupil.setName("Test");
		pupil.setUsername("test");
		pupil.setActive(true);
		pupil.setEmail("test@teachus.dk");
		pupil.setTeacher(teacher);
		
		getPersonDAO().save(pupil);
		endTransaction();
	}

	public void testSaveTeacher() {
		Teacher teacher = new TeacherImpl();
		teacher.setName("Test");
		teacher.setUsername("test");
		teacher.setActive(true);
		teacher.setEmail("test@teachus.dk");
		teacher.setCurrency("kr");
		
		getPersonDAO().save(teacher);
	}
	
	public void testUsernameExists() {
		Person usernameExists = getPersonDAO().usernameExists("admin");
		assertNotNull(usernameExists);
		assertEquals(new Long(1), usernameExists.getId());
		
		assertNull(getPersonDAO().usernameExists("admin2"));
		
		assertNull(getPersonDAO().usernameExists("admi"));
	}
	
	public void testDeleteTeacher() {
		int teachersBefore = getPersonDAO().getPersons(Teacher.class).size();
		int pupilsBefore = getPersonDAO().getPersons(Pupil.class).size();
		
		
		// Create a teacher 
		Teacher teacher = createTeacher();
		
		// Add a teacher attribute
		TeacherAttribute attribute = new WelcomeIntroductionTeacherAttribute();
		attribute.setTeacher(teacher);
		attribute.setValue("Test value");
		getPersonDAO().saveAttribute(attribute);
		endTransaction();
		
		// Create a periods
		Period period = new PeriodImpl();
		period.setTeacher(teacher);
		period.setActive(true);
		period.setBeginDate(new Date());
		period.setStartTime(new DateTime().withTime(10, 0, 0, 0).toDate());
		period.setEndTime(new DateTime().withTime(16, 0, 0, 0).toDate());
		period.addWeekDay(WeekDay.FRIDAY);
		period.setName("Test period");
		getPeriodDAO().save(period);
		
		// Create some pupils for the teacher
		Pupil pupil1 = createPupil(teacher, 1);
		Pupil pupil2 = createPupil(teacher, 2);
		
		// Create some bookings for the teacher and pupils
		DateTime date = new DateTime().plusWeeks(1).withDayOfWeek(WeekDay.FRIDAY.getYodaWeekDay());
		date = date.withTime(11, 0, 0, 0);
		Long pupilBooking1 = createPupilBooking(period.getId(), pupil1.getId(), date, null);
		date = date.withTime(13, 0, 0, 0);
		Long pupilBooking2 = createPupilBooking(period.getId(), pupil2.getId(), date, null);
		
		date = date.withTime(15, 0, 0, 0);
		Long teacherBooking = createTeacherBooking(period.getId(), teacher.getId(), date);
		
		
		// Now delete the whole teacher
		getPersonDAO().deleteTeacher(teacher);
		
		
		// Try to load the different pupils, periods and bookings to see if they are really deleted
		assertNull(getPersonDAO().getPerson(teacher.getId()));
		assertNull(getPersonDAO().getPerson(pupil1.getId()));
		assertNull(getPersonDAO().getPerson(pupil2.getId()));
		assertNull(getBookingDAO().getBooking(pupilBooking1));
		assertNull(getBookingDAO().getBooking(pupilBooking2));
		assertNull(getBookingDAO().getBooking(teacherBooking));
		assertNull(getPeriodDAO().get(period.getId()));
		assertNull(loadObject(AbstractTeacherAttribute.class, attribute.getId()));
		
		
		int teachersAfter = getPersonDAO().getPersons(Teacher.class).size();
		int pupilsAfter = getPersonDAO().getPersons(Pupil.class).size();
		
		assertEquals(teachersBefore, teachersAfter);
		assertEquals(pupilsBefore, pupilsAfter);
	}
	
	public void testAuthenticatePerson() {
		Person person = getPersonDAO().authenticatePerson("sadolin", "sadolin");
		endTransaction();
		
		assertNotNull(person);
		
		assertEquals(new Long(2L), person.getId());
	}
	
	public void testAuthenticatePerson_inactive() {
		inactivateTeacher();
		
		// Authenticate
		Person person = getPersonDAO().authenticatePerson("sadolin", "sadolin");
		endTransaction();
		
		assertNull(person);
	}
	
	public void testGetPersons_pupils_onlyActive() {
		List<Pupil> persons = getPersonDAO().getPersons(Pupil.class);
		endTransaction();
		
		int personsBefore = persons.size();
		
		getPersonDAO().changeActiveState(3L);
		endTransaction();
		getPersonDAO().changeActiveState(4L);
		endTransaction();
		
		// Get persons
		persons = getPersonDAO().getPersons(Pupil.class);
		endTransaction();

		int personsAfter = persons.size();
		
		assertEquals(personsBefore-2, personsAfter);
	}
	
	/**
	 * Show teachers even if inactive.
	 */
	public void testGetPersons_teacher() {
		inactivateTeacher();
		
		// Get persons
		List<Teacher> persons = getPersonDAO().getPersons(Teacher.class);
		endTransaction();
		
		assertEquals(1, persons.size());
	}
	
	/**
	 * Show admins even if inactive.
	 */
	public void testGetPersons_admin() {
		inactivateTeacher();
		
		// Get persons
		List<Admin> persons = getPersonDAO().getPersons(Admin.class);
		endTransaction();
		
		assertEquals(1, persons.size());
	}
	
	public void testGetPupils_inactiveTeacher() {
		Teacher teacher = inactivateTeacher();
		
		// Get pupils
		List<Pupil> pupils = getPersonDAO().getPupils(teacher);
		endTransaction();
		
		assertEquals(0, pupils.size());
	}
	
	public void testGetAttributes_inactiveTeacher() {
		Teacher teacher = inactivateTeacher();
		
		List<TeacherAttribute> attributes = getPersonDAO().getAttributes(teacher);
		endTransaction();
		
		assertEquals(0, attributes.size());
	}
	
	public void testGetAttribute_inactiveTeacher() {
		Teacher teacher = getTeacher();
		
		// Get the attribute to see that we can get it
		TeacherAttribute attribute = getPersonDAO().getAttribute(WelcomeIntroductionTeacherAttribute.class, teacher);
		endTransaction();
		
		assertNotNull(attribute);
		
		teacher = inactivateTeacher();
		
		attribute = getPersonDAO().getAttribute(WelcomeIntroductionTeacherAttribute.class, teacher);
		endTransaction();
		
		assertNull(attribute);
	}
	
	public void testChangeActiveState_admin() {
		long personId = 1L;
		
		Person person = getPersonDAO().getPerson(personId);
		endTransaction();
		
		assertTrue(person instanceof Admin);
		assertTrue(person.isActive());
		
		getPersonDAO().changeActiveState(personId);
		endTransaction();
		
		person = getPersonDAO().getPerson(personId);
		endTransaction();
		
		assertFalse(person.isActive());
	}
	
	public void testChangeActiveState_teacher() {
		long personId = 2L;
		
		Person person = getPersonDAO().getPerson(personId);
		endTransaction();
		
		assertTrue(person instanceof Teacher);
		assertTrue(person.isActive());
		
		getPersonDAO().changeActiveState(personId);
		endTransaction();
		
		person = getPersonDAO().getPerson(personId);
		endTransaction();
		
		assertFalse(person.isActive());
	}
	
	public void testChangeActiveState_pupil() {
		long personId = 3L;
		
		Person person = getPersonDAO().getPerson(personId);
		endTransaction();
		
		assertTrue(person instanceof Pupil);
		assertTrue(person.isActive());
		
		getPersonDAO().changeActiveState(personId);
		endTransaction();
		
		person = getPersonDAO().getPerson(personId);
		endTransaction();
		
		assertFalse(person.isActive());
	}
	
}
