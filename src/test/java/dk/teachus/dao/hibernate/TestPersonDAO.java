package dk.teachus.dao.hibernate;

import java.util.Date;

import org.joda.time.DateTime;

import dk.teachus.domain.Period;
import dk.teachus.domain.Person;
import dk.teachus.domain.Pupil;
import dk.teachus.domain.Teacher;
import dk.teachus.domain.TeacherAttribute;
import dk.teachus.domain.impl.AbstractTeacherAttribute;
import dk.teachus.domain.impl.PeriodImpl;
import dk.teachus.domain.impl.PupilImpl;
import dk.teachus.domain.impl.TeacherImpl;
import dk.teachus.domain.impl.WelcomeIntroductionTeacherAttribute;
import dk.teachus.domain.impl.PeriodImpl.WeekDay;
import dk.teachus.frontend.WicketSpringTestCase;

public class TestPersonDAO extends WicketSpringTestCase {
	private static final long serialVersionUID = 1L;

	public void testPasswordUserType() {
		Person person = getPersonDAO().authenticatePerson("admin", "admin");
		endTransaction();
		assertNotNull(person);
	}
	
	public void testSave() {
		Teacher teacher = (Teacher) getPersonDAO().getPerson(2L);
		endTransaction();
		
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
	
}
