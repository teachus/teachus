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
package dk.teachus.frontend.test;

import java.io.Serializable;
import java.lang.reflect.Proxy;
import java.util.Date;
import java.util.TimeZone;

import org.apache.wicket.Component;
import org.apache.wicket.Request;
import org.apache.wicket.Response;
import org.apache.wicket.Session;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.util.tester.TagTester;
import org.apache.wicket.util.tester.WicketTester;
import org.jmock.integration.junit3.MockObjectTestCase;
import org.joda.time.DateMidnight;

import dk.teachus.backend.dao.ApplicationDAO;
import dk.teachus.backend.dao.BookingDAO;
import dk.teachus.backend.dao.PeriodDAO;
import dk.teachus.backend.dao.PersonDAO;
import dk.teachus.backend.domain.Admin;
import dk.teachus.backend.domain.ApplicationConfiguration;
import dk.teachus.backend.domain.Period;
import dk.teachus.backend.domain.Period.Status;
import dk.teachus.backend.domain.Person;
import dk.teachus.backend.domain.Pupil;
import dk.teachus.backend.domain.PupilBooking;
import dk.teachus.backend.domain.TeachUsDate;
import dk.teachus.backend.domain.Teacher;
import dk.teachus.backend.domain.impl.AdminImpl;
import dk.teachus.backend.domain.impl.ApplicationConfigurationImpl;
import dk.teachus.backend.domain.impl.PeriodImpl;
import dk.teachus.backend.domain.impl.PeriodImpl.WeekDay;
import dk.teachus.backend.domain.impl.PupilBookingImpl;
import dk.teachus.backend.domain.impl.PupilImpl;
import dk.teachus.backend.domain.impl.TeacherImpl;
import dk.teachus.frontend.TeachUsApplication;
import dk.teachus.frontend.TeachUsSession;
import dk.teachus.frontend.UserLevel;
import dk.teachus.frontend.pages.LazyInitProxy;

public abstract class WicketTestCase extends MockObjectTestCase implements Serializable {
	private static final long serialVersionUID = 1L;

	public static class TestTeachUsApplication extends TeachUsApplication {		
		private TeachUsWicketTester tester;
		
		private Long personId;
		
		public void setPersonId(Long personId) {
			this.personId = personId;
		}

		public void setTester(TeachUsWicketTester tester) {
			this.tester = tester;
		}

		@Override
		public Session newSession(Request request, Response response) {
			TesterTeachUsSession session = new TesterTeachUsSession(this, request);
			session.setPerson(personId);							
			return session;
		}
		
		@Override
		protected void init() {
			super.init();
		}
		
		@Override
		public BookingDAO getBookingDAO() {
			return tester.getBookingDAO();
		}
		
		@Override
		public PeriodDAO getPeriodDAO() {
			return tester.getPeriodDAO();
		}
		
		@Override
		public PersonDAO getPersonDAO() {
			return tester.getPersonDAO();
		}
		
		@Override
		protected ApplicationDAO getApplicationDAO() {
			if (tester == null) {
				ApplicationDAO applicationDAO = new ApplicationDAO() {
					public ApplicationConfiguration loadConfiguration() {
						ApplicationConfigurationImpl conf = new ApplicationConfigurationImpl(null);
						
						conf.setConfiguration(ApplicationConfiguration.SERVER_URL, "http://localhost:8080/");
						conf.setConfiguration(ApplicationConfiguration.VERSION, "1.2.3");
						
						return conf;
					}

					public void saveConfiguration(ApplicationConfiguration configuration) {
						
					}					
				};
				
				return applicationDAO;
			} else {
				return tester.getApplicationDAO();
			}			
		}
	}
	
	public static class TeachUsWicketTester extends WicketTester implements Serializable {
		private static final long serialVersionUID = 1L;
		
		private BookingDAO bookingDAO;
		private PeriodDAO periodDAO;
		private PersonDAO personDAO;
		private ApplicationDAO applicationDAO;
		
		public TeachUsWicketTester(TestTeachUsApplication application) {
			super(application);
			
			application.setTester(this);
		}
		
		public BookingDAO getBookingDAO() {
			return bookingDAO;
		}

		public void setBookingDAO(BookingDAO bookingDAO) {
			Class<?>[] interfaces = new Class[] {
				BookingDAO.class
			};
			this.bookingDAO = (BookingDAO) Proxy.newProxyInstance(BookingDAO.class.getClassLoader(), interfaces, new LazyInitProxy(bookingDAO));
		}

		public PeriodDAO getPeriodDAO() {
			return periodDAO;
		}

		public void setPeriodDAO(PeriodDAO periodDAO) {
			Class<?>[] interfaces = new Class[] {
					PeriodDAO.class
			};
			this.periodDAO = (PeriodDAO) Proxy.newProxyInstance(PeriodDAO.class.getClassLoader(), interfaces, new LazyInitProxy(periodDAO));
		}
		
		public PersonDAO getPersonDAO() {
			return personDAO;
		}

		public void setPersonDAO(PersonDAO personDAO) {
			Class<?>[] interfaces = new Class[] {
					PersonDAO.class
			};
			this.personDAO = (PersonDAO) Proxy.newProxyInstance(PersonDAO.class.getClassLoader(), interfaces, new LazyInitProxy(personDAO));
		}

		public ApplicationDAO getApplicationDAO() {			
			return applicationDAO;
		}

		public void setApplicationDAO(ApplicationDAO applicationDAO) {
			Class<?>[] interfaces = new Class[] {
					ApplicationDAO.class
			};
			this.applicationDAO = (ApplicationDAO) Proxy.newProxyInstance(ApplicationDAO.class.getClassLoader(), interfaces, new LazyInitProxy(applicationDAO));
		}

		public TeachUsApplication getTeachUsApplication() {
			return (TeachUsApplication) getApplication();
		}
	}
	
	public static class TesterTeachUsSession extends TeachUsSession {
		private static final long serialVersionUID = 1L;
		
		public static TesterTeachUsSession get() {
			return (TesterTeachUsSession) TeachUsSession.get();
		}

		private Long personId;
		
		public TesterTeachUsSession(WebApplication application, Request request) {
			super(request);
		}
		
		@Override
		public Person getPerson() {
			if (person == null) {
				person = TeachUsApplication.get().getPersonDAO().getPerson(personId);
			}
			
			return person;
		}
		
		@Override
		public UserLevel getUserLevel() {
			person = getPerson();
			
			return super.getUserLevel();
		}
		
		@Override
		public boolean isAuthenticated() {
			return personId != null;
		}
		
		private void setPerson(Long personId) {
			this.personId = personId;
			this.person = null;
		}
	}
	
	protected TeachUsWicketTester createTester() {
		return createTester(2);
	}
	
	protected TeachUsWicketTester createTester(long personId) {
		TestTeachUsApplication testTeachUsApplication = new TestTeachUsApplication();
		testTeachUsApplication.setPersonId(personId);
		TeachUsWicketTester tester = new TeachUsWicketTester(testTeachUsApplication);
		
		return tester;
	}
	
	protected PersonDAO createPersonDAO() {
		return mock(PersonDAO.class);
	}
	
	protected PeriodDAO createPeriodDAO() {
		return mock(PeriodDAO.class);
	}
	
	protected BookingDAO createBookingDAO() {
		return mock(BookingDAO.class);
	}
	
	protected void assertTimeNotSelected(TeachUsWicketTester tester, String componentPath) {
		TagTester tagTester = getTagTesterForComponent(tester, componentPath);
		assertTrue("The time was booked", tagTester.getAttributeContains("class", "nobooking"));
	}
	
	protected void assertTimeOccupied(TeachUsWicketTester tester, String componentPath) {
		TagTester tagTester = getTagTesterForComponent(tester, componentPath);
		assertTrue(tagTester.getAttributeContains("class", "pupilBooked"));
	}
	
	protected void assertTimeSelected(TeachUsWicketTester tester, String componentPath) {
		TagTester tagTester = getTagTesterForComponent(tester, componentPath);
		System.out.println(tagTester.getAttribute("class"));
		assertTrue("The time wasn't booked", tagTester.getAttributeContains("class", "booked"));
	}

	private TagTester getTagTesterForComponent(TeachUsWicketTester tester, String componentPath) {
		Component comp = tester.getComponentFromLastRenderedPage(componentPath);
		TagTester tagTester = tester.getTagById(comp.getMarkupId());
		return tagTester;
	}
	
	protected Admin createAdmin() {
		return createAdmin(1L);
	}
	
	protected Admin createAdmin(Long adminId) {
		AdminImpl admin = new AdminImpl();
		admin.setId(adminId);
		admin.setActive(true);
		admin.setEmail("admin@teachus.dk");
		admin.setName("Mock Admin");
		admin.setUsername("admin");
		admin.setPassword("FEDCBA654321");
		return admin;
	}
	
	protected Teacher createTeacher() {
		return createTeacher(2L);
	}
	
	protected Teacher createTeacher(Long teacherId) {
		TeacherImpl teacher = new TeacherImpl();
		teacher.setId(teacherId);
		teacher.setActive(true);
		teacher.setCurrency("kr");
		teacher.setEmail("teacher@teachus.dk");
		teacher.setName("Mock Teacher");
		teacher.setUsername("teacher");
		teacher.setPassword("AFSF64523673946FJRGE23464");
		return teacher;
	}
	
	protected Pupil createPupil() {
		return createPupil(3L);
	}
	
	protected Pupil createPupil(Long pupilId) {
		return createPupil(pupilId, createTeacher());
	}
	
	protected Pupil createPupil(Long pupilId, Teacher teacher) {
		PupilImpl pupil = new PupilImpl();
		pupil.setId(pupilId);
		pupil.setActive(true);
		pupil.setEmail("pupil@teachus.dk");
		pupil.setName("Mock Pupil");
		pupil.setTeacher(teacher);
		pupil.setUsername("pupil");
		pupil.setPassword("ABCDEF123456");
		return pupil;
	}
	
	protected Period createPeriod() {
		return createPeriod(1L);
	}
	
	protected Period createPeriod(Long periodId) {
		TimeZone timeZone = TimeZone.getDefault();
		
		TeachUsDate beginDate = new TeachUsDate(2007, 1, 1, timeZone);
		TeachUsDate endDate = new TeachUsDate(2007, 12, 31, timeZone);
		TeachUsDate startTime = new TeachUsDate(new DateMidnight(), timeZone).withTime(10, 0, 0, 0);
		TeachUsDate endTime = new TeachUsDate(new DateMidnight(), timeZone).withTime(18, 0, 0, 0);
		return createPeriod(periodId, createTeacher(), beginDate, endDate, startTime, endTime);
	}
	
	protected Period createPeriod(Long periodId, Teacher teacher, TeachUsDate beginDate, TeachUsDate endDate, TeachUsDate startTime, TeachUsDate endTime) {
		PeriodImpl period = new PeriodImpl();
		period.setId(periodId);
		period.setStatus(Status.FINAL);
		period.setBeginDate(beginDate);
		period.setEndDate(endDate);
		period.setStartTime(startTime);
		period.setEndTime(endTime);
		period.setName("Mock period");
		period.setPrice(200);
		period.setTeacher(teacher);
		period.addWeekDay(WeekDay.MONDAY);
		period.addWeekDay(WeekDay.WEDNESDAY);
		period.addWeekDay(WeekDay.FRIDAY);
		return period; 
	}

	protected PupilBooking createPupilBooking() {
		return createPupilBooking(1L);
	}
	
	protected PupilBooking createPupilBooking(Long pupilBookingId) {
		TeachUsDate dateTime = new TeachUsDate(2007, 4, 20, 13, 0, 0, TimeZone.getDefault());
		return createPupilBooking(pupilBookingId, dateTime);
	}
	
	protected PupilBooking createPupilBooking(Long pupilBookingId, TeachUsDate dateTime) {
		return createPupilBooking(pupilBookingId, createPupil(), createPeriod(), dateTime);
	}
	
	protected PupilBooking createPupilBooking(Long pupilBookingId, Pupil pupil, Period period, TeachUsDate dateTime) {
		PupilBookingImpl booking = new PupilBookingImpl();
		booking.setId(pupilBookingId);
		booking.setActive(true);
		booking.setCreateDate(new TeachUsDate(new Date(), TimeZone.getDefault()));
		booking.setDate(dateTime);
		booking.setPupil(pupil);
		booking.setTeacher(pupil.getTeacher());
		booking.setPeriod(period);
		return booking;
	}
	
}
