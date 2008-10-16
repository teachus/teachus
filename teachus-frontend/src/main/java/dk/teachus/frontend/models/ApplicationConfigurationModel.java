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
package dk.teachus.frontend.models;

import org.apache.wicket.model.IModel;
import org.apache.wicket.util.string.Strings;

import dk.teachus.frontend.TeachUsApplication;

public class ApplicationConfigurationModel implements IModel {
	private static final long serialVersionUID = 1L;
	
	private String configurationKey;
	
	public ApplicationConfigurationModel(String configurationKey) {
		if (Strings.isEmpty(configurationKey)) {
			throw new IllegalArgumentException("Configuration key must not be null.");
		}
		
		this.configurationKey = configurationKey;
	}

	public Object getObject() {
		return convertConfigurationValue(TeachUsApplication.get().getConfiguration().getConfiguration(configurationKey));
	}
	
	protected Object convertConfigurationValue(String configurationValue) {
		return configurationValue;
	}

	public void setObject(Object object) {
		TeachUsApplication.get().getConfiguration().setConfiguration(configurationKey, convertModelValue(object));
	}
	
	protected String convertModelValue(Object modelValue) {
		return String.valueOf(modelValue);
	}

	public void detach() {
	}

}
