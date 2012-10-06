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
import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.MarkupContainer;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.validation.IFormValidator;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.RepeatingView;

public class FormPanel extends Panel {
	private static final long serialVersionUID = 1L;
	static final String ELEMENT_ID = "element";
	
	public static interface FormValidator extends Serializable {
		IFormValidator getFormValidator();
	}
	
	private RepeatingView elements;
	private Form form;
	private List<FormValidator> validators = new ArrayList<FormValidator>();

	public FormPanel(String id) {
		super(id);
		
		form = new Form("form");
		add(form);
		
		elements = new RepeatingView("elements");
		form.add(elements);		
	}
	
	public MarkupContainer addElement(FormElement element) {
		WebMarkupContainer elementContainer = new WebMarkupContainer(elements.newChildId());
		elementContainer.setRenderBodyOnly(true);
		elements.add(elementContainer);
		
		elementContainer.add(element);
		
		return elementContainer;
	}

	public void addValidator(FormValidator formValidator) {
		validators.add(formValidator);
	}
	
	@Override
	protected void onBeforeRender() {
		super.onBeforeRender();
		
		for (FormValidator formValidator : validators) {
			form.add(formValidator.getFormValidator());
		}
	}
	
	@Override
	public void onAfterRender() {
		super.onAfterRender();
	}
	
}
