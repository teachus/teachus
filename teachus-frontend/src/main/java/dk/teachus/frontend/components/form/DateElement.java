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

import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import org.apache.wicket.Component;
import org.apache.wicket.datetime.PatternDateConverter;
import org.apache.wicket.datetime.markup.html.form.DateTextField;
import org.apache.wicket.extensions.yui.calendar.DatePicker;
import org.apache.wicket.feedback.ComponentFeedbackMessageFilter;
import org.apache.wicket.feedback.IFeedbackMessageFilter;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.validation.IValidator;

public class DateElement extends AbstractValidationInputElement<Date> {
	private static final long serialVersionUID = 1L;

	private IModel<Date> inputModel;
	private DateTextField dateField;
	
	public DateElement(String label, IModel<Date> inputModel) {
		this(label, inputModel, false);
	}
	
	public DateElement(String label, IModel<Date> inputModel, boolean required) {
		super(label, required);
		
		this.inputModel = inputModel;
	}

	@Override
	protected void addValidator(IValidator<Date> validator) {
		dateField.add(validator);
	}

	@Override
	protected IFeedbackMessageFilter getFeedbackMessageFilter() {
		return new ComponentFeedbackMessageFilter(dateField);
	}

	@Override
	public FormComponent<Date> getFormComponent() {
		return dateField;
	}

	@Override
	protected String getValidationEvent() {
		return "onchange";
	}

	@Override
	protected Component newInputComponent(String wicketId, FeedbackPanel feedbackPanel) {
		DateElementPanel elementPanel = new DateElementPanel(wicketId);
		elementPanel.setRenderBodyOnly(true);
		
		dateField = new DateTextField("dateField", inputModel, new PatternDateConverter("yyyy-MM-dd", false)) {
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isEnabled() {
				return isReadOnly() == false;
			}
		};
		dateField.add(new DatePicker() {
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isEnabled(Component component) {
				return isReadOnly() == false;
			}
			
			@Override
			protected void configure(Map<String, Object> widgetProperties, IHeaderResponse response, Map<String, Object> initVariables) {
				super.configure(widgetProperties, response, initVariables);

				widgetProperties.put("START_WEEKDAY", Calendar.getInstance(getLocale()).getFirstDayOfWeek()-1);
			}
			
			@Override
			protected boolean enableMonthYearSelection() {
				return true;
			}
		});
		dateField.setLabel(new Model<String>(label));
		dateField.setRequired(required);
		elementPanel.add(dateField);
		
		return elementPanel;
	}

}
