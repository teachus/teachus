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

import wicket.Component;
import wicket.extensions.yui.calendar.DateField;
import wicket.feedback.ComponentFeedbackMessageFilter;
import wicket.feedback.IFeedbackMessageFilter;
import wicket.markup.html.form.FormComponent;
import wicket.markup.html.form.validation.IValidator;
import wicket.markup.html.panel.FeedbackPanel;
import wicket.model.IModel;
import wicket.model.Model;

public class DateElement extends AbstractValidationInputElement {
	private static final long serialVersionUID = 1L;

	private IModel inputModel;
	private DateField dateField;
	
	public DateElement(String label, IModel inputModel) {
		this(label, inputModel, false);
	}
	
	public DateElement(String label, IModel inputModel, boolean required) {
		super(label, required);
		
		this.inputModel = inputModel;
	}

	@Override
	protected void addValidator(IValidator validator) {
		dateField.add(validator);
	}

	@Override
	protected IFeedbackMessageFilter getFeedbackMessageFilter() {
		return new ComponentFeedbackMessageFilter(dateField);
	}

	@Override
	public FormComponent getFormComponent() {
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
		
		dateField = new DateField("dateField", inputModel);
		dateField.setLabel(new Model(label));
		dateField.setRequired(required);
		elementPanel.add(dateField);
		
		return elementPanel;
	}

}
