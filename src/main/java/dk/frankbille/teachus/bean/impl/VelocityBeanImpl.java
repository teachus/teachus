package dk.frankbille.teachus.bean.impl;

import java.util.Locale;
import java.util.Map;

import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.exception.VelocityException;
import org.springframework.core.io.ClassPathResource;
import org.springframework.ui.velocity.VelocityEngineUtils;

import dk.frankbille.teachus.bean.VelocityBean;

public class VelocityBeanImpl implements VelocityBean {
	private static final String UNDERSCORE = "_";
	private static final String VM = ".vm";

	private static final long serialVersionUID = 1L;

	private VelocityEngine velocityEngine;

	public VelocityBeanImpl(VelocityEngine velocityEngine) {
		this.velocityEngine = velocityEngine;
	}
	
	public String mergeTemplate(String template, Map<String, Object> model) throws VelocityException {
		return VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, template, model);
	}
	
	public String mergeTemplate(String template, Map<String, Object> model, Locale locale) throws VelocityException {
		StringBuilder templateLocation = new StringBuilder(template);
		
		if (locale != null) {
			StringBuilder localeTemplateLocation = new StringBuilder(templateLocation);
			localeTemplateLocation.append(UNDERSCORE);
			localeTemplateLocation.append(locale.toString());
			localeTemplateLocation.append(VM);
			
			ClassPathResource classPathResource = new ClassPathResource(localeTemplateLocation.toString());
			if (classPathResource.exists()) {
				templateLocation = localeTemplateLocation;
			} else {
				templateLocation.append(VM);
			}
		} else {
			templateLocation.append(VM);
		}
		
		return mergeTemplate(templateLocation.toString(), model);
	}
	
}
