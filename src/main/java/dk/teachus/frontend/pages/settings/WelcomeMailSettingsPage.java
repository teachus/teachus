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
package dk.teachus.frontend.pages.settings;

import java.util.List;

import wicket.markup.html.form.Button;
import wicket.markup.html.form.Form;
import wicket.markup.html.form.TextArea;
import wicket.model.Model;
import wicket.model.PropertyModel;
import dk.teachus.domain.Teacher;
import dk.teachus.domain.TeacherAttribute;
import dk.teachus.domain.impl.WelcomeIntroductionTeacherAttribute;
import dk.teachus.frontend.TeachUsApplication;
import dk.teachus.frontend.TeachUsSession;

public class WelcomeMailSettingsPage extends AbstractSettingsPage {
	private static final long serialVersionUID = 1L;
	
	private List<TeacherAttribute> attributes;

	public WelcomeMailSettingsPage() {		
		createIntroductionMailForm();
	}

	private void createIntroductionMailForm() {
		// Introduction mail text
		final WelcomeIntroductionTeacherAttribute attribute = getAttribute(WelcomeIntroductionTeacherAttribute.class);
		
		Form form = new Form("form");
		add(form);
		
		form.add(new TextArea("introductionMailText", new PropertyModel(attribute, "value")));
		form.add(new Button("save", new Model(TeachUsSession.get().getString("General.save"))) {
			private static final long serialVersionUID = 1L;

			@Override
			public void onSubmit() {
				TeachUsApplication.get().getPersonDAO().saveAttribute(attribute);
				getRequestCycle().setResponsePage(TeacherSettingsPage.class);
			}
		});
	}
	
	@SuppressWarnings("unchecked")
	private <A extends TeacherAttribute> A getAttribute(Class<A> attributeClass) {
		Teacher teacher = (Teacher) TeachUsSession.get().getPerson();
		
		if (attributes == null) {
			attributes = TeachUsApplication.get().getPersonDAO().getAttributes(teacher);
		}
		
		A attribute = null;
		
		for (TeacherAttribute att : attributes) {
			if (attributeClass.isInstance(att)) {
				attribute = (A) att;
			}
		}
		
		if (attribute == null) {
			try {
				attribute = attributeClass.newInstance();
				attribute.setTeacher(teacher);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
		
		return attribute;
	}
	
	@Override
	protected String getPageLabel() {
		return TeachUsSession.get().getString("TeacherSettingsPage.introductionMailText");
	}

}
