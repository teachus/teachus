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

import java.util.List;

import wicket.Component;
import wicket.feedback.ComponentFeedbackMessageFilter;
import wicket.feedback.IFeedbackMessageFilter;
import wicket.markup.html.form.CheckBoxMultipleChoice;
import wicket.markup.html.form.FormComponent;
import wicket.markup.html.form.IChoiceRenderer;
import wicket.markup.html.form.validation.IValidator;
import wicket.markup.html.panel.FeedbackPanel;
import wicket.model.IModel;
import wicket.model.Model;
import dk.teachus.frontend.components.form.ElementModifier.BehaviorAdder;

public class CheckGroupElement extends AbstractChoiceElement {
	private static final long serialVersionUID = 1L;
	private CheckBoxMultipleChoice checkGroup;
	
	public CheckGroupElement(String label, IModel inputModel, List choices) {
		this(label, inputModel, choices, null, false);
	}
	
	public CheckGroupElement(String label, IModel inputModel, List choices, IChoiceRenderer choiceRenderer) {
		this(label, inputModel, choices, choiceRenderer, false);
	}
	
	public CheckGroupElement(String label, IModel inputModel, List choices, boolean required) {
		this(label, inputModel, choices, null, required);
	}
	
	public CheckGroupElement(String label, IModel inputModel, List choices, IChoiceRenderer choiceRenderer, boolean required) {
		super(label, inputModel, choices, choiceRenderer, required);
	}

	@Override
	public FormComponent getFormComponent() {
		return checkGroup;
	}

	@Override
	protected Component newInputComponent(String wicketId, FeedbackPanel feedbackPanel) {
		checkGroup = new CheckBoxMultipleChoice(wicketId, inputModel, choices, choiceRenderer);
		checkGroup.setRequired(required);
		checkGroup.setLabel(new Model(label));
		return checkGroup;
	}

	@Override
	protected IFeedbackMessageFilter getFeedbackMessageFilter() {
		return new ComponentFeedbackMessageFilter(checkGroup);
	}

	@Override
	protected void addValidator(IValidator validator) {
		checkGroup.add(validator);
	}

	@Override
	protected String getValidationEvent() {
		return null; // Not needed
	}
	
	@Override
	public void add(BehaviorAdder behaviorAdder) {
		// Do nothing
	}
	
}