package dk.teachus.domain;

import java.io.Serializable;
import java.util.Locale;

public interface Person extends Serializable {
	Long getId();
	
	String getName();
	void setName(String name);
	
	String getUsername();
	void setUsername(String username);
	
	String getPassword();
	void setPassword(String password);
	
	String getCryptedPassword();
	void setCryptedPassword(String password);
	
	String getEmail();
	void setEmail(String email);
	
	String getPhoneNumber();
	void setPhoneNumber(String phoneNumber);
	
	Locale getLocale();
	void setLocale(Locale locale);
	
	Theme getTheme();
	void setTheme(Theme theme);
	
	boolean isActive();
	void setActive(boolean active);
}
