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
package dk.teachus.backend.test;

import java.io.Serializable;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import javax.naming.NamingException;
import javax.sql.DataSource;

import org.hibernate.SessionFactory;
import org.hsqldb.jdbcDriver;
import org.joda.time.DateTime;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.mock.jndi.SimpleNamingContextBuilder;
import org.springframework.test.annotation.AbstractAnnotationAwareTransactionalTests;

import com.mysql.jdbc.jdbc2.optional.MysqlConnectionPoolDataSource;

import dk.teachus.backend.dao.ApplicationDAO;
import dk.teachus.backend.dao.BookingDAO;
import dk.teachus.backend.dao.MessageDAO;
import dk.teachus.backend.dao.PeriodDAO;
import dk.teachus.backend.dao.PersonDAO;
import dk.teachus.backend.dao.StatisticsDAO;
import dk.teachus.backend.database.StaticDataImport;
import dk.teachus.backend.domain.ApplicationConfiguration;
import dk.teachus.backend.domain.Period;
import dk.teachus.backend.domain.Pupil;
import dk.teachus.backend.domain.PupilBooking;
import dk.teachus.backend.domain.Teacher;
import dk.teachus.backend.domain.TeacherBooking;
import dk.teachus.backend.domain.impl.ApplicationConfigurationImpl;
import dk.teachus.backend.domain.impl.PupilImpl;
import dk.teachus.backend.domain.impl.TeacherImpl;

public abstract class SpringTestCase extends AbstractAnnotationAwareTransactionalTests implements Serializable {
	private static final long serialVersionUID = 1L;
	
	protected static final String TABLE_APPLICATION_CONFIGURATION = "application_configuration";
	protected static final String TABLE_BOOKING = "booking";
	protected static final String TABLE_PERIOD = "period";
	protected static final String TABLE_PERSON = "person";
	protected static final String TABLE_TEACHER_ATTRIBUTE = "teacher_attribute";
	protected static final String TABLE_MESSAGE = "message";
	private static boolean useMysql = false;
	
	static {
		final String mysql = System.getProperty("teachus.test.use.mysql");
		if (mysql != null && mysql.length() > 0) {
			SpringTestCase.useMysql = true;
		}
	}
	
	public SpringTestCase() {
		setDefaultRollback(false);
	}
	
	protected Long createPupilBooking(final long periodId, final long pupilId, final DateTime dateTime, final DateTime createDate) {
		final BookingDAO bookingDAO = getBookingDAO();
		final PersonDAO personDAO = getPersonDAO();
		final PeriodDAO periodDAO = getPeriodDAO();
		
		final Period period = periodDAO.get(periodId);
		endTransaction();
		
		final Pupil pupil = (Pupil) personDAO.getPerson(pupilId);
		endTransaction();
		
		final PupilBooking pupilBooking = bookingDAO.createPupilBookingObject();
		pupilBooking.setPeriod(period);
		pupilBooking.setPupil(pupil);
		pupilBooking.setTeacher(pupil.getTeacher());
		pupilBooking.setPaid(false);
		pupilBooking.setNotificationSent(false);
		pupilBooking.setCreateDate(createDate);
		pupilBooking.setDate(dateTime);
		
		bookingDAO.book(pupilBooking);
		endTransaction();
		
		return pupilBooking.getId();
	}
	
	protected Long createTeacherBooking(final long periodId, final long teacherId, final DateTime date) {
		return createTeacherBooking(periodId, teacherId, date, new DateTime());
	}
	
	protected Long createTeacherBooking(final long periodId, final long teacherId, final DateTime date, final DateTime createDate) {
		final BookingDAO bookingDAO = getBookingDAO();
		final PersonDAO personDAO = getPersonDAO();
		final PeriodDAO periodDAO = getPeriodDAO();
		
		final Period period = periodDAO.get(periodId);
		endTransaction();
		
		final Teacher teacher = (Teacher) personDAO.getPerson(teacherId);
		endTransaction();
		
		final TeacherBooking teacherBooking = bookingDAO.createTeacherBookingObject();
		teacherBooking.setCreateDate(createDate);
		teacherBooking.setDate(date);
		teacherBooking.setPeriod(period);
		teacherBooking.setTeacher(teacher);
		
		bookingDAO.book(teacherBooking);
		endTransaction();
		
		return teacherBooking.getId();
	}
	
	@Override
	protected String[] getConfigLocations() {
		final List<String> configLocations = new ArrayList<String>();
		
		configLocations.add("/dk/teachus/backend/applicationContext.xml");
		configLocations.add("/dk/teachus/backend/dao/hibernate/applicationContext-hibernate.xml");
		configLocations.add("/dk/teachus/frontend/applicationContext-frontend.xml");
		
		DataSource dataSource = null;
		if (SpringTestCase.useMysql) {
			final MysqlConnectionPoolDataSource ds = new MysqlConnectionPoolDataSource();
			ds.setUrl("jdbc:mysql://localhost/teachus_test");
			ds.setUser("teachus_build");
			ds.setPassword("teachus_build");
			dataSource = ds;
			configLocations.add("/dk/teachus/backend/test/applicationContext-test-mysql.xml");
		} else {
			dataSource = new SimpleDriverDataSource(new jdbcDriver(), "jdbc:hsqldb:mem:teachus", "sa", "");
			configLocations.add("/dk/teachus/backend/test/applicationContext-test-hsqldb.xml");
		}
		
		final SimpleNamingContextBuilder contextBuilder = new SimpleNamingContextBuilder();
		contextBuilder.bind("java:comp/env/jdbc/teachus", dataSource);
		try {
			contextBuilder.activate();
		} catch (final IllegalStateException e) {
			throw new RuntimeException(e);
		} catch (final NamingException e) {
			throw new RuntimeException(e);
		}
		
		addConfigLocations(configLocations);
		
		return configLocations.toArray(new String[configLocations.size()]);
	}
	
	protected void addConfigLocations(final List<String> configLocations) {
	}
	
	@Override
	protected void onSetUpBeforeTransaction() throws Exception {
		final Connection connection = getSessionFactory().openSession().connection();
		new StaticDataImport(connection);
		
		final DataSource dataSource = (DataSource) applicationContext.getBean("dataSource");
		setDataSource(dataSource);
	}
	
	public BookingDAO getBookingDAO() {
		return (BookingDAO) applicationContext.getBean("bookingDao");
	}
	
	public PeriodDAO getPeriodDAO() {
		return (PeriodDAO) applicationContext.getBean("periodDao");
	}
	
	public PersonDAO getPersonDAO() {
		return (PersonDAO) applicationContext.getBean("personDao");
	}
	
	public StatisticsDAO getStatisticsDAO() {
		return (StatisticsDAO) applicationContext.getBean("statisticsDao");
	}
	
	public ApplicationDAO getApplicationDAO() {
		return (ApplicationDAO) applicationContext.getBean("applicationDao");
	}
	
	public MessageDAO getMessageDAO() {
		return (MessageDAO) applicationContext.getBean("messageDao");
	}
	
	public SessionFactory getSessionFactory() {
		return (SessionFactory) applicationContext.getBean("sessionFactory");
	}
	
	protected Pupil createPupil(final Teacher teacher, final int pupilNumber) {
		final Pupil pupil = new PupilImpl();
		pupil.setName("Test pupil " + pupilNumber);
		pupil.setActive(true);
		pupil.setEmail("pupil" + pupilNumber + "@teachus.dk");
		pupil.setUsername("pupil" + pupilNumber);
		pupil.setTeacher(teacher);
		getPersonDAO().save(pupil);
		endTransaction();
		return pupil;
	}
	
	protected Teacher createTeacher() {
		final Teacher teacher = new TeacherImpl();
		teacher.setName("Test name");
		teacher.setActive(true);
		teacher.setEmail("test@teachus.dk");
		teacher.setUsername("test");
		getPersonDAO().save(teacher);
		endTransaction();
		return teacher;
	}
	
	protected Object loadObject(final Class<?> objectClass, final Serializable objectId) {
		Object object = null;
		
		final org.hibernate.Session session = getSessionFactory().openSession();
		session.beginTransaction();
		
		object = session.get(objectClass, objectId);
		
		session.getTransaction().commit();
		session.close();
		
		return object;
	}
	
	protected Teacher inactivateTeacher() {
		final Teacher teacher = (Teacher) getPersonDAO().getPerson(2L);
		endTransaction();
		
		teacher.setActive(false);
		
		getPersonDAO().save(teacher);
		endTransaction();
		return teacher;
	}
	
	protected Teacher getTeacher() {
		final Teacher teacher = (Teacher) getPersonDAO().getPerson(2L);
		endTransaction();
		return teacher;
	}
	
	protected ApplicationConfiguration createDummyConfiguration() {
		final ApplicationConfigurationImpl conf = new ApplicationConfigurationImpl(null);
		
		conf.setConfiguration(ApplicationConfiguration.SERVER_URL, "http://localhost:8080/");
		
		return conf;
	}
	
}
