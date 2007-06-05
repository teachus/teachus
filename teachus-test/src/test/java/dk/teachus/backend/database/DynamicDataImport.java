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
package dk.teachus.backend.database;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.SQLQuery;
import org.hibernate.SessionFactory;
import org.hibernate.classic.Session;
import org.hibernate.criterion.Restrictions;
import org.joda.time.DateMidnight;
import org.joda.time.DateTime;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import dk.teachus.backend.domain.Admin;
import dk.teachus.backend.domain.Period;
import dk.teachus.backend.domain.Pupil;
import dk.teachus.backend.domain.PupilBooking;
import dk.teachus.backend.domain.Teacher;
import dk.teachus.backend.domain.TeacherAttribute;
import dk.teachus.backend.domain.Theme;
import dk.teachus.backend.domain.impl.AdminImpl;
import dk.teachus.backend.domain.impl.PeriodImpl;
import dk.teachus.backend.domain.impl.PupilBookingImpl;
import dk.teachus.backend.domain.impl.PupilImpl;
import dk.teachus.backend.domain.impl.TeacherImpl;
import dk.teachus.backend.domain.impl.WelcomeIntroductionTeacherAttribute;
import dk.teachus.backend.domain.impl.PeriodImpl.WeekDay;

public abstract class DynamicDataImport {
	private static final Log log = LogFactory.getLog(DynamicDataImport.class);
	
	public static void main(String[] args) {
		ApplicationContext context = new ClassPathXmlApplicationContext(new String[] {
				"/dk/teachus/backend/applicationContext.xml",
				"/dk/teachus/backend/database/applicationContext-dynamicDataImport.xml"
		});

		SessionFactory sessionFactory = (SessionFactory) context.getBean("sessionFactory");

		DateMidnight startDate = new DateMidnight().minusYears(3).withMonthOfYear(1).withDayOfMonth(10);
		DateMidnight endDate = new DateMidnight().plusYears(1).withMonthOfYear(12).withDayOfMonth(31);

		deleteExistingData(sessionFactory);
		
		createAdmin(sessionFactory);
		
		int numberOfTeachers = 10;
		
		for (int i = 1; i <= numberOfTeachers; i++) {
			log.info("Creating teacher number " + i + " of " + numberOfTeachers);
			
			Teacher teacher = createTeacher(sessionFactory, i);
			
			createTeacherAttribute(sessionFactory, teacher);
			
			createPupils(sessionFactory, teacher, i);
			
			createPeriods(sessionFactory, teacher, startDate);
			
			createBookings(sessionFactory, teacher, startDate, endDate);
			
			verify(sessionFactory, teacher);
		}
		
		System.exit(0);
	}

	private static void createTeacherAttribute(SessionFactory sessionFactory, Teacher teacher) {
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		
		TeacherAttribute attribute = new WelcomeIntroductionTeacherAttribute();
		attribute.setTeacher(teacher);
		attribute.setValue("A welcome introduction");
		session.save(attribute);
		
		session.getTransaction().commit();
		session.close();
	}
	
	private static void verify(SessionFactory sessionFactory, Teacher teacher) {
		Session session = sessionFactory.openSession();
		session.beginTransaction();

		SQLQuery sqlQuery = session.createSQLQuery("SELECT date, COUNT(id) AS c FROM booking WHERE teacher_id = "+teacher.getId()+" GROUP BY date HAVING c > 1");
		List result = sqlQuery.list();
		
		for (Object object : result) {
			Object[] objects = (Object[]) object;
			log.error(objects[0] + ": " + objects[1]);
		}
		
		session.getTransaction().commit();
		session.close();		
	}
	
	private static void createBookings(SessionFactory sessionFactory, Teacher teacher, DateMidnight startDate, DateMidnight endDate) {
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		
		Set<DateTime> bookedDates = new HashSet<DateTime>();
		
		List<Pupil> pupils = getPupils(teacher, session);
		List<Period> periods = getPeriods(teacher, session);
		
		DateMidnight now = new DateMidnight();
		
		for (Pupil pupil : pupils) {
			DateMidnight date = new DateMidnight(startDate);
			while(date.isBefore(endDate)) {
				// First see if the pupil should have a booking here at all
				long haveBooking = Math.round(Math.random());
				if (haveBooking == 1) {
					// Then find out which period to book in
					int periodIndex = (int) (Math.random()*periods.size());
					Period period = periods.get(periodIndex);
					
					// Then find out which day in the period to book in
					int weekDayIndex = (int) (Math.random()*period.getWeekDays().size());
					WeekDay weekDay = period.getWeekDays().get(weekDayIndex);
					DateMidnight weekDayDate = date.withDayOfWeek(weekDay.getYodaWeekDay());
					
					// Now find out which time of day to use
					int startHour = new DateTime(period.getStartTime()).getHourOfDay();
					int endHour = new DateTime(period.getEndTime()).getHourOfDay();
					int duration = endHour - startHour;
					int bookHour = startHour + (int) (Math.random()*duration);
					DateTime bookTime = weekDayDate.toDateTime().withHourOfDay(bookHour);
					
					if (bookedDates.contains(bookTime) == false) {
						bookedDates.add(bookTime);
						
						// Create booking
						PupilBooking booking = new PupilBookingImpl();
						booking.setCreateDate(new Date());
						booking.setDate(bookTime.toDate());
						booking.setPeriod(period);
						booking.setTeacher(teacher);
						booking.setPupil(pupil);
						booking.setNotificationSent(true);
						
						// Set the paid based on the booking time is in the past
						if (now.isAfter(bookTime)) {
							// If it's within a month, then give it a 40% change of not have been paid
							if (now.minusMonths(1).isBefore(bookTime)) {
								int pct = (int) (Math.random()*100.0);
								if (pct >= 40) {
									booking.setPaid(true);
								}
							} else {
								booking.setPaid(true);
							}
						}
						
						// Book
						session.save(booking);
					}
				}
				
				date = date.plusWeeks(1);
			}
		}
		
		session.getTransaction().commit();
		session.close();
	}

	@SuppressWarnings("unchecked")
	private static List<Pupil> getPupils(Teacher teacher, Session session) {
		Criteria criteria = session.createCriteria(PupilImpl.class);
		criteria.add(Restrictions.eq("teacher", teacher));
		List<Pupil> pupils = criteria.list();
		return pupils;
	}

	@SuppressWarnings("unchecked")
	private static List<Period> getPeriods(Teacher teacher, Session session) {
		Criteria criteria = session.createCriteria(PeriodImpl.class);
		criteria.add(Restrictions.eq("teacher", teacher));
		List<Period> periods = criteria.list();
		return periods;
	}
	
	private static void createPeriods(SessionFactory sessionFactory, Teacher teacher, DateMidnight startDate) {
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		
		// Periods starting 1. january 3 years ago
		Period period = new PeriodImpl();
		period.setName("Mon/Wed/Fri");
		period.setBeginDate(startDate.toDate());
		period.setStartTime(startDate.toDateTime().withTime(10, 0, 0, 0).toDate());
		period.setEndTime(startDate.toDateTime().withTime(17, 0, 0, 0).toDate());
		period.setLocation("Odense");
		period.setPrice(450);
		period.addWeekDay(WeekDay.MONDAY);
		period.addWeekDay(WeekDay.WEDNESDAY);
		period.addWeekDay(WeekDay.FRIDAY);
		period.setTeacher(teacher);
		session.save(period);
		
		period = new PeriodImpl();
		period.setName("Tue/Thu");
		period.setBeginDate(startDate.toDate());
		period.setStartTime(startDate.toDateTime().withTime(9, 0, 0, 0).toDate());
		period.setEndTime(startDate.toDateTime().withTime(17, 0, 0, 0).toDate());
		period.setLocation("Copenhagen");
		period.setPrice(450);
		period.addWeekDay(WeekDay.TUESDAY);
		period.addWeekDay(WeekDay.THURSDAY);
		period.setTeacher(teacher);
		session.save(period);
		
		session.getTransaction().commit();
		session.close();
	}
	
	private static Admin createAdmin(SessionFactory sessionFactory) {
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		
		Admin admin = new AdminImpl();
		admin.setName("Admin");
		admin.setUsername("admin");
		admin.setPassword("admin");
		admin.setEmail("admin@teachus.dk");
		admin.setLocale(new Locale("en"));
		session.save(admin);
		
		session.getTransaction().commit();
		session.close();
		
		return admin;
	}

	private static void createPupils(SessionFactory sessionFactory, Teacher teacher, int number) {
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		
		String postfixUnix = "";
		String postfixName = "";
		if (number > 1) {
			postfixUnix = "_" + number;
			postfixName = " " + number;
		}
		
		for (String firstName : Names.firstNames) {
			int lastNameIndex = (int) (Math.random()*Names.lastNames.size());
			String lastName = Names.lastNames.get(lastNameIndex);
			String name = firstName + " " + lastName;
			String unixName = name
					.toLowerCase()
					.replace(" ", "_")
					.replace("ü", "u")
					.replace("å", "aa")
					.replace("ø", "o");
			unixName += postfixUnix;
			
			Pupil pupil = new PupilImpl();
			pupil.setName(name+postfixName);
			pupil.setEmail(unixName + "@teachus.dk");
			pupil.setUsername(unixName);
			pupil.setPassword(unixName);
			pupil.setTeacher(teacher);
			session.save(pupil);
			log.debug("Created pupil: "+unixName);
		}
		
		session.getTransaction().commit();
		session.close();
	}

	private static Teacher createTeacher(SessionFactory sessionFactory, int number) {
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		
		String postfixUnix = "";
		String postfixName = "";
		if (number > 1) {
			postfixUnix = "_" + number;
			postfixName = " " + number;
		}
		
		Teacher teacher = new TeacherImpl();
		teacher.setName("Leif Borilde"+postfixName);
		teacher.setUsername("leif"+postfixUnix);
		teacher.setPassword("leif"+postfixUnix);
		teacher.setEmail("leif"+postfixUnix+"@teachus.dk");
		teacher.setLocale(new Locale("en", "US", "singers"));
		teacher.setTheme(Theme.GREEN);
		teacher.setCurrency("kr");
		session.save(teacher);
		
		session.getTransaction().commit();
		session.close();
		
		return teacher;
	}

	private static void deleteExistingData(SessionFactory sessionFactory) {
		executeSql(sessionFactory, "UPDATE person SET teacher_id = NULL");
		
		executeSql(sessionFactory, "TRUNCATE booking");
		executeSql(sessionFactory, "TRUNCATE period");
		executeSql(sessionFactory, "TRUNCATE teacher_attribute");
		executeSql(sessionFactory, "TRUNCATE person");
	}
	
	private static void executeSql(SessionFactory sessionFactory, String sql) {
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		
		SQLQuery sqlQuery = session.createSQLQuery(sql);
		sqlQuery.executeUpdate();
		
		session.getTransaction().commit();
		session.close();
	}
	
}
