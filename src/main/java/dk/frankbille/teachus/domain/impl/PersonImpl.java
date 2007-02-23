package dk.frankbille.teachus.domain.impl;

import java.util.Locale;

import dk.frankbille.teachus.domain.Person;

public abstract class PersonImpl extends AbstractHibernateObject implements Person {
	private String name;

	private String username;

	private String password;

	private String email;

	private String phoneNumber;

	private Locale locale;
	
	private boolean active = true;

	public String getEmail() {
		return email;
	}

	public Locale getLocale() {
		return locale;
	}

	public String getName() {
		return name;
	}

	public String getPassword() {
		return password;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public String getUsername() {
		return username;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setLocale(Locale locale) {
		this.locale = locale;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public void setUsername(String username) {
		this.username = username;
	}
}
