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

import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.model.IModel;

public abstract class AbstractChoiceElement<S,T> extends AbstractValidationInputElement<T> {
	private static final long serialVersionUID = 1L;
	
	protected IModel<S> inputModel;
	protected List<? extends T> choices;
	protected IChoiceRenderer<? super T> choiceRenderer;
	
	public AbstractChoiceElement(String label, IModel<S> inputModel, List<? extends T> choices) {
		this(label, inputModel, choices, null, false);
	}
	
	public AbstractChoiceElement(String label, IModel<S> inputModel, List<? extends T> choices, IChoiceRenderer<? super T> choiceRenderer) {
		this(label, inputModel, choices, choiceRenderer, false);
	}
	
	public AbstractChoiceElement(String label, IModel<S> inputModel, List<? extends T> choices, boolean required) {
		this(label, inputModel, choices, null, required);
	}
	
	public AbstractChoiceElement(String label, IModel<S> inputModel, List<? extends T> choices, IChoiceRenderer<? super T> choiceRenderer, boolean required) {
		super(label, required);
		
		this.inputModel = inputModel;
		this.choices = choices;
		this.choiceRenderer = choiceRenderer;
	}
	
}
