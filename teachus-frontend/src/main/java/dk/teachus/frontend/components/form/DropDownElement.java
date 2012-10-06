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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.TimeZone;

import org.apache.wicket.Component;
import org.apache.wicket.feedback.ComponentFeedbackMessageFilter;
import org.apache.wicket.feedback.IFeedbackMessageFilter;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.validation.IValidator;

import dk.teachus.frontend.components.Select2Enabler;

public class DropDownElement<T> extends AbstractChoiceElement<T,T> {
	private static final long serialVersionUID = 1L;
	
	public static DropDownElement<TimeZone> createTimeZoneElement(String label, IModel<TimeZone> inputModel, boolean required) {
		List<TimeZone> choices = new ArrayList<TimeZone>();
		
		for (String timeZoneId : TimeZone.getAvailableIDs()) {
			TimeZone timeZone = TimeZone.getTimeZone(timeZoneId);
			choices.add(timeZone);
		}
		
		Collections.sort(choices, new Comparator<TimeZone>() {
			public int compare(TimeZone o1, TimeZone o2) {
				return o1.getID().compareTo(o2.getID());
			}
		});
		
		IChoiceRenderer<TimeZone> choiceRenderer = new ChoiceRenderer<TimeZone>() {
			private static final long serialVersionUID = 1L;
			
			@Override
			public Object getDisplayValue(TimeZone timeZone) {
				return timeZone.getID();
			}
		};
		return new DropDownElement<TimeZone>(label, inputModel, choices, choiceRenderer, required);
	}
	
	private DropDownChoice<T> dropDownChoice;
	
	public DropDownElement(String label, IModel<T> inputModel, List<T> choices) {
		this(label, inputModel, choices, null, false);
	}
	
	public DropDownElement(String label, IModel<T> inputModel, List<T> choices, IChoiceRenderer<T> choiceRenderer) {
		this(label, inputModel, choices, choiceRenderer, false);
	}
	
	public DropDownElement(String label, IModel<T> inputModel, List<T> choices, boolean required) {
		this(label, inputModel, choices, null, required);
	}
	
	public DropDownElement(String label, IModel<T> inputModel, List<T> choices, IChoiceRenderer<T> choiceRenderer, boolean required) {
		super(label, inputModel, choices, choiceRenderer, required);
	}
	
	@Override
	protected IFeedbackMessageFilter getFeedbackMessageFilter() {
		return new ComponentFeedbackMessageFilter(dropDownChoice);
	}

	@Override
	protected Component newInputComponent(String wicketId, FeedbackPanel feedbackPanel) {
		DropDownElementPanel elementPanel = new DropDownElementPanel(wicketId);
		elementPanel.setRenderBodyOnly(true);
		
		dropDownChoice = new DropDownChoice<T>("dropDown", inputModel, choices, choiceRenderer) {
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isEnabled() {
				return isReadOnly() == false;
			}
		};
		dropDownChoice.setLabel(new Model<String>(label));
		elementPanel.add(dropDownChoice);
		dropDownChoice.setRequired(required);
		dropDownChoice.setNullValid(required == false);
		dropDownChoice.setEnabled(isReadOnly() == false);
		dropDownChoice.add(new Select2Enabler());
				
		return elementPanel;
	}
	
	@Override
	protected void addValidator(IValidator<T> validator) {
		dropDownChoice.add(validator);
	}
	
	@Override
	public FormComponent<T> getFormComponent() {
		return dropDownChoice;
	}

	@Override
	protected String getValidationEvent() {
		return "onchange";
	}
	
}
