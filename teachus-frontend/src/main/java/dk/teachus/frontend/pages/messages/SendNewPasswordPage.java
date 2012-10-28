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
package dk.teachus.frontend.pages.messages;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.validation.EqualInputValidator;
import org.apache.wicket.markup.html.form.validation.IFormValidator;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.util.string.Strings;
import org.apache.wicket.validation.validator.StringValidator;

import dk.teachus.backend.dao.MessageDAO;
import dk.teachus.backend.dao.PersonDAO;
import dk.teachus.backend.domain.Message;
import dk.teachus.backend.domain.MessageState;
import dk.teachus.backend.domain.Pupil;
import dk.teachus.backend.domain.TeacherAttribute;
import dk.teachus.backend.domain.impl.MailMessage;
import dk.teachus.frontend.TeachUsApplication;
import dk.teachus.frontend.TeachUsSession;
import dk.teachus.frontend.UserLevel;
import dk.teachus.frontend.components.form.ButtonPanelElement;
import dk.teachus.frontend.components.form.FormPanel;
import dk.teachus.frontend.components.form.FormPanel.FormValidator;
import dk.teachus.frontend.components.form.GeneratePasswordElement;
import dk.teachus.frontend.components.form.PasswordFieldElement;
import dk.teachus.frontend.components.form.TextAreaElement;
import dk.teachus.frontend.models.PupilModel;
import dk.teachus.frontend.pages.AuthenticatedBasePage;
import dk.teachus.frontend.pages.persons.PupilsPage;

public class SendNewPasswordPage extends AuthenticatedBasePage {
	private static final long serialVersionUID = 1L;
	
	private String password1;
	private String password2;
	private String introMessage;
	
	public SendNewPasswordPage(final Long pupilId) {
		super(UserLevel.TEACHER, true);
		
		if (pupilId == null) {
			throw new IllegalArgumentException("Must provide a valid pupilId"); //$NON-NLS-1$
		}
		
		final PupilModel pupilModel = new PupilModel(pupilId);
		
		// Find intro message from teachers attributes
		final TeacherAttribute teacherAttribute = TeachUsSession.get().getTeacherAttribute();
		if (teacherAttribute != null) {
			setIntroMessage(teacherAttribute.getWelcomeIntroduction());
		}
		
		String title = TeachUsSession.get().getString("SendNewPasswordPage.title"); //$NON-NLS-1$
		title = title.replace("{pupilname}", pupilModel.getObject().getName()); //$NON-NLS-1$
		add(new Label("newPasswordTitle", title)); //$NON-NLS-1$
		
		final FormPanel formPanel = new FormPanel("passwordForm"); //$NON-NLS-1$
		add(formPanel);
		
		// Password 1
		final PasswordFieldElement password1Field = new PasswordFieldElement(
				TeachUsSession.get().getString("General.password"), new PropertyModel(this, "password1"), true); //$NON-NLS-1$ //$NON-NLS-2$
		password1Field.add(StringValidator.lengthBetween(4, 32));
		formPanel.addElement(password1Field);
		
		// Password 2
		final PasswordFieldElement password2Field = new PasswordFieldElement(
				TeachUsSession.get().getString("PersonPanel.repeatPassword"), new PropertyModel(this, "password2"), true); //$NON-NLS-1$ //$NON-NLS-2$
		formPanel.addElement(password2Field);
		
		// Password validator
		formPanel.addValidator(new FormValidator() {
			private static final long serialVersionUID = 1L;
			
			@Override
			public IFormValidator getFormValidator() {
				return new EqualInputValidator(password1Field.getFormComponent(), password2Field.getFormComponent());
			}
		});
		
		// Password generator
		formPanel.addElement(new GeneratePasswordElement("", pupilModel.getObject().getUsername()) { //$NON-NLS-1$
					private static final long serialVersionUID = 1L;
					
					@Override
					protected void passwordGenerated(final AjaxRequestTarget target, final String password) {
						setPassword1(password);
						setPassword2(password);
						
						target.addComponent(password1Field.getFormComponent());
						target.addComponent(password1Field.getFeedbackPanel());
						target.addComponent(password2Field.getFormComponent());
						target.addComponent(password2Field.getFeedbackPanel());
					}
				});
		
		// Text
		formPanel.addElement(new TextAreaElement(TeachUsSession.get().getString("SendNewPasswordPage.introMessage"), new PropertyModel(this, "introMessage"))); //$NON-NLS-1$ //$NON-NLS-2$
		
		// Buttons
		formPanel.addElement(new ButtonPanelElement(TeachUsSession.get().getString("General.send")) { //$NON-NLS-1$
					private static final long serialVersionUID = 1L;
					
					@Override
					protected void onCancel() {
						getRequestCycle().setResponsePage(PupilsPage.class);
					}
					
					@Override
					protected void onSave(final AjaxRequestTarget target) {
						final PersonDAO personDAO = TeachUsApplication.get().getPersonDAO();
						final MessageDAO messageDAO = TeachUsApplication.get().getMessageDAO();
						
						// Update pupil
						final Pupil pupil = pupilModel.getObject();
						pupil.setPassword(getPassword1());
						personDAO.save(pupil);
						
						// Create mail message
						final Message message = new MailMessage();
						message.setSender(pupil.getTeacher());
						message.setRecipient(pupil);
						message.setState(MessageState.FINAL);
						
						final String subject = TeachUsSession.get().getString("NewPasswordMail.subject"); //$NON-NLS-1$
						message.setSubject(subject);
						
						String body = TeachUsSession.get().getString("NewPasswordMail.body"); //$NON-NLS-1$
						body = body.replace("${username}", pupil.getUsername()); //$NON-NLS-1$
						body = body.replace("${password}", getPassword1()); //$NON-NLS-1$
						final String serverUrl = TeachUsApplication.get().getServerUrl();
						body = body.replace("${server}", serverUrl); //$NON-NLS-1$
						String im = getIntroMessage();
						if (Strings.isEmpty(im) == false) {
							im += "\n\n"; //$NON-NLS-1$
						}
						body = body.replace("${introMessage}", im); //$NON-NLS-1$
						message.setBody(body);
						
						messageDAO.save(message);
						
						getRequestCycle().setResponsePage(PupilsPage.class);
					}
				});
	}
	
	@Override
	public AuthenticatedPageCategory getPageCategory() {
		return AuthenticatedPageCategory.MESSAGES;
	}
	
	public String getPassword1() {
		return password1;
	}
	
	public void setPassword1(final String password1) {
		this.password1 = password1;
	}
	
	public String getPassword2() {
		return password2;
	}
	
	public void setPassword2(final String password2) {
		this.password2 = password2;
	}
	
	public String getIntroMessage() {
		return introMessage;
	}
	
	public void setIntroMessage(final String introMessage) {
		this.introMessage = introMessage;
	}
	
}
