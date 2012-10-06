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
import org.apache.wicket.model.PropertyModel;

import dk.teachus.backend.dao.MessageDAO;
import dk.teachus.backend.domain.Message;
import dk.teachus.backend.domain.MessageState;
import dk.teachus.frontend.TeachUsApplication;
import dk.teachus.frontend.TeachUsSession;
import dk.teachus.frontend.UserLevel;
import dk.teachus.frontend.components.form.ButtonPanelElement;
import dk.teachus.frontend.components.form.FormPanel;
import dk.teachus.frontend.components.form.MultiLineReadOnlyElement;
import dk.teachus.frontend.components.form.ReadOnlyElement;
import dk.teachus.frontend.pages.AuthenticatedBasePage;
import dk.teachus.frontend.pages.persons.PupilsPage;
import dk.teachus.frontend.utils.MessageStateChoiceRenderer;

public class EditMessagePage extends AuthenticatedBasePage {
	private static final long serialVersionUID = 1L;
	
	private final Message message;
	
	public EditMessagePage(Message message) {
		super(UserLevel.TEACHER, true);
		
		this.message = message;
				
		FormPanel mailForm = new FormPanel("mailForm"); //$NON-NLS-1$
		add(mailForm);
		
		mailForm.addElement(new ReadOnlyElement(TeachUsSession.get().getString("Messages.recipient"), new PropertyModel(message, "recipient.name"))); //$NON-NLS-1$ //$NON-NLS-2$
		
		mailForm.addElement(new ReadOnlyElement(TeachUsSession.get().getString("Messages.subject"), new PropertyModel(message, "subject"))); //$NON-NLS-1$ //$NON-NLS-2$
		
		mailForm.addElement(new ReadOnlyElement(TeachUsSession.get().getString("Messages.state"), new PropertyModel(message, "state"), new MessageStateChoiceRenderer())); //$NON-NLS-1$ //$NON-NLS-2$
		
		mailForm.addElement(new MultiLineReadOnlyElement(TeachUsSession.get().getString("Messages.message"), new PropertyModel(message, "body"))); //$NON-NLS-1$ //$NON-NLS-2$
		
		if (message.getState() == MessageState.ERROR_SENDING_INVALID_RECIPIENT 
				|| message.getState() == MessageState.ERROR_SENDING_UNKNOWN) {
			mailForm.addElement(new ButtonPanelElement(TeachUsSession.get().getString("Messages.reSend")) { //$NON-NLS-1$
				private static final long serialVersionUID = 1L;
	
				@Override
				protected void onCancel() {
					getRequestCycle().setResponsePage(PupilsPage.class);
				}
	
				@Override
				protected void onSave(AjaxRequestTarget target) {
					MessageDAO messageDAO = TeachUsApplication.get().getMessageDAO();
					Message m = EditMessagePage.this.message;
					m.setState(MessageState.FINAL);
					m.setProcessingDate(null);
					messageDAO.save(m);
					
					getRequestCycle().setResponsePage(SentMessagesPage.class);				
				}
			});
		}
	}
	
	@Override
	protected AuthenticatedPageCategory getPageCategory() {
		return AuthenticatedPageCategory.MESSAGES;
	}
	
}
