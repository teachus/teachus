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

import java.io.InputStream;

import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessagePreparator;

import dk.teachus.backend.bean.MailBean;
import dk.teachus.backend.domain.impl.MailMessage;
import dk.teachus.backend.test.SpringTestCase;

public class TestMailBeanImpl extends SpringTestCase {
	private static final long serialVersionUID = 1L;
		
	public void testSendMail() throws Exception {
		MailBean mailBean = createMailBean();
		
		mailBean.sendMail(new InternetAddress("test@teachus.dk"), new InternetAddress("test2@teachus.dk"), "A subject", "A body", MailMessage.Type.PLAIN);
	}

	private MailBean createMailBean() throws NoSuchFieldException, IllegalAccessException {
		// Replace mailsender with a dummy
		JavaMailSender mailSender = new JavaMailSender() {
			private JavaMailSender wrappedSender = new JavaMailSenderImpl();

			public MimeMessage createMimeMessage() {
				return wrappedSender.createMimeMessage();
			}

			public MimeMessage createMimeMessage(InputStream arg0) throws MailException {
				return wrappedSender.createMimeMessage(arg0);
			}

			public void send(MimeMessage arg0) throws MailException {				
			}

			public void send(MimeMessage[] arg0) throws MailException {				
			}

			public void send(MimeMessagePreparator arg0) throws MailException {		
				try {
					arg0.prepare(createMimeMessage());
				} catch (Exception e) {
					throw new MailException("", e) {
						private static final long serialVersionUID = 1L;
					};
				}
			}

			public void send(MimeMessagePreparator[] arg0) throws MailException {				
			}

			public void send(SimpleMailMessage arg0) throws MailException {				
			}

			public void send(SimpleMailMessage[] arg0) throws MailException {				
			}			
		};

		return new MailBeanImpl(mailSender);
	}
	
}
