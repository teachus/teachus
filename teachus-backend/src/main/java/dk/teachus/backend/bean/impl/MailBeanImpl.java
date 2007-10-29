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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.web.util.HtmlUtils;

import dk.teachus.backend.MailException;
import dk.teachus.backend.bean.MailBean;
import dk.teachus.backend.bean.VelocityBean;
import dk.teachus.backend.domain.ApplicationConfiguration;
import dk.teachus.backend.domain.PupilBooking;
import dk.teachus.backend.domain.Teacher;
import dk.teachus.utils.ClassUtils;

public class MailBeanImpl implements MailBean {
	private static final long serialVersionUID = 1L;
	
	private JavaMailSender mailSender;
	private VelocityBean velocityBean;

	public MailBeanImpl(JavaMailSender mailSender, VelocityBean velocityBean) {
		this.mailSender = mailSender;
		this.velocityBean = velocityBean;
	}
	
	public void sendNewBookingsMail(final Teacher teacher, final List<PupilBooking> pupilBookings, final ApplicationConfiguration configuration) {
		if (pupilBookings.isEmpty() == false) {
			mailSender.send(new MimeMessagePreparator() {
				public void prepare(MimeMessage mimeMessage) throws Exception {
					MimeMessageHelper message = new MimeMessageHelper(mimeMessage, false, "UTF-8");
	
					// Sender and recipient
					String fromEmail = "notification@teachus.dk";
					String fromName = "TeachUs Booking System";
					
					message.setFrom(new InternetAddress(fromEmail, fromName));
					message.setTo(new InternetAddress(teacher.getEmail(), teacher.getName()));
					
					// The locale
					Locale locale = teacher.getLocale();
					
					// Build up bookingslist and format date
					List<FormattedPupilBooking> pupilBookingList = new ArrayList<FormattedPupilBooking>();
					for (PupilBooking pupilBooking : pupilBookings) {
						FormattedPupilBooking formattedPupilBooking = new FormattedPupilBooking();
						formattedPupilBooking.setPupilBooking(pupilBooking);
						SimpleDateFormat dateFormat = new SimpleDateFormat("EE, d. MMM yyyy H:mm", locale);
						formattedPupilBooking.setFormattedDate(dateFormat.format(pupilBooking.getDate()));
						pupilBookingList.add(formattedPupilBooking);
					}
					
					// Load properties
					ResourceBundle bundle = ResourceBundle.getBundle(ClassUtils.getAsResourceBundlePath(MailBeanImpl.class, "NewBookingsMail"), locale);	
					
					// Parse the template
					Map<String, Object> model = new HashMap<String, Object>();
					model.put("subject", bundle.getString("subject"));
					model.put("followingPupilsHasBookings", bundle.getString("followingPupilsHasBookings"));
					model.put("regard", bundle.getString("regard"));
					model.put("pupilHeader", bundle.getString("pupilHeader"));
					model.put("dateHeader", bundle.getString("dateHeader"));
					model.put("from", HtmlUtils.htmlEscape(fromName));
					model.put("pupilBookingList", pupilBookingList);
					String template = velocityBean.mergeTemplate(ClassUtils.getAsResourcePath(MailBeanImpl.class, "NewBookingsMail.vm"), model);
					
					// Subject
					// First line in parsed template is the subject
					String subject = template.substring(0, template.indexOf('\n'));
					message.setSubject(subject);
					
					// Text
					String text = template.substring(template.indexOf('\n'));
					message.setText(text, true);
	
					mimeMessage.addHeader("X-Mailer", "TeachUs ("+configuration.getConfiguration(ApplicationConfiguration.VERSION)+")");
				}
			});
		}
	}
	
	public void sendMail(final InternetAddress sender, final InternetAddress recipient, final String subject, final String body) throws MailException {
		try {
			mailSender.send(new MimeMessagePreparator() {
				public void prepare(MimeMessage mimeMessage) throws Exception {
					MimeMessageHelper message = new MimeMessageHelper(mimeMessage, false, "UTF-8");
					
					message.setFrom(sender);
					message.addTo(recipient);
					message.setSubject(subject);
					message.setText(body);
				}
			});
		} catch (MailSendException e) {
			throw new MailException(e);
		}
	}
	
	public static class FormattedPupilBooking {
		private PupilBooking pupilBooking;

		private String formattedDate;

		public String getFormattedDate() {
			return formattedDate;
		}

		public void setFormattedDate(String formattedDate) {
			this.formattedDate = formattedDate;
		}

		public PupilBooking getPupilBooking() {
			return pupilBooking;
		}

		public void setPupilBooking(PupilBooking pupilBooking) {
			this.pupilBooking = pupilBooking;
		}
	}
	
}
