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

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.feedback.ComponentFeedbackMessageFilter;
import org.apache.wicket.feedback.IFeedbackMessageFilter;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.util.convert.IConverter;
import org.apache.wicket.validation.IValidator;

public class TextFieldElement<T> extends AbstractValidationInputElement<T> {
	private static final long serialVersionUID = 1L;

	private IModel<T> inputModel;
	private int size;
	private TextField<T> textField;

	public TextFieldElement(String label, IModel<T> inputModel) {
		this(label, inputModel, false);
	}
	
	public TextFieldElement(String label, IModel<T> inputModel, int size) {
		this(label, inputModel, false, size);
	}
	
	public TextFieldElement(String label, IModel<T> inputModel, boolean required) {
		this(label, inputModel, required, -1);
	}
	
	public TextFieldElement(String label, IModel<T> inputModel, boolean required, int size) {
		super(label, required);
		this.inputModel = inputModel;
		this.size = size;
	}
	
	@Override
	protected final Component newInputComponent(String wicketId, FeedbackPanel feedbackPanel) {
		TextFieldElementPanel inputPanel = new TextFieldElementPanel(wicketId);
		inputPanel.setRenderBodyOnly(true);
		
		textField = getNewInputComponent("inputField", feedbackPanel);
		textField.setLabel(new Model<String>(label));
		textField.setRequired(required);
		textField.setOutputMarkupId(true);
		inputPanel.add(textField);
		
		return inputPanel;
	}
	
	@Override
	protected IFeedbackMessageFilter getFeedbackMessageFilter() {
		return new ComponentFeedbackMessageFilter(textField);
	}

	protected TextField<T> getNewInputComponent(String wicketId, FeedbackPanel feedbackPanel) {
		TextField<T> textField = new TextField<T>(wicketId, inputModel) {
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isEnabled() {
				return isReadOnly() == false;
			}
			
			@SuppressWarnings("unchecked")
			@Override
			public <C> IConverter<C> getConverter(Class<C> type) {
				return (IConverter<C>) getComponentConverter((Class<T>) type);
			}
		};
		if (size > -1) {
			textField.add(AttributeModifier.append("size", ""+size));
		}
		return textField;
	}
	
	@Override
	protected void addValidator(IValidator<T> validator) {
		textField.add(validator);
	}
	
	@Override
	public FormComponent<T> getFormComponent() {
		return textField;
	}
	
	@Override
	protected String getValidationEvent() {
		return "onchange";
	}
	
	protected IConverter<T> getComponentConverter(Class<T> type) {
		return getApplication().getConverterLocator().getConverter(type);
	}
	
}
