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
package dk.teachus.frontend;

import java.util.Enumeration;
import java.util.List;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.TimeZone;

import javax.servlet.http.Cookie;

import org.apache.wicket.Request;
import org.apache.wicket.RequestCycle;
import org.apache.wicket.Session;
import org.apache.wicket.protocol.http.WebRequest;
import org.apache.wicket.protocol.http.WebResponse;
import org.apache.wicket.protocol.http.WebSession;
import org.joda.time.DateMidnight;
import org.joda.time.DateTime;

import dk.teachus.backend.dao.PersonDAO;
import dk.teachus.backend.domain.Admin;
import dk.teachus.backend.domain.ApplicationConfiguration;
import dk.teachus.backend.domain.Person;
import dk.teachus.backend.domain.Pupil;
import dk.teachus.backend.domain.TeachUsDate;
import dk.teachus.backend.domain.Teacher;
import dk.teachus.backend.domain.TeacherAttribute;
import dk.teachus.backend.domain.impl.TimeZoneAttribute;
import dk.teachus.frontend.pages.UnAuthenticatedBasePage;
import dk.teachus.utils.ClassUtils;

public class TeachUsSession extends WebSession {
	private static final long serialVersionUID = 1L;
	private static final String BUNDLE_NAME = ClassUtils.getAsResourceBundlePath(TeachUsSession.class, "messages"); //$NON-NLS-1$
	
	protected Person person;
	private Properties resourceBundle;
	
	private TeacherAttributes teacherAttributes = new TeacherAttributes();
	
	public TeachUsSession(Request request) {
		super(request);

		changeLocale(getLocale());
	}

	public boolean isAuthenticated() {
		return getPerson() != null;
	}
	
	public void signIn(String username, String password) {
		PersonDAO personDAO = TeachUsApplication.get().getPersonDAO();

		person = personDAO.authenticatePerson(username, password);

		setLocale();
	}
	
	public void signInWithPrivateKey(String username, String privateKey) {
		PersonDAO personDAO = TeachUsApplication.get().getPersonDAO();
		
		person = personDAO.authenticatePersonWithPrivateKey(username, privateKey);

		setLocale();
	}

	private void setLocale() {
		Person p = getPerson();
		if (p != null) {
			if (p.getLocale() != null) {
				setLocale(p.getLocale());
			} else if (p instanceof Pupil) {
				Pupil pupil = (Pupil) p;
				setLocale(pupil.getTeacher().getLocale());
			}
		}
		
		changeLocale(getLocale());
	}

	/**
	 * Should only be used for an Admin to be able to login as a teacher to see his/hers settings.
	 * 
	 * @param teacher The teacher to authenticate as.
	 */
	public void setAuthenticatedPerson(Teacher teacher) {
		if (getUserLevel() == UserLevel.ADMIN) {
			person = teacher;
		}
		
		// We don't set any locale because the teacher might not have the same as the admin.
	}
	
	public void changeLocale(Locale locale) {
		setLocale(locale);
		resourceBundle = createResourceBundle(locale);
	}
	
	public Properties createResourceBundle(Locale locale) {
		Properties properties = new Properties();
		
		ResourceBundle bundle = ResourceBundle.getBundle(BUNDLE_NAME, locale);
		Enumeration<String> keys = bundle.getKeys();
		while(keys.hasMoreElements()) {
			String key = keys.nextElement();
			properties.setProperty(key, bundle.getString(key));
		}
		
		return properties;
	}

	public void signOut() {
		WebRequest webRequest = (WebRequest) RequestCycle.get().getRequest();
		WebResponse webResponse = (WebResponse) RequestCycle.get().getResponse();
		Cookie[] cookies = webRequest.getCookies();
		
		for (Cookie cookie : cookies) {
			if (UnAuthenticatedBasePage.USERNAME_PATH.equals(cookie.getName())
					|| UnAuthenticatedBasePage.PASSWORD_PATH.equals(cookie.getName())
					|| UnAuthenticatedBasePage.REMEMBER_PATH.equals(cookie.getName())) {
				webResponse.clearCookie(cookie);
			}
		}
		
		person = null;
		invalidate();
	}
	
	public Person getPerson() {
		return person;
	}
	
	public Teacher getTeacher() {
		return (Teacher) getPerson();
	}
	
	public List<TeacherAttribute> getTeacherAttributes() {
		Person p = getPerson();
		return teacherAttributes.getTeacherAttributes(p);
	}
	
	public <A extends TeacherAttribute> A getTeacherAttribute(Class<A> attributeClass) {
		return teacherAttributes.getTeacherAttribute(attributeClass, getPerson());
	}

	public <A extends TeacherAttribute> A getTeacherAttribute(Class<A> attributeClass, Teacher teacher) {
		return teacherAttributes.getTeacherAttribute(attributeClass, teacher);
	}
	
	public void saveNewTeacherAttribute(TeacherAttribute teacherAttribute) {
		if (teacherAttribute != null && teacherAttribute.getId() == null) {
			TeachUsApplication.get().getPersonDAO().saveAttribute(teacherAttribute);
			
			teacherAttributes.refreshAttributes(teacherAttribute.getTeacher());
		}
	}
	
	public UserLevel getUserLevel() {
		UserLevel userLevel = null;
		
		if (person != null) {
			if (person instanceof Admin) {
				userLevel = UserLevel.ADMIN;
			} else if (person instanceof Teacher) {
				userLevel = UserLevel.TEACHER;
			} else if (person instanceof Pupil) {
				userLevel = UserLevel.PUPIL;
			} else {
				throw new IllegalStateException("Unsupported person in user: "+person.getClass().getName()); //$NON-NLS-1$
			}
		}
		
		return userLevel;
	}

	public String getString(String key) {
		try {
			return resourceBundle.getProperty(key);
		} catch (MissingResourceException e) {
			return '!' + key + '!';
		}
	}
	
	public TeachUsDate createNewDate() {
		return createNewDate(new DateTime());
	}

	public TeachUsDate createNewDate(DateMidnight dateMidnight) {
		return createNewDate(dateMidnight.toDateTime());
	}

	public TeachUsDate createNewDate(DateTime dateTime) {
		return createNewDate(dateTime, getPerson());
	}
	
	public TeachUsDate createNewDate(DateMidnight dateMidnight, Person person) {
		return createNewDate(dateMidnight.toDateTime(), person);
	}
	
	public TeachUsDate createNewDate(DateTime dateTime, Person person) {
		TeachUsDate date = new TeachUsDate(dateTime);
		
		TimeZone timeZone = null;
		if (person != null) {
			Teacher teacher = null;
			if (person instanceof Pupil) {
				Pupil pupil = (Pupil) person;
				teacher = pupil.getTeacher();
			} else if (person instanceof Teacher) {
				teacher = (Teacher) person;
			}
			
			if (teacher != null) {
				TimeZoneAttribute timeZoneAttribute = getTeacherAttribute(TimeZoneAttribute.class, teacher);
				
				if (timeZoneAttribute != null) {
					timeZone = timeZoneAttribute.getTimeZone();
				}
			}
		}
		
		if (timeZone == null) {
			ApplicationConfiguration configuration = TeachUsApplication.get().getConfiguration();
			String tzString = configuration.getConfiguration(ApplicationConfiguration.DEFAULT_TIMEZONE);
			
			if (tzString != null) {
				timeZone = TimeZone.getTimeZone(tzString);
			}
		}
		
		if (timeZone == null) {
			timeZone = TimeZone.getDefault();
		}
		
		date.setTimeZone(timeZone);
		
		return date;
	}

	public static TeachUsSession get() {
		return (TeachUsSession) Session.get();
	}
	
}
