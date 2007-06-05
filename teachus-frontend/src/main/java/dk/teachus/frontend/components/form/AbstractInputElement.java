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
import java.util.List;

import wicket.Component;
import wicket.feedback.IFeedbackMessageFilter;
import wicket.markup.html.basic.Label;
import wicket.markup.html.form.FormComponent;
import wicket.markup.html.form.validation.IValidator;
import wicket.markup.html.panel.FeedbackPanel;
import wicket.model.AbstractReadOnlyModel;
import wicket.model.IModel;

public abstract class AbstractInputElement extends FormElement {
	private static final long serialVersionUID = 1L;
	
	private boolean attached = false;
	protected boolean required;
	protected String label;
	private List<IValidator> validators = new ArrayList<IValidator>();

	protected FeedbackPanel feedbackPanel;

	public AbstractInputElement(String label) {
		this(label, false);
	}
	
	public AbstractInputElement(String label, boolean required) {		
		this.required = required;
		this.label = label;
	}
	
	public void add(IValidator validator) {
		validators.add(validator);
	}
	
	public FeedbackPanel getFeedbackPanel() {
		return feedbackPanel;
	}
	
	@Override
	protected void onAttach() {
		if (attached == false) {
			Component label = newLabelComponent("label");
			add(label);
			
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
			
			Component input = newInputComponent("input", feedbackPanel);
			add(input);
			
			feedbackPanel.setFilter(getFeedbackMessageFilter());
			
			for (IValidator validator : validators) {
				addValidator(validator);
			}
			
			onEndAttach();
			
			attached = true;
		}
	}
	
	protected void onEndAttach() {
	}
	
	protected Component newLabelComponent(String wicketId) {
		return new Label(wicketId, label).setRenderBodyOnly(true);
	}
	
	protected abstract Component newInputComponent(String wicketId, FeedbackPanel feedbackPanel);
	
	protected abstract IFeedbackMessageFilter getFeedbackMessageFilter();
	
	protected abstract void addValidator(IValidator validator);
	
	protected abstract FormComponent getFormComponent();

}
