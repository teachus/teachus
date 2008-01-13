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
package dk.teachus.backend.bean.impl;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;

import javax.mail.internet.InternetAddress;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import dk.teachus.backend.MailException;
import dk.teachus.backend.bean.MailBean;
import dk.teachus.backend.dao.MessageDAO;
import dk.teachus.backend.domain.Message;
import dk.teachus.backend.domain.Person;
import dk.teachus.backend.domain.impl.MailMessage;

public class MailMessageSendingBean {
	private static final Log log = LogFactory.getLog(MailMessageSendingBean.class);
	
	private MailBean mailBean;
	private MessageDAO messageDAO;
	
	public MailMessageSendingBean(MailBean mailBean, MessageDAO messageDAO) {
		this.mailBean = mailBean;
		this.messageDAO = messageDAO;
	}
	
	/**
	 * Only allow one execution of this at a time
	 */
	public synchronized void sendMailMessages() {
		List<Message> unsentMessages = messageDAO.getUnsentMessages();
		
		for (Message message : unsentMessages) {
			if (message instanceof MailMessage) {
				try {
					if (log.isDebugEnabled()) {
						log.debug("Sending new message with id: "+message.getId()+" and subject: "+message.getSubject());
					}
					
					MailMessage mailMessage = (MailMessage) message;					

					Person recipientPerson = mailMessage.getRecipient();
					Person senderPerson = mailMessage.getSender();
					
					InternetAddress sender = new InternetAddress(senderPerson.getEmail(), mailMessage.getSender().getName());
					InternetAddress recipient = new InternetAddress(recipientPerson.getEmail(), recipientPerson.getName());
					String subject = mailMessage.getSubject();
					
					String body = mailMessage.getBody();
					body = body.replace("${recipient.name}", recipientPerson.getName());
					body = body.replace("${recipient.email}", recipientPerson.getEmail());
					body = body.replace("${sender.name}", senderPerson.getName());
					body = body.replace("${sender.email}", senderPerson.getEmail());
					
					if (log.isDebugEnabled()) {
						log.debug("Sending mail to: "+recipient);
					}
					
					mailBean.sendMail(sender, recipient, subject, body, mailMessage.getType());
					
					message.setSent(true);
					message.setSentDate(new Date());
					messageDAO.save(message);
					
					if (log.isDebugEnabled()) {
						log.debug("Message succesfully sent: "+message.getId());
					}
				} catch (UnsupportedEncodingException e) {
					throw new RuntimeException(e);
				} catch (MailException e) {
					log.error("Couldn't send the mail with message id: "+message.getId(), e);
				}
			}
		}
	}
	
}
