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
package dk.teachus.frontend.components.form;

import java.util.Locale;

import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.util.convert.IConverter;
import org.apache.wicket.util.convert.converters.IntegerConverter;

public class IntegerFieldElement extends TextFieldElement {
	private static final long serialVersionUID = 1L;
	
	private class LocalIntegerConverter implements IConverter {
		private static final long serialVersionUID = 1L;

		public Object convertToObject(String value, Locale locale) {
			Integer intValue = (Integer) IntegerConverter.INSTANCE.convertToObject(value, locale);
			
			if (intValue == null) {
				intValue = getDefaultNullValue();
			}
			
			return intValue;
		}

		public String convertToString(Object value, Locale locale) {
			return IntegerConverter.INSTANCE.convertToString(value, locale);
		}
		
	}
	
	private Integer defaultNullValue;
	
	public IntegerFieldElement(String label, IModel inputModel) {
		super(label, inputModel);
	}
	
	public IntegerFieldElement(String label, IModel inputModel, int size) {
		super(label, inputModel, size);
	}
	
	public IntegerFieldElement(String label, IModel inputModel, boolean required) {
		super(label, inputModel, required);
	}
	
	public IntegerFieldElement(String label, IModel inputModel, boolean required, int size) {
		super(label, inputModel, required, size);
	}

	@Override
	protected TextField getNewInputComponent(String wicketId, FeedbackPanel feedbackPanel) {
		TextField textField = super.getNewInputComponent(wicketId, feedbackPanel);
		textField.setType(Integer.class);
		return textField;
	}
	
	@Override
	protected IConverter getComponentConverter(Class<?> type) {
		return new LocalIntegerConverter();
	}
	
	public void setDefaultNullValue(Integer defaultNullValue) {
		this.defaultNullValue = defaultNullValue;
	}
	
	public Integer getDefaultNullValue() {
		return defaultNullValue;
	}
	
}
