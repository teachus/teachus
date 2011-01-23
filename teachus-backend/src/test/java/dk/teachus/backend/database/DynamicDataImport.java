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

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
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
import dk.teachus.backend.domain.ApplicationConfiguration;
import dk.teachus.backend.domain.Period;
import dk.teachus.backend.domain.Pupil;
import dk.teachus.backend.domain.PupilBooking;
import dk.teachus.backend.domain.TeachUsDate;
import dk.teachus.backend.domain.Teacher;
import dk.teachus.backend.domain.TeacherAttribute;
import dk.teachus.backend.domain.Theme;
import dk.teachus.backend.domain.Period.Status;
import dk.teachus.backend.domain.impl.AdminImpl;
import dk.teachus.backend.domain.impl.ApplicationConfigurationEntry;
import dk.teachus.backend.domain.impl.PeriodImpl;
import dk.teachus.backend.domain.impl.PupilBookingImpl;
import dk.teachus.backend.domain.impl.PupilImpl;
import dk.teachus.backend.domain.impl.TeacherImpl;
import dk.teachus.backend.domain.impl.TimeZoneAttribute;
import dk.teachus.backend.domain.impl.WelcomeIntroductionTeacherAttribute;
import dk.teachus.backend.domain.impl.PeriodImpl.WeekDay;
import dk.teachus.utils.DateUtils;

public abstract class DynamicDataImport {
	private static final Log log = LogFactory.getLog(DynamicDataImport.class);
	
	public static void main(String[] args) {
		ApplicationContext context = new ClassPathXmlApplicationContext(new String[] {
				"/dk/teachus/backend/applicationContext.xml",
				"/dk/teachus/backend/database/applicationContext-dynamicDataImport.xml"
		});

		SessionFactory sessionFactory = (SessionFactory) context.getBean("sessionFactory");
		
		TimeZone timeZone = TimeZone.getDefault();

		TeachUsDate startDate = new TeachUsDate(new DateMidnight(), timeZone).minusYears(3).withMonthOfYear(1).withDayOfMonth(10);
		TeachUsDate endDate = new TeachUsDate(new DateMidnight(), timeZone).withMonthOfYear(12).withDayOfMonth(31);

		deleteExistingData(sessionFactory);
		
		createVersion(sessionFactory);
		
		createDefaultTimezone(sessionFactory, timeZone);
		
		createAdmin(sessionFactory);
		
		int numberOfTeachers = 1;
		
		for (int i = 1; i <= numberOfTeachers; i++) {
			log.info("Creating teacher number " + i + " of " + numberOfTeachers);
			
			Teacher teacher = createTeacher(sessionFactory, i);
			
			createTeacherAttribute(sessionFactory, teacher);
			
			createPupils(sessionFactory, teacher, i);
			
			createPeriods(sessionFactory, teacher, startDate);
			
			createBookings(sessionFactory, teacher, startDate, endDate);
			
			verify(sessionFactory, teacher);
		}
		
		log.info("Done");
		
		System.exit(0);
	}
	
	private static void createVersion(SessionFactory sessionFactory) {
		// Parse the pom file to get the version
		File file = new File("pom.xml");
		
		if (file.exists()) {
			try {
				String pomContent = FileUtils.readFileToString(file, "UTF-8");
				
				Pattern pattern = Pattern.compile(".*?\\<version\\>([^\\<]+)\\<\\/version>.*", Pattern.MULTILINE | Pattern.DOTALL);
				Matcher matcher = pattern.matcher(pomContent);
				if (matcher.matches()) {
					String version = matcher.group(1);
					version = version.replace("-SNAPSHOT", "");
					
					Session session = sessionFactory.openSession();
					session.beginTransaction();
					
					ApplicationConfigurationEntry entry = new ApplicationConfigurationEntry("VERSION", version);
					session.save(entry);
					
					session.getTransaction().commit();
					session.close();
				}
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
	}
	
	private static void createDefaultTimezone(SessionFactory sessionFactory, TimeZone timeZone) {
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		
		ApplicationConfigurationEntry entry = new ApplicationConfigurationEntry(ApplicationConfiguration.DEFAULT_TIMEZONE, timeZone.getID());
		session.save(entry);
		
		session.getTransaction().commit();
		session.close();
	}

	private static void createTeacherAttribute(SessionFactory sessionFactory, Teacher teacher) {
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		
		TeacherAttribute attribute = new WelcomeIntroductionTeacherAttribute();
		attribute.setTeacher(teacher);
		attribute.setValue("A welcome introduction");
		session.save(attribute);
		
		TimeZoneAttribute tzAttribute = new TimeZoneAttribute();
		tzAttribute.setTeacher(teacher);
		tzAttribute.setTimeZone(TimeZone.getDefault());
		session.save(tzAttribute);
		
		session.getTransaction().commit();
		session.close();
	}
	
	private static void verify(SessionFactory sessionFactory, Teacher teacher) {
		Session session = sessionFactory.openSession();
		session.beginTransaction();

		SQLQuery sqlQuery = session.createSQLQuery("SELECT date, COUNT(id) AS c FROM booking WHERE teacher_id = "+teacher.getId()+" GROUP BY date HAVING c > 1");
		List<?> result = sqlQuery.list();
		
		for (Object object : result) {
			Object[] objects = (Object[]) object;
			log.error(objects[0] + ": " + objects[1]);
		}
		
		session.getTransaction().commit();
		session.close();		
	}
	
	private static void createBookings(SessionFactory sessionFactory, Teacher teacher, TeachUsDate startDate, TeachUsDate endDate) {
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		
		Set<TeachUsDate> bookedDates = new HashSet<TeachUsDate>();
		
		List<Pupil> pupils = getPupils(teacher, session);
		List<Period> periods = getPeriods(teacher, session);
		
		TeachUsDate now = new TeachUsDate(new DateTime(), startDate.getTimeZone());
		
		for (Pupil pupil : pupils) {
			TeachUsDate date = startDate;
			while(date.isBefore(endDate)) {
				// First see if the pupil should have a booking here at all
				long shouldBook = Math.round(Math.random());
				if (shouldBook == 1) {
					// Then find out which period to book in
					int periodIndex = (int) (Math.random()*periods.size());
					Period period = periods.get(periodIndex);
					log.debug("Booking lesson for period: "+period.getName());
					
					// Then find out which day in the period to book in
					int weekDayIndex = (int) (Math.random()*period.getWeekDays().size());
					WeekDay weekDay = period.getWeekDays().get(weekDayIndex);
					TeachUsDate weekDayDate = date.withDayOfWeek(weekDay.getYodaWeekDay());
					
					// Now find out which time of day to use
					// We do that by listing the possible booking time entries. A lesson doesn't have to start at a whole number.
					List<TeachUsDate> avaiableLessonsStart = new ArrayList<TeachUsDate>();
					TeachUsDate bookTime = DateUtils.resetDateTime(period.getStartTime(), weekDayDate);
					TeachUsDate et = DateUtils.resetDateTime(period.getEndTime(), weekDayDate);
					while(bookTime.isBefore(et)) {
						if (period.mayBook(bookTime)) {
							avaiableLessonsStart.add(bookTime);
						}
						
						bookTime = bookTime.plusMinutes(period.getIntervalBetweenLessonStart());
					}
					
					int selectedIndex = (int) Math.round(Math.random()*(avaiableLessonsStart.size()-1));
					log.debug("Selected lesson number "+selectedIndex+" out of "+avaiableLessonsStart.size());
					bookTime = avaiableLessonsStart.get(selectedIndex);
										
					if (bookedDates.contains(bookTime) == false) {
						bookedDates.add(bookTime);
						
						// Create booking
						PupilBooking booking = new PupilBookingImpl();
						booking.setCreateDate(new TeachUsDate(new Date(), TimeZone.getDefault()));
						booking.setDate(bookTime);
						booking.setPeriod(period);
						booking.setTeacher(teacher);
						booking.setPupil(pupil);
						booking.setNotificationSent(true);
						booking.setPupilNotificationSent(true);
						
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
	
	private static void createPeriods(SessionFactory sessionFactory, Teacher teacher, TeachUsDate startDate) {
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		
		// Periods starting 1. january 3 years ago
		Period period = new PeriodImpl();
		period.setStatus(Status.FINAL);
		period.setName("Mon/Wed");
		period.setBeginDate(startDate);
		period.setStartTime(startDate.withTime(10, 0, 0, 0));
		period.setEndTime(startDate.withTime(17, 0, 0, 0));
		period.setLocation("Odense");
		period.setPrice(450);
		period.addWeekDay(WeekDay.MONDAY);
		period.addWeekDay(WeekDay.WEDNESDAY);
		period.setTeacher(teacher);
		session.save(period);
		
		period = new PeriodImpl();
		period.setStatus(Status.FINAL);
		period.setName("Tue/Thu");
		period.setBeginDate(startDate);
		period.setStartTime(startDate.withTime(9, 0, 0, 0));
		period.setEndTime(startDate.withTime(17, 0, 0, 0));
		period.setLocation("Copenhagen");
		period.setPrice(450);
		period.addWeekDay(WeekDay.TUESDAY);
		period.addWeekDay(WeekDay.THURSDAY);
		period.setTeacher(teacher);
		session.save(period);
		
		period = new PeriodImpl();
		period.setStatus(Status.FINAL);
		period.setName("Fri");
		period.setBeginDate(startDate);
		period.setStartTime(startDate.withTime(9, 0, 0, 0));
		period.setEndTime(startDate.withTime(16, 0, 0, 0));
		period.setLocation("Aarhus");
		period.setPrice(450);
		period.setIntervalBetweenLessonStart(70);
		period.addWeekDay(WeekDay.FRIDAY);
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
					.replace("\u00FC", "u")
					.replace("\u00E5", "aa")
					.replace("\u00F8", "o");
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

		executeSql(sessionFactory, "TRUNCATE application_configuration");
		executeSql(sessionFactory, "TRUNCATE booking");
		executeSql(sessionFactory, "TRUNCATE period");
		executeSql(sessionFactory, "TRUNCATE teacher_attribute");
		executeSql(sessionFactory, "TRUNCATE message");
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
