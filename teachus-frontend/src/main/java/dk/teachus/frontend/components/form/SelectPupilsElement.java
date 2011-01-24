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

import java.util.Collection;

import org.apache.wicket.Component;
import org.apache.wicket.feedback.ComponentFeedbackMessageFilter;
import org.apache.wicket.feedback.IFeedbackMessageFilter;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.validation.IValidator;

import dk.teachus.backend.domain.Pupil;

public class SelectPupilsElement extends AbstractInputElement<Collection<Pupil>> {
	private static final long serialVersionUID = 1L;
	private final IModel<Collection<Pupil>> inputModel;
	private SelectPupilsPanel selectPupilsPanel;
	
	public SelectPupilsElement(String label, IModel<Collection<Pupil>> inputModel, boolean required) {
		super(label, required);
		this.inputModel = inputModel;
	}

	@Override
	protected void addValidator(IValidator<Collection<Pupil>> validator) {
		selectPupilsPanel.getInputComponent().add(validator);
	}
	
	@Override
	protected IFeedbackMessageFilter getFeedbackMessageFilter() {
		return new ComponentFeedbackMessageFilter(this);
	}
	
	public FormComponent<Collection<Pupil>> getFormComponent() {
		return selectPupilsPanel.getInputComponent();
	}
	
	@Override
	protected Component newInputComponent(String wicketId, FeedbackPanel feedbackPanel) {
		this.feedbackPanel = feedbackPanel;
		
		selectPupilsPanel = new SelectPupilsPanel(wicketId, inputModel);
		return selectPupilsPanel;
	}
	
}
