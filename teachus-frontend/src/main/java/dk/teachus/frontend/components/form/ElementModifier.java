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

import org.apache.wicket.Component;
import org.apache.wicket.WicketRuntimeException;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.behavior.Behavior;

public class ElementModifier extends Behavior {
	private static final long serialVersionUID = 1L;

	public static interface BehaviorAdder extends Serializable {
		Behavior createNewBehavior();
	}
	
	private String event;
	private ValidationProducer validationProducer;
	
	public ElementModifier(String event) {
		this(event, null);
	}
	
	public ElementModifier(String event, ValidationProducer validationProducer) {
		this.event = event;
		this.validationProducer = validationProducer;
	}

	@Override
	public void bind(Component component) {
		final ValidationProducer validationProducer = getValidationProducer(component);
		
		validationProducer.add(new BehaviorAdder() {
			private static final long serialVersionUID = 1L;

			public Behavior createNewBehavior() {
				return new AjaxFormComponentUpdatingBehavior(event) {
					private static final long serialVersionUID = 1L;
					
					@Override
					protected void onUpdate(AjaxRequestTarget target) {
						Component[] components = validationProducer.inputValid();
						
						for (Component component2 : components) {
							target.add(component2);
						}
					}		
					
					@Override
					protected void onError(AjaxRequestTarget target, RuntimeException e) {
						Component[] components = validationProducer.inputInvalid();
						
						for (Component component2 : components) {
							target.add(component2);
						}
					}
				};
			}
			
		});
		
		if (validationProducer.hasError()) {
			validationProducer.inputInvalid();
		} else {
			validationProducer.inputValid();
		}
	}
	
	private ValidationProducer getValidationProducer(Component component) {
		if (validationProducer != null) {
			return validationProducer;
		} else {
			if (component instanceof ValidationProducer == false) {
				throw new WicketRuntimeException();
			}
			
			return (ValidationProducer) component;
		}
	}
	
}
