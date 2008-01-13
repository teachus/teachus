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
package dk.teachus.frontend.pages.persons.mail;

import java.util.HashSet;
import java.util.Set;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.model.PropertyModel;

import dk.teachus.backend.dao.MessageDAO;
import dk.teachus.backend.domain.Message;
import dk.teachus.backend.domain.MessageState;
import dk.teachus.backend.domain.Person;
import dk.teachus.backend.domain.impl.MailMessage;
import dk.teachus.frontend.TeachUsApplication;
import dk.teachus.frontend.TeachUsSession;
import dk.teachus.frontend.UserLevel;
import dk.teachus.frontend.components.form.ButtonPanelElement;
import dk.teachus.frontend.components.form.FormPanel;
import dk.teachus.frontend.components.form.SelectPupilsElement;
import dk.teachus.frontend.components.form.TextAreaElement;
import dk.teachus.frontend.components.form.TextFieldElement;
import dk.teachus.frontend.pages.AuthenticatedBasePage;
import dk.teachus.frontend.pages.persons.PupilsPage;

public class MailPage extends AuthenticatedBasePage {
	private static final long serialVersionUID = 1L;
	
	private Set<Person> recipients = new HashSet<Person>();
	
	public MailPage() {
		super(UserLevel.TEACHER, true);
		
		final Message messageTemplate = new MailMessage();
		messageTemplate.setState(MessageState.FINAL);
		messageTemplate.setSender(TeachUsSession.get().getPerson());
		
		FormPanel mailForm = new FormPanel("mailForm"); //$NON-NLS-1$
		add(mailForm);
		
		mailForm.addElement(new SelectPupilsElement(TeachUsSession.get().getString("MailPage.recipients"), new PropertyModel(this, "recipients"), true)); //$NON-NLS-1$ //$NON-NLS-2$
		
		mailForm.addElement(new TextFieldElement(TeachUsSession.get().getString("MailPage.subject"), new PropertyModel(messageTemplate, "subject"), true, 50)); //$NON-NLS-1$ //$NON-NLS-2$
		
		TextAreaElement body = new TextAreaElement(TeachUsSession.get().getString("MailPage.message"), new PropertyModel(messageTemplate, "body"), true); //$NON-NLS-1$ //$NON-NLS-2$
		mailForm.addElement(body);
		
		mailForm.addElement(new ButtonPanelElement(TeachUsSession.get().getString("MailPage.sendMail")) { //$NON-NLS-1$
			private static final long serialVersionUID = 1L;

			@Override
			protected void onCancel() {
				getRequestCycle().setResponsePage(PupilsPage.class);
			}

			@Override
			protected void onSave(AjaxRequestTarget target) {
				MessageDAO messageDAO = TeachUsApplication.get().getMessageDAO();
				
				for (Person recipient : recipients) {
					Message message = messageTemplate.copy();
					message.setRecipient(recipient);
					
					messageDAO.save(message);
				}

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
		return TeachUsSession.get().getString("MailPage.sendMail"); //$NON-NLS-1$
	}

	public Set<Person> getRecipients() {
		return recipients;
	}

	public void setRecipients(Set<Person> recipients) {
		this.recipients = recipients;
	}
	
}
