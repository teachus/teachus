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
package dk.teachus.backend.testdatagenerator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class DynamicDataImport {
	private static final Log log = LogFactory.getLog(DynamicDataImport.class);
	
//	private SessionFactory sessionFactory;
	
	public void doImport() {
//		TimeZone timeZone = TimeZone.getDefault();
//
//		DateMidnight startDate = new DateMidnight().minusYears(3).withMonthOfYear(1).withDayOfMonth(10);
//		DateMidnight endDate = new DateMidnight().withMonthOfYear(12).withDayOfMonth(31);
//
//		deleteExistingData();
//		
//		createVersion();
//		
//		createDefaultTimezone(timeZone);
//		
//		createAdmin();
//		
//		int numberOfTeachers = 1;
//		
//		for (int i = 1; i <= numberOfTeachers; i++) {
//			log.info("Creating teacher number " + i + " of " + numberOfTeachers);
//			
//			Teacher teacher = createTeacher(i);
//			
//			createTeacherAttribute(teacher);
//			
//			createPupils(teacher, i);
//			
//			createPeriods(teacher, startDate);
//			
//			createBookings(teacher, startDate, endDate);
//			
//			verify(teacher);
//		}
//		
//		log.info("Done");
	}
	
//	public void setSessionFactory(SessionFactory sessionFactory) {
//		this.sessionFactory = sessionFactory;
//	}
//	
//	public SessionFactory getSessionFactory() {
//		return sessionFactory;
//	}
//	
//	private void createVersion() {
//		// Parse the pom file to get the version
//		File file = new File("pom.xml");
//		
//		if (file.exists()) {
//			try {
//				String pomContent = FileUtils.readFileToString(file, "UTF-8");
//				
//				Pattern pattern = Pattern.compile(".*?\\<version\\>([^\\<]+)\\<\\/version>.*", Pattern.MULTILINE | Pattern.DOTALL);
//				Matcher matcher = pattern.matcher(pomContent);
//				if (matcher.matches()) {
//					String version = matcher.group(1);
//					version = version.replace("-SNAPSHOT", "");
//					
//					Session session = sessionFactory.openSession();
//					session.beginTransaction();
//					
//					ApplicationConfigurationEntry entry = new ApplicationConfigurationEntry("VERSION", version);
//					session.save(entry);
//					
//					session.getTransaction().commit();
//					session.close();
//				}
//			} catch (Exception e) {
//				throw new RuntimeException(e);
//			}
//		}
//	}
//	
//	private void createDefaultTimezone(TimeZone timeZone) {
//		Session session = sessionFactory.openSession();
//		session.beginTransaction();
//		
//		ApplicationConfigurationEntry entry = new ApplicationConfigurationEntry(ApplicationConfiguration.DEFAULT_TIMEZONE, timeZone.getID());
//		session.save(entry);
//		
//		session.getTransaction().commit();
//		session.close();
//	}
//
//	private void createTeacherAttribute(Teacher teacher) {
//		Session session = sessionFactory.openSession();
//		session.beginTransaction();
//		
//		TeacherAttribute attribute = new WelcomeIntroductionTeacherAttribute();
//		attribute.setTeacher(teacher);
//		attribute.setValue("A welcome introduction");
//		session.save(attribute);
//		
//		TimeZoneAttribute tzAttribute = new TimeZoneAttribute();
//		tzAttribute.setTeacher(teacher);
//		tzAttribute.setTimeZone(TimeZone.getDefault());
//		session.save(tzAttribute);
//		
//		session.getTransaction().commit();
//		session.close();
//	}
//	
//	private void verify(Teacher teacher) {
//		Session session = sessionFactory.openSession();
//		session.beginTransaction();
//
//		SQLQuery sqlQuery = session.createSQLQuery("SELECT date, COUNT(id) AS c FROM booking WHERE teacher_id = "+teacher.getId()+" GROUP BY date HAVING c > 1");
//		List<?> result = sqlQuery.list();
//		
//		for (Object object : result) {
//			Object[] objects = (Object[]) object;
//			log.error(objects[0] + ": " + objects[1]);
//		}
//		
//		session.getTransaction().commit();
//		session.close();		
//	}
//	
//	private void createBookings(Teacher teacher, DateMidnight startDate, DateMidnight endDate) {
//		Session session = sessionFactory.openSession();
//		session.beginTransaction();
//		
//		Set<DateTime> bookedDates = new HashSet<DateTime>();
//		
//		List<Pupil> pupils = getPupils(teacher, session);
//		List<Period> periods = getPeriods(teacher, session);
//		
//		DateTime now = new DateTime();
//		
//		for (Pupil pupil : pupils) {
//			DateMidnight date = startDate;
//			while(date.isBefore(endDate)) {
//				// First see if the pupil should have a booking here at all
//				long shouldBook = Math.round(Math.random());
//				if (shouldBook == 1) {
//					// Then find out which period to book in
//					int periodIndex = (int) (Math.random()*periods.size());
//					Period period = periods.get(periodIndex);
//					log.debug("Booking lesson for period: "+period.getName());
//					
//					// Then find out which day in the period to book in
//					int weekDayIndex = (int) (Math.random()*period.getWeekDays().size());
//					WeekDay weekDay = period.getWeekDays().get(weekDayIndex);
//					DateMidnight weekDayDate = date.withDayOfWeek(weekDay.getYodaWeekDay());
//					
//					// Now find out which time of day to use
//					// We do that by listing the possible booking time entries. A lesson doesn't have to start at a whole number.
//					List<DateTime> avaiableLessonsStart = new ArrayList<DateTime>();
//					DateTime bookTime = period.getStartTime().toDateTime(weekDayDate);
//					DateTime et = period.getEndTime().toDateTime(weekDayDate);
//					while(bookTime.isBefore(et)) {
//						if (period.mayBook(bookTime)) {
//							avaiableLessonsStart.add(bookTime);
//						}
//						
//						bookTime = bookTime.plusMinutes(period.getIntervalBetweenLessonStart());
//					}
//					
//					int selectedIndex = (int) Math.round(Math.random()*(avaiableLessonsStart.size()-1));
//					log.debug("Selected lesson number "+selectedIndex+" out of "+avaiableLessonsStart.size());
//					bookTime = avaiableLessonsStart.get(selectedIndex);
//										
//					if (bookedDates.contains(bookTime) == false) {
//						bookedDates.add(bookTime);
//						
//						// Create booking
//						PupilBooking booking = new PupilBookingImpl();
//						booking.setCreateDate(new DateTime());
//						booking.setDate(bookTime);
//						booking.setPeriod(period);
//						booking.setTeacher(teacher);
//						booking.setPupil(pupil);
//						booking.setNotificationSent(true);
//						booking.setPupilNotificationSent(true);
//						
//						// Set the paid based on the booking time is in the past
//						if (now.isAfter(bookTime)) {
//							// If it's within a month, then give it a 40% change of not have been paid
//							if (now.minusMonths(1).isBefore(bookTime)) {
//								int pct = (int) (Math.random()*100.0);
//								if (pct >= 40) {
//									booking.setPaid(true);
//								}
//							} else {
//								booking.setPaid(true);
//							}
//						}
//						
//						// Book
//						session.save(booking);
//					}
//				}
//				
//				date = date.plusWeeks(1);
//			}
//		}
//		
//		session.getTransaction().commit();
//		session.close();
//	}
//
//	@SuppressWarnings("unchecked")
//	private List<Pupil> getPupils(Teacher teacher, Session session) {
//		Criteria criteria = session.createCriteria(PupilImpl.class);
//		criteria.add(Restrictions.eq("teacher", teacher));
//		List<Pupil> pupils = criteria.list();
//		return pupils;
//	}
//
//	@SuppressWarnings("unchecked")
//	private List<Period> getPeriods(Teacher teacher, Session session) {
//		Criteria criteria = session.createCriteria(PeriodImpl.class);
//		criteria.add(Restrictions.eq("teacher", teacher));
//		List<Period> periods = criteria.list();
//		return periods;
//	}
//	
//	private void createPeriods(Teacher teacher, DateMidnight startDate) {
//		Session session = sessionFactory.openSession();
//		session.beginTransaction();
//		
//		// Periods starting 1. january 3 years ago
//		Period period = new PeriodImpl();
//		period.setStatus(Status.FINAL);
//		period.setName("Mon/Wed");
//		period.setBeginDate(startDate);
//		period.setStartTime(new LocalTime(10, 0, 0, 0));
//		period.setEndTime(new LocalTime(17, 0, 0, 0));
//		period.setLocation("Odense");
//		period.setPrice(450);
//		period.addWeekDay(WeekDay.MONDAY);
//		period.addWeekDay(WeekDay.WEDNESDAY);
//		period.setTeacher(teacher);
//		session.save(period);
//		
//		period = new PeriodImpl();
//		period.setStatus(Status.FINAL);
//		period.setName("Tue/Thu");
//		period.setBeginDate(startDate);
//		period.setStartTime(new LocalTime(9, 0, 0, 0));
//		period.setEndTime(new LocalTime(17, 0, 0, 0));
//		period.setLocation("Copenhagen");
//		period.setPrice(450);
//		period.addWeekDay(WeekDay.TUESDAY);
//		period.addWeekDay(WeekDay.THURSDAY);
//		period.setTeacher(teacher);
//		session.save(period);
//		
//		period = new PeriodImpl();
//		period.setStatus(Status.FINAL);
//		period.setName("Fri");
//		period.setBeginDate(startDate);
//		period.setStartTime(new LocalTime(9, 0, 0, 0));
//		period.setEndTime(new LocalTime(16, 0, 0, 0));
//		period.setLocation("Aarhus");
//		period.setPrice(450);
//		period.setIntervalBetweenLessonStart(70);
//		period.addWeekDay(WeekDay.FRIDAY);
//		period.setTeacher(teacher);
//		session.save(period);
//		
//		session.getTransaction().commit();
//		session.close();
//	}
//	
//	private Admin createAdmin() {
//		Session session = sessionFactory.openSession();
//		session.beginTransaction();
//		
//		Admin admin = new AdminImpl();
//		admin.setName("Admin");
//		admin.setUsername("admin");
//		admin.setPassword("admin");
//		admin.setEmail("admin@teachus.dk");
//		admin.setLocale(new Locale("en"));
//		session.save(admin);
//		
//		session.getTransaction().commit();
//		session.close();
//		
//		return admin;
//	}
//
//	private void createPupils(Teacher teacher, int number) {
//		Session session = sessionFactory.openSession();
//		session.beginTransaction();
//		
//		String postfixUnix = "";
//		String postfixName = "";
//		if (number > 1) {
//			postfixUnix = "_" + number;
//			postfixName = " " + number;
//		}
//		
//		for (String firstName : Names.firstNames) {
//			int lastNameIndex = (int) (Math.random()*Names.lastNames.size());
//			String lastName = Names.lastNames.get(lastNameIndex);
//			String name = firstName + " " + lastName;
//			String unixName = name
//					.toLowerCase()
//					.replace(" ", "_")
//					.replace("\u00FC", "u")
//					.replace("\u00E5", "aa")
//					.replace("\u00F8", "o");
//			unixName += postfixUnix;
//			
//			Pupil pupil = new PupilImpl();
//			pupil.setName(name+postfixName);
//			pupil.setEmail(unixName + "@teachus.dk");
//			pupil.setUsername(unixName);
//			pupil.setPassword(unixName);
//			pupil.setTeacher(teacher);
//			session.save(pupil);
//			log.debug("Created pupil: "+unixName);
//		}
//		
//		session.getTransaction().commit();
//		session.close();
//	}
//
//	private Teacher createTeacher(int number) {
//		Session session = sessionFactory.openSession();
//		session.beginTransaction();
//		
//		String postfixUnix = "";
//		String postfixName = "";
//		if (number > 1) {
//			postfixUnix = "_" + number;
//			postfixName = " " + number;
//		}
//		
//		Teacher teacher = new TeacherImpl();
//		teacher.setName("Leif Borilde"+postfixName);
//		teacher.setUsername("leif"+postfixUnix);
//		teacher.setPassword("leif"+postfixUnix);
//		teacher.setEmail("leif"+postfixUnix+"@teachus.dk");
//		teacher.setLocale(new Locale("en", "US", "singers"));
//		teacher.setTheme(Theme.GREEN);
//		teacher.setCurrency("kr");
//		session.save(teacher);
//		
//		CalendarNarrowTimesTeacherAttribute calAtt = new CalendarNarrowTimesTeacherAttribute();
//		calAtt.setBooleanValue(true);
//		calAtt.setTeacher(teacher);
//		session.save(calAtt);
//		
//		session.getTransaction().commit();
//		session.close();
//		
//		return teacher;
//	}
//
//	private void deleteExistingData() {
//		executeSql("UPDATE person SET teacher_id = NULL");
//
//		executeSql("DELETE FROM application_configuration");
//		executeSql("DELETE FROM booking");
//		executeSql("ALTER TABLE booking AUTO_INCREMENT = 1");
//		executeSql("DELETE FROM period");
//		executeSql("ALTER TABLE period AUTO_INCREMENT = 1");
//		executeSql("DELETE FROM teacher_attribute");
//		executeSql("ALTER TABLE teacher_attribute AUTO_INCREMENT = 1");
//		executeSql("DELETE FROM message");
//		executeSql("ALTER TABLE message AUTO_INCREMENT = 1");
//		executeSql("DELETE FROM person");
//		executeSql("ALTER TABLE person AUTO_INCREMENT = 1");
//	}
//	
//	private void executeSql(String sql) {
//		Session session = sessionFactory.openSession();
//		session.beginTransaction();
//		
//		SQLQuery sqlQuery = session.createSQLQuery(sql);
//		sqlQuery.executeUpdate();
//		
//		session.getTransaction().commit();
//		session.close();
//	}
	
}
