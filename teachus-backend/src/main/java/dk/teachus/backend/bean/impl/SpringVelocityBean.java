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
package dk.teachus.backend.bean.impl;

import java.util.Locale;
import java.util.Map;

import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.exception.VelocityException;
import org.springframework.core.io.ClassPathResource;
import org.springframework.ui.velocity.VelocityEngineUtils;

import dk.teachus.backend.bean.VelocityBean;

public class SpringVelocityBean implements VelocityBean {
	private static final String UNDERSCORE = "_";
	private static final String VM = ".vm";

	private static final long serialVersionUID = 1L;

	private VelocityEngine velocityEngine;

	public SpringVelocityBean(VelocityEngine velocityEngine) {
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
