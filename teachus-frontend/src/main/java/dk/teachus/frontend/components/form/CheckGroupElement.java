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

import org.apache.wicket.Component;
import org.apache.wicket.feedback.ComponentFeedbackMessageFilter;
import org.apache.wicket.feedback.IFeedbackMessageFilter;
import org.apache.wicket.markup.html.form.CheckBoxMultipleChoice;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.validation.IValidator;

import dk.teachus.frontend.components.form.ElementModifier.BehaviorAdder;

public class CheckGroupElement<T> extends AbstractChoiceElement<List<T>,T> {
	private static final long serialVersionUID = 1L;
	private CheckBoxMultipleChoice<T> checkGroup;
	
	public CheckGroupElement(String label, IModel<List<T>> inputModel, List<? extends T> choices) {
		this(label, inputModel, choices, null, false);
	}
	
	public CheckGroupElement(String label, IModel<List<T>> inputModel, List<? extends T> choices, IChoiceRenderer<? super T> choiceRenderer) {
		this(label, inputModel, choices, choiceRenderer, false);
	}
	
	public CheckGroupElement(String label, IModel<List<T>> inputModel, List<? extends T> choices, boolean required) {
		this(label, inputModel, choices, null, required);
	}
	
	public CheckGroupElement(String label, IModel<List<T>> inputModel, List<? extends T> choices, IChoiceRenderer<? super T> choiceRenderer, boolean required) {
		super(label, inputModel, choices, choiceRenderer, required);
	}

	@Override
	public FormComponent getFormComponent() {
		return checkGroup;
	}

	@Override
	protected Component newInputComponent(String wicketId, FeedbackPanel feedbackPanel) {
		checkGroup = new CheckBoxMultipleChoice<T>(wicketId, inputModel, choices, choiceRenderer) {
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isEnabled() {
				return isReadOnly() == false;
			}
		};
		checkGroup.setRequired(required);
		checkGroup.setLabel(new Model<String>(label));
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