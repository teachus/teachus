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
import org.apache.wicket.markup.html.panel.FeedbackPanel;

import dk.teachus.frontend.components.form.ElementModifier.BehaviorAdder;


public abstract class AbstractValidationInputElement<T> extends AbstractInputElement<T> implements ValidationProducer<T> {
	private static final long serialVersionUID = 1L;

	public AbstractValidationInputElement(String label) {
		super(label);
	}
	
	public AbstractValidationInputElement(String label, boolean required) {
		super(label, required);
	}

	public void add(BehaviorAdder behaviorAdder) {
		getFormComponent().add(behaviorAdder.createNewBehavior());
	}
	
	public boolean hasError() {
		return getFormComponent().hasErrorMessage();
	}
	
	public final Component[] inputValid() {
		return onInputValid(feedbackPanel);
	}

	public final Component[] inputInvalid() {
		return onInputInvalid(feedbackPanel);
	}
	
	@Override
	protected void onEndAttach() {
		add(new ElementModifier(getValidationEvent()));
	}
	
	protected abstract String getValidationEvent();

	public Component[] onInputValid(FeedbackPanel feedbackPanel) {
		return new Component[] {feedbackPanel};
	}

	public Component[] onInputInvalid(FeedbackPanel feedbackPanel) {
		return new Component[] {feedbackPanel};
	}


}
