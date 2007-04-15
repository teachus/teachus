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
import wicket.behavior.SimpleAttributeModifier;
import wicket.feedback.ComponentFeedbackMessageFilter;
import wicket.feedback.IFeedbackMessageFilter;
import wicket.markup.html.form.FormComponent;
import wicket.markup.html.form.PasswordTextField;
import wicket.markup.html.form.validation.IValidator;
import wicket.markup.html.panel.FeedbackPanel;
import wicket.model.IModel;
import wicket.model.Model;

public class PasswordFieldElement extends AbstractValidationInputElement {
	private static final long serialVersionUID = 1L;

	private IModel inputModel;
	private int size;
	private PasswordTextField passwordTextField;
	
	public PasswordFieldElement(String label, IModel inputModel) {
		this(label, inputModel, false);
	}

	public PasswordFieldElement(String label, IModel inputModel, boolean required) {
		this(label, inputModel, -1, required);
	}
	
	public PasswordFieldElement(String label, IModel inputModel, int size) {
		this(label, inputModel, size, false);
	}
	
	public PasswordFieldElement(String label, IModel inputModel, int size, boolean required) {
		super(label, required);
		
		this.size = size;
		this.inputModel = inputModel;
	}

	@Override
	protected IFeedbackMessageFilter getFeedbackMessageFilter() {
		return new ComponentFeedbackMessageFilter(passwordTextField);
	}

	@Override
	protected final Component newInputComponent(String wicketId, FeedbackPanel feedbackPanel) {
		PasswordFieldElementPanel inputPanel = new PasswordFieldElementPanel(wicketId);
		inputPanel.setRenderBodyOnly(true);
		
		passwordTextField = new PasswordTextField("inputField", inputModel);
//		passwordTextField.add(new TextFieldErrorModifier(feedbackPanel, "onchange"));
		passwordTextField.setRequired(required);
		passwordTextField.setResetPassword(false);
		passwordTextField.setOutputMarkupId(true);
		passwordTextField.setLabel(new Model(label));
		if (size > -1) {
			passwordTextField.add(new SimpleAttributeModifier("size", ""+size));
		}
		inputPanel.add(passwordTextField);
		
		return inputPanel;
	}
	
	@Override
	protected void addValidator(IValidator validator) {
		passwordTextField.add(validator);
	}
	
	@Override
	public FormComponent getFormComponent() {
		return passwordTextField;
	}
	
	@Override
	protected String getValidationEvent() {
		return "onchange";
	}

}
