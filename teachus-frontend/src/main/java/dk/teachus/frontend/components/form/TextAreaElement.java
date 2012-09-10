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

import org.apache.wicket.Component;
import org.apache.wicket.feedback.ComponentFeedbackMessageFilter;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.validation.IValidator;

import dk.teachus.frontend.components.form.ElementModifier.BehaviorAdder;

public class TextAreaElement extends FormElement implements ValidationProducer<String> {
	private static final long serialVersionUID = 1L;
	private TextArea<String> inputField;
	private FeedbackPanel feedbackPanel;

	public TextAreaElement(String label, IModel<String> inputModel) {
		this(label, inputModel, false);
	}
	
	public TextAreaElement(String label, IModel<String> inputModel, final boolean required) {
		add(new Label("label", label).setRenderBodyOnly(true));
		
		IModel<String> requiredModel = new AbstractReadOnlyModel<String>() {
			private static final long serialVersionUID = 1L;

			@Override
			public String getObject() {
				return required ? "*" : "&nbsp;";
			}
		};
		add(new Label("required", requiredModel).setEscapeModelStrings(false));
		
		feedbackPanel = new FeedbackPanel("feedback");
		feedbackPanel.setOutputMarkupId(true);
		add(feedbackPanel);
		
		inputField = new TextArea<String>("inputField", inputModel);
		inputField.setRequired(required);
		inputField.setLabel(new Model<String>(label));
		add(inputField);
		
		feedbackPanel.setFilter(new ComponentFeedbackMessageFilter(inputField));
	}
	
	@Override
	protected void onBeforeRender() {
		super.onBeforeRender();
		
		add(new ElementModifier("onchange"));
	}
	
	public Component add(IValidator<String> validator) {
		return inputField.add(validator);
	}

	public void add(BehaviorAdder adder) {
		inputField.add(adder.createNewBehavior());
	}

	public FormComponent<String> getFormComponent() {
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
