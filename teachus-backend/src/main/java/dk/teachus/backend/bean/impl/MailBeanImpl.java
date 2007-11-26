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

import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;

import dk.teachus.backend.MailException;
import dk.teachus.backend.bean.MailBean;
import dk.teachus.backend.domain.impl.MailMessage.Type;

public class MailBeanImpl implements MailBean {
	private static final long serialVersionUID = 1L;
	
	private JavaMailSender mailSender;

	public MailBeanImpl(JavaMailSender mailSender) {
		this.mailSender = mailSender;
	}
	
	public void sendMail(final InternetAddress sender, final InternetAddress recipient, final String subject, final String body, final Type mailType) throws MailException {
		try {
			mailSender.send(new MimeMessagePreparator() {
				public void prepare(MimeMessage mimeMessage) throws Exception {
					MimeMessageHelper message = new MimeMessageHelper(mimeMessage, false, "UTF-8");
					
					message.setFrom(sender);
					message.addTo(recipient);
					message.setSubject(subject);
					
					switch(mailType) {
						case HTML:
							message.setText(body, true);
							break;
						default:
							message.setText(body);
							break;
					}
				}
			});
		} catch (MailSendException e) {
			throw new MailException(e);
		}
	}
	
}
