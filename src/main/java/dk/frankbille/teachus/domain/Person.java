package dk.frankbille.teachus.domain;

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
	
	String getEmail();
	void setEmail(String email);
	
	Locale getLocale();
	void setLocale(Locale locale);
}
