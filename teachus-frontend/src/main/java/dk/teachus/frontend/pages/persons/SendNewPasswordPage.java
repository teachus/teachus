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
package dk.teachus.frontend.pages.persons;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.validation.EqualInputValidator;
import org.apache.wicket.markup.html.form.validation.IFormValidator;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.protocol.http.WebRequest;
import org.apache.wicket.util.string.Strings;
import org.apache.wicket.validation.validator.StringValidator;

import dk.teachus.backend.dao.MessageDAO;
import dk.teachus.backend.dao.PersonDAO;
import dk.teachus.backend.domain.ApplicationConfiguration;
import dk.teachus.backend.domain.Message;
import dk.teachus.backend.domain.Pupil;
import dk.teachus.backend.domain.impl.MailMessage;
import dk.teachus.backend.domain.impl.WelcomeIntroductionTeacherAttribute;
import dk.teachus.frontend.TeachUsApplication;
import dk.teachus.frontend.TeachUsSession;
import dk.teachus.frontend.UserLevel;
import dk.teachus.frontend.components.form.ButtonPanelElement;
import dk.teachus.frontend.components.form.FormPanel;
import dk.teachus.frontend.components.form.GeneratePasswordElement;
import dk.teachus.frontend.components.form.PasswordFieldElement;
import dk.teachus.frontend.components.form.TextAreaElement;
import dk.teachus.frontend.components.form.FormPanel.FormValidator;
import dk.teachus.frontend.models.PupilModel;
import dk.teachus.frontend.pages.AuthenticatedBasePage;

public class SendNewPasswordPage extends AuthenticatedBasePage {
	private static final long serialVersionUID = 1L;
	private static final Log log = LogFactory.getLog(SendNewPasswordPage.class);

	private String password1;
	private String password2;
	private String introMessage;
	
	public SendNewPasswordPage(Long pupilId) {
		super(UserLevel.TEACHER, true);
		
		if (pupilId == null) {
			throw new IllegalArgumentException("Must provide a valid pupilId"); //$NON-NLS-1$
		}
		
		final PupilModel pupilModel = new PupilModel(pupilId);
		
		// Find intro message from teachers attributes
		PersonDAO personDAO = TeachUsApplication.get().getPersonDAO();
		WelcomeIntroductionTeacherAttribute welcomeIntroduction = personDAO.getAttribute(WelcomeIntroductionTeacherAttribute.class, pupilModel.getObject().getTeacher());
		if (welcomeIntroduction != null) {
			setIntroMessage(welcomeIntroduction.getValue());
		}
		
		String title = TeachUsSession.get().getString("SendNewPasswordPage.title"); //$NON-NLS-1$
		title = title.replace("{pupilname}", pupilModel.getObject().getName()); //$NON-NLS-1$
		add(new Label("newPasswordTitle", title)); //$NON-NLS-1$
		
		FormPanel formPanel = new FormPanel("passwordForm"); //$NON-NLS-1$
		add(formPanel);
		
		// Password 1
		final PasswordFieldElement password1Field = new PasswordFieldElement(TeachUsSession.get().getString("General.password"), new PropertyModel(this, "password1"), true); //$NON-NLS-1$ //$NON-NLS-2$
		password1Field.add(StringValidator.lengthBetween(4, 32));
		formPanel.addElement(password1Field);
		
		// Password 2
		final PasswordFieldElement password2Field = new PasswordFieldElement(TeachUsSession.get().getString("PersonPanel.repeatPassword"), new PropertyModel(this, "password2"), true); //$NON-NLS-1$ //$NON-NLS-2$
		formPanel.addElement(password2Field);
		
		// Password validator
		formPanel.addValidator(new FormValidator() {
			private static final long serialVersionUID = 1L;

			public IFormValidator getFormValidator() {
				return new EqualInputValidator(password1Field.getFormComponent(), password2Field.getFormComponent());
			}			
		});
		
		// Password generator
		formPanel.addElement(new GeneratePasswordElement("", pupilModel.getObject().getUsername()) { //$NON-NLS-1$
			private static final long serialVersionUID = 1L;

			@Override
			protected void passwordGenerated(AjaxRequestTarget target, String password) {
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
			protected void onSave(AjaxRequestTarget target) {
				PersonDAO personDAO = TeachUsApplication.get().getPersonDAO();
				MessageDAO messageDAO = TeachUsApplication.get().getMessageDAO();
				
				// Update pupil
				final Pupil pupil = pupilModel.getObject();
				pupil.setPassword(getPassword1());
				personDAO.save(pupil);
				
				// Create mail message
				Message message = new MailMessage();
				message.setSender(pupil.getTeacher());
				message.setRecipient(pupil);
				
				String subject = TeachUsSession.get().getString("NewPasswordMail.subject");
				message.setSubject(subject);
				
				String body = TeachUsSession.get().getString("NewPasswordMail.body");
				body = body.replace("${username}", pupil.getUsername());
				body = body.replace("${password}", getPassword1());
				String serverUrl = TeachUsApplication.get().getConfiguration().getConfiguration(ApplicationConfiguration.SERVER_URL);
				
				/*
				 * If the server URL is empty, then the administrator have misconfigured the system (forgot to set the
				 * server URL in the settings). We will get the server URL from the current server, but we will also
				 * warn the administrator by adding an entry to the log.
				 */
				if (Strings.isEmpty(serverUrl)) {
					log.error("No server url is set for the system. It's very important that you set it.");
					
					WebRequest request = (WebRequest) getRequest();
					HttpServletRequest httpServletRequest = request.getHttpServletRequest();
					
					StringBuilder b = new StringBuilder();
					b.append(httpServletRequest.getScheme()).append("://");
					b.append(httpServletRequest.getServerName());
					if (httpServletRequest.getServerPort() != 80 && httpServletRequest.getServerPort() != 443) {
						b.append(":").append(httpServletRequest.getServerPort());
					}
					
					serverUrl = b.toString();
				}
				
				body = body.replace("${server}", serverUrl);
				String im = getIntroMessage();
				if (Strings.isEmpty(im) == false) {
					im += "\n\n";
				}
				body = body.replace("${introMessage}", im);
				message.setBody(body);
				
				messageDAO.save(message);
				
				getRequestCycle().setResponsePage(PupilsPage.class);
			}			
		});
	}

	@Override
	protected AuthenticatedPageCategory getPageCategory() {
		return AuthenticatedPageCategory.PUPILS;
	}

	@Override
	protected String getPageLabel() {
		return TeachUsSession.get().getString("SendNewPasswordPage.sendNewPassword"); //$NON-NLS-1$
	}

	public String getPassword1() {
		return password1;
	}

	public void setPassword1(String password1) {
		this.password1 = password1;
	}

	public String getPassword2() {
		return password2;
	}

	public void setPassword2(String password2) {
		this.password2 = password2;
	}
	
	public String getIntroMessage() {
		return introMessage;
	}

	public void setIntroMessage(String introMessage) {
		this.introMessage = introMessage;
	}

}
