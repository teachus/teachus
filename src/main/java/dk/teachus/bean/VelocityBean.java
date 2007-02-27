package dk.teachus.bean;

import java.io.Serializable;
import java.util.Locale;
import java.util.Map;

import org.apache.velocity.exception.VelocityException;

public interface VelocityBean extends Serializable {

	String mergeTemplate(String template, Map<String, Object> model) throws VelocityException;
	
	String mergeTemplate(String template, Map<String, Object> model, Locale locale) throws VelocityException;
	
}
