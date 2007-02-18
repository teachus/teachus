package dk.frankbille.teachus.frontend;

import java.util.Enumeration;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.Properties;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

import wicket.Request;
import wicket.protocol.http.WebApplication;
import wicket.protocol.http.WebSession;
import dk.frankbille.teachus.dao.PersonDAO;
import dk.frankbille.teachus.domain.Admin;
import dk.frankbille.teachus.domain.Person;
import dk.frankbille.teachus.domain.Pupil;
import dk.frankbille.teachus.domain.Teacher;

public class TeachUsSession extends WebSession {
	private static final long serialVersionUID = 1L;
	private static final String BUNDLE_NAME = "dk.frankbille.teachus.frontend.messages"; //$NON-NLS-1$
	
	private Person person;
	private Properties resourceBundle;
	
	public TeachUsSession(WebApplication application, Request request) {
		super(application, request);

		changeLocale(getLocale());
	}

	public boolean isAuthenticated() {
		return person != null;
	}
	
	public void signIn(String username, String password) {
		PersonDAO personDAO = TeachUsApplication.get().getPersonDAO();

		person = personDAO.authenticatePerson(username, password);

		if (person != null) {
			if (person.getLocale() != null) {
				setLocale(person.getLocale());
			} else if (person instanceof Pupil) {
				Pupil pupil = (Pupil) person;
				setLocale(pupil.getTeacher().getLocale());
			}
		}
		
		changeLocale(getLocale());
	}
	
	public void changeLocale(Locale locale) {
		setLocale(locale);
		resourceBundle = createResourceBundle(locale);
	}
	
	public Properties createResourceBundle(Locale locale) {
		Properties properties = new Properties();
		
		ResourceBundle bundle = PropertyResourceBundle.getBundle(BUNDLE_NAME, locale);
		Enumeration<String> keys = bundle.getKeys();
		while(keys.hasMoreElements()) {
			String key = keys.nextElement();
			properties.setProperty(key, bundle.getString(key));
		}
		
		return properties;
	}

	public void signOut() {
		person = null;
		invalidate();
	}
	
	public Person getPerson() {
		return person;
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

	public static TeachUsSession get() {
		return (TeachUsSession) WebSession.get();
	}
	
}
