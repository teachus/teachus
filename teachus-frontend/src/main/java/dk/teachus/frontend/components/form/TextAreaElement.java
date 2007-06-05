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
import wicket.feedback.ComponentFeedbackMessageFilter;
import wicket.markup.html.basic.Label;
import wicket.markup.html.form.FormComponent;
import wicket.markup.html.form.TextArea;
import wicket.markup.html.form.validation.IValidator;
import wicket.markup.html.panel.FeedbackPanel;
import wicket.model.AbstractReadOnlyModel;
import wicket.model.IModel;
import wicket.model.Model;
import dk.teachus.frontend.components.form.ElementModifier.BehaviorAdder;

public class TextAreaElement extends FormElement implements ValidationProducer {
	private static final long serialVersionUID = 1L;
	private TextArea inputField;
	private FeedbackPanel feedbackPanel;

	public TextAreaElement(String label, IModel inputModel) {
		this(label, inputModel, false);
	}
	
	public TextAreaElement(String label, IModel inputModel, final boolean required) {
		add(new Label("label", label).setRenderBodyOnly(true));
		
		IModel requiredModel = new AbstractReadOnlyModel() {
			private static final long serialVersionUID = 1L;

			@Override
			public Object getObject(Component component) {
				return required ? "*" : "&nbsp;";
			}
		};
		add(new Label("required", requiredModel).setEscapeModelStrings(false));
		
		feedbackPanel = new FeedbackPanel("feedback");
		feedbackPanel.setOutputMarkupId(true);
		add(feedbackPanel);
		
		inputField = new TextArea("inputField", inputModel);
		inputField.setRequired(required);
		inputField.setLabel(new Model(label));
		add(inputField);
		
		feedbackPanel.setFilter(new ComponentFeedbackMessageFilter(inputField));
	}
	
	@Override
	protected void onAttach() {
		add(new ElementModifier("onchange"));
	}
	
	public Component add(IValidator validator) {
		return inputField.add(validator);
	}

	public void add(BehaviorAdder adder) {
		inputField.add(adder.createNewBehavior());
	}

	public FormComponent getFormComponent() {
		return inputField;
	}

	public boolean hasError() {
		return inputField.hasErrorMessage();
	}

	public Component[] inputInvalid() {
		return new Component[] {feedbackPanel};
	}

	public Component[] inputValid() {
		return new Component[] {feedbackPanel};
	}
}
