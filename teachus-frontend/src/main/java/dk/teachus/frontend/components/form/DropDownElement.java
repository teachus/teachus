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

public class DropDownElement extends AbstractChoiceElement {
	private static final long serialVersionUID = 1L;
	
	public static DropDownElement createTimeZoneElement(String label, IModel inputModel, boolean required) {
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
		
		IChoiceRenderer choiceRenderer = new ChoiceRenderer() {
			private static final long serialVersionUID = 1L;
			
			@Override
			public Object getDisplayValue(Object object) {
				TimeZone timeZone = (TimeZone) object;
				return timeZone.getID();
			}
		};
		return new DropDownElement(label, inputModel, choices, choiceRenderer, required);
	}
	
	private DropDownChoice dropDownChoice;
	
	public DropDownElement(String label, IModel inputModel, List<?> choices) {
		this(label, inputModel, choices, null, false);
	}
	
	public DropDownElement(String label, IModel inputModel, List<?> choices, IChoiceRenderer choiceRenderer) {
		this(label, inputModel, choices, choiceRenderer, false);
	}
	
	public DropDownElement(String label, IModel inputModel, List<?> choices, boolean required) {
		this(label, inputModel, choices, null, required);
	}
	
	public DropDownElement(String label, IModel inputModel, List<?> choices, IChoiceRenderer choiceRenderer, boolean required) {
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
		
		dropDownChoice = new DropDownChoice("dropDown", inputModel, choices, choiceRenderer) {
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isEnabled() {
				return isReadOnly() == false;
			}
		};
		dropDownChoice.setLabel(new Model(label));
		elementPanel.add(dropDownChoice);
		dropDownChoice.setRequired(required);
		dropDownChoice.setNullValid(required == false);
		dropDownChoice.setEnabled(isReadOnly() == false);
				
		return elementPanel;
	}
	
	@Override
	protected void addValidator(IValidator validator) {
		dropDownChoice.add(validator);
	}
	
	@Override
	public FormComponent getFormComponent() {
		return dropDownChoice;
	}

	@Override
	protected String getValidationEvent() {
		return "onchange";
	}
	
}
