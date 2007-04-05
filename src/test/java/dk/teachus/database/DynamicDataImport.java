package dk.teachus.database;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.hibernate.Criteria;
import org.hibernate.SQLQuery;
import org.hibernate.SessionFactory;
import org.hibernate.classic.Session;
import org.hibernate.criterion.Restrictions;
import org.joda.time.DateMidnight;
import org.joda.time.DateTime;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import dk.teachus.domain.Admin;
import dk.teachus.domain.Currency;
import dk.teachus.domain.Period;
import dk.teachus.domain.Pupil;
import dk.teachus.domain.PupilBooking;
import dk.teachus.domain.Teacher;
import dk.teachus.domain.Theme;
import dk.teachus.domain.impl.AdminImpl;
import dk.teachus.domain.impl.CurrencyImpl;
import dk.teachus.domain.impl.PeriodImpl;
import dk.teachus.domain.impl.PupilBookingImpl;
import dk.teachus.domain.impl.PupilImpl;
import dk.teachus.domain.impl.TeacherImpl;
import dk.teachus.domain.impl.PeriodImpl.WeekDay;

public class DynamicDataImport {
	
	private static final List<String> firstNames = new ArrayList<String>();
	private static final List<String> lastNames = new ArrayList<String>();
	
	static {
		firstNames.add("Anne");
		firstNames.add("Bent");
		firstNames.add("Christina");
		firstNames.add("Dennis");
		firstNames.add("Eva");
		firstNames.add("Frederik");
		firstNames.add("Grethe");
		firstNames.add("Hans");
		firstNames.add("Inge");
		firstNames.add("Jørgen");
		firstNames.add("Karen");
		firstNames.add("Lars");
		firstNames.add("Maria");
		firstNames.add("Niels");
		firstNames.add("Olga");
		firstNames.add("Per");
		firstNames.add("Rikke");
		firstNames.add("Søren");
		firstNames.add("Trine");
		firstNames.add("Ulrik");
		firstNames.add("Vera");
		firstNames.add("Åge");
		
		lastNames.add("Pedersen");
		lastNames.add("Jørgensen");
		lastNames.add("Jensen");
		lastNames.add("Nielsen");
		lastNames.add("Funk");
		lastNames.add("Pihl");
		lastNames.add("Hansen");
		lastNames.add("Sünk");
		lastNames.add("Petersen");
		lastNames.add("Frimand");
		lastNames.add("From");
		lastNames.add("Lund");
		lastNames.add("Andersen");
		lastNames.add("Mogensen");
		lastNames.add("Larsen");
		lastNames.add("Finnsen");
		lastNames.add("Noller");
		lastNames.add("Holm");
		lastNames.add("Jürgensen");
		
	}

	public static void main(String[] args) {
		ApplicationContext context = new ClassPathXmlApplicationContext(new String[] {
				"/dk/teachus/frontend/applicationContext.xml",
				"/dk/teachus/database/applicationContext-dynamicDataImport.xml", });

		SessionFactory sessionFactory = (SessionFactory) context.getBean("sessionFactory");

		DateMidnight startDate = new DateMidnight().minusYears(3).withMonthOfYear(1).withDayOfMonth(10);
		DateMidnight endDate = new DateMidnight().plusYears(1).withMonthOfYear(12).withDayOfMonth(31);

		deleteExistingData(sessionFactory);
		
		Teacher teacher = createPeople(sessionFactory);
		
		createPeriods(sessionFactory, teacher, startDate);
		
		createBookings(sessionFactory, teacher, startDate, endDate);
		
		verify(sessionFactory);
		
		System.exit(0);
	}
	
	private static void verify(SessionFactory sessionFactory) {
		Session session = sessionFactory.openSession();
		session.beginTransaction();

		SQLQuery sqlQuery = session.createSQLQuery("SELECT date, COUNT(id) AS c FROM booking GROUP BY date HAVING c > 1");
		List result = sqlQuery.list();
		
		for (Object object : result) {
			Object[] objects = (Object[]) object;
			System.out.println(objects[0] + ": " + objects[1]);
		}
		
		System.out.println(result.size());
		
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
		
		// Create a danish kroner currency
		Currency dkr = new CurrencyImpl();
		dkr.setBase(true);
		dkr.setExchangeRate(1);
		dkr.setLabel("kr");
		dkr.setTeacher(teacher);
		session.save(dkr);
		
		// Periods starting 1. january 3 years ago
		Period period = new PeriodImpl();
		period.setName("Mon/Wed/Fri");
		period.setBeginDate(startDate.toDate());
		period.setStartTime(startDate.toDateTime().withTime(10, 0, 0, 0).toDate());
		period.setEndTime(startDate.toDateTime().withTime(17, 0, 0, 0).toDate());
		period.setLocation("Odense");
		period.setPrice(450);
		period.setCurrency(dkr);
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
		period.setCurrency(dkr);
		period.addWeekDay(WeekDay.TUESDAY);
		period.addWeekDay(WeekDay.THURSDAY);
		period.setTeacher(teacher);
		session.save(period);
		
		session.getTransaction().commit();
		session.close();
	}
	
	private static Teacher createPeople(SessionFactory sessionFactory) {
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		
		// Create admin
		Admin admin = new AdminImpl();
		admin.setName("Admin");
		admin.setUsername("admin");
		admin.setPassword("admin");
		admin.setEmail("admin@teachus.dk");
		admin.setLocale(new Locale("da"));
		session.save(admin);
		
		// Create teacher
		Teacher teacher = new TeacherImpl();
		teacher.setName("Leif Borilde");
		teacher.setUsername("leif");
		teacher.setPassword("leif");
		teacher.setEmail("leif@teachus.dk");
		teacher.setLocale(new Locale("da", "DK", "singers"));
		teacher.setTheme(Theme.GREEN);
		session.save(teacher);
		
		// Create pupils
		for (String firstName : firstNames) {
			int lastNameIndex = (int) (Math.random()*lastNames.size());
			String lastName = lastNames.get(lastNameIndex);
			String name = firstName + " " + lastName;
			String unixName = name
					.toLowerCase()
					.replace(" ", "_")
					.replace("ü", "u")
					.replace("å", "aa")
					.replace("ø", "o");
			
			Pupil pupil = new PupilImpl();
			pupil.setName(name);
			pupil.setEmail(unixName + "@teachus.dk");
			pupil.setUsername(unixName);
			pupil.setPassword(unixName);
			pupil.setTeacher(teacher);
			session.save(pupil);
		}
		
		session.getTransaction().commit();
		session.close();
		
		return teacher;
	}

	private static void deleteExistingData(SessionFactory sessionFactory) {
		executeSql(sessionFactory, "UPDATE person SET teacher_id = NULL");
		
		executeSql(sessionFactory, "TRUNCATE booking");
		executeSql(sessionFactory, "TRUNCATE period");
		executeSql(sessionFactory, "TRUNCATE teacher_attribute");
		executeSql(sessionFactory, "TRUNCATE currency");
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
