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

import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;

import dk.teachus.backend.domain.Teacher;
import dk.teachus.backend.domain.impl.WelcomeIntroductionTeacherAttribute;
import dk.teachus.frontend.TeachUsApplication;
import dk.teachus.frontend.TeachUsSession;

public class WelcomeMailSettingsPage extends AbstractSettingsPage {
	private static final long serialVersionUID = 1L;

	public WelcomeMailSettingsPage() {		
		createIntroductionMailForm();
	}

	private void createIntroductionMailForm() {
		Teacher teacher = (Teacher) TeachUsSession.get().getPerson();
		
		WelcomeIntroductionTeacherAttribute welcomeAttribute = TeachUsApplication.get().getPersonDAO().getAttribute(WelcomeIntroductionTeacherAttribute.class, teacher);
		if (welcomeAttribute == null) {
			welcomeAttribute = new WelcomeIntroductionTeacherAttribute();
			welcomeAttribute.setTeacher(teacher);
		}
		
		// Introduction mail text
		final WelcomeIntroductionTeacherAttribute attribute = welcomeAttribute;
		
		
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
	
	@Override
	protected String getPageLabel() {
		return TeachUsSession.get().getString("TeacherSettingsPage.introductionMailText");
	}

}
