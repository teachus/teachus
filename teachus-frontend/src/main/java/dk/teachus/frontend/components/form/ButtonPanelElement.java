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

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.behavior.SimpleAttributeModifier;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.repeater.RepeatingView;

import dk.teachus.frontend.TeachUsSession;

public abstract class ButtonPanelElement extends FormElement {
	private static final long serialVersionUID = 1L;
	
	public static interface IButton extends Serializable {
		String getValue();
		
		void onClick(AjaxRequestTarget target);
	}
	
	private Map<ValidationProducer, Boolean> errorStates = new HashMap<ValidationProducer, Boolean>();
	
	private Form associatedForm;

	public ButtonPanelElement() {
		this(TeachUsSession.get().getString("General.save"));
	}
	
	public ButtonPanelElement(String submitLabel) {
		Button cancelButton = new Button("cancelButton") {
			private static final long serialVersionUID = 1L;

			@Override
			public void onSubmit() {
				onCancel();
			}
		};
		cancelButton.add(new SimpleAttributeModifier("value", TeachUsSession.get().getString("PeriodPanel.regretInput")));
		add(cancelButton);
		
		AjaxButton saveButton = new AjaxButton("saveButton") {
			private static final long serialVersionUID = 1L;

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form form) {
				onSave(target);
			}
			
			@Override
			protected void onError(final AjaxRequestTarget target, Form form) {
				associatedForm.visitChildren(ValidationProducer.class, new org.apache.wicket.Component.IVisitor() {
					public Object component(Component component) {
						ValidationProducer validationProducer = (ValidationProducer) component;
						
						boolean hasErrorMessage = validationProducer.hasError();
						if (hasErrorMessage != errorStates.get(validationProducer)) {
							errorStates.put(validationProducer, hasErrorMessage);
							
							Component[] components = null;
							
							if (hasErrorMessage) {
								components = validationProducer.inputInvalid();
							} else {
								components = validationProducer.inputValid();
							}
							
							if (components != null) {
								for (Component component2 : components) {
									target.addComponent(component2);
								}
							}
						}
						
						return CONTINUE_TRAVERSAL;
					}
				});
			}
		};
		saveButton.add(new SimpleAttributeModifier("value", submitLabel));
		add(saveButton);
		
		RepeatingView additionalButtons = new RepeatingView("additionalButtons");
		add(additionalButtons);
		
		List<IButton> additionalButtonsList = getAdditionalButtons();
		if (additionalButtonsList != null) {
			for (final IButton button : additionalButtonsList) {
				AjaxButton ajaxButton = new AjaxButton(additionalButtons.newChildId()) {
					private static final long serialVersionUID = 1L;

					@Override
					protected void onSubmit(AjaxRequestTarget target, Form form) {
						button.onClick(target);
					}					
				};
				ajaxButton.add(new SimpleAttributeModifier("value", button.getValue()));
				additionalButtons.add(ajaxButton);
			}
		}
	}
	
	@Override
	protected void onBeforeRender() {
		super.onBeforeRender();
		
		associatedForm = (Form) findParent(Form.class);
		
		associatedForm.visitChildren(ValidationProducer.class, new IVisitor() {
			public Object component(Component component) {
				ValidationProducer validationProducer = (ValidationProducer) component;
				
				errorStates.put(validationProducer, validationProducer.hasError());
				
				return CONTINUE_TRAVERSAL;
			}
		});
	}
	
	protected List<IButton> getAdditionalButtons() {
		return null;
	}
	
	protected abstract void onCancel();
	
	protected abstract void onSave(AjaxRequestTarget target);
	
}
