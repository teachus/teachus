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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.velocity.exception.VelocityException;

import dk.teachus.backend.bean.NotificationBean;
import dk.teachus.backend.bean.VelocityBean;
import dk.teachus.backend.dao.BookingDAO;
import dk.teachus.backend.dao.MessageDAO;
import dk.teachus.backend.dao.PersonDAO;
import dk.teachus.backend.domain.MessageState;
import dk.teachus.backend.domain.Pupil;
import dk.teachus.backend.domain.PupilBooking;
import dk.teachus.backend.domain.Teacher;
import dk.teachus.backend.domain.impl.MailMessage;
import dk.teachus.backend.domain.impl.MailMessage.Type;
import dk.teachus.utils.ClassUtils;

public class VelocityNotificationBean implements NotificationBean {
	private static final long serialVersionUID = 1L;
	private static final Log log = LogFactory.getLog(VelocityNotificationBean.class);
	
	public static class FormattedBooking {
		private String location;
		
		private String formattedDate;

		public String getLocation() {
			return location;
		}

		public void setLocation(String location) {
			this.location = location;
		}

		public String getFormattedDate() {
			return formattedDate;
		}

		public void setFormattedDate(String formattedDate) {
			this.formattedDate = formattedDate;
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
	
	private BookingDAO bookingDAO;
	private PersonDAO personDAO;
	private final VelocityBean velocityBean;
	private final MessageDAO messageDAO;

	public VelocityNotificationBean(BookingDAO bookingDAO, PersonDAO personDAO, VelocityBean velocityBean, MessageDAO messageDAO) {
		this.bookingDAO = bookingDAO;
		this.personDAO = personDAO;
		this.velocityBean = velocityBean;
		this.messageDAO = messageDAO;
	}

	public synchronized void sendTeacherNotificationMail() {
		log.info("Start sending notification mails to the teachers");
		
		List<Teacher> teachers = personDAO.getPersons(Teacher.class);
		
		for (Teacher teacher : teachers) {			
			List<PupilBooking> pupilBookings = bookingDAO.getTeacherNotificationBookings(teacher);
			
			if (pupilBookings.isEmpty() == false) {
				if (log.isDebugEnabled()) {
					log.debug("Sending mails with booking count: "+pupilBookings.size()+" for teacher: "+teacher.getName());
				}
				
				// Create message to the teacher
				MailMessage message = new MailMessage();
		
				// Sender and recipient
				message.setSender(teacher);
				message.setRecipient(teacher);
				
				// The locale
				Locale locale = teacher.getLocale();
				
				// Build up bookingslist and format date
				List<VelocityNotificationBean.FormattedPupilBooking> pupilBookingList = new ArrayList<VelocityNotificationBean.FormattedPupilBooking>();
				for (PupilBooking pupilBooking : pupilBookings) {
					VelocityNotificationBean.FormattedPupilBooking formattedPupilBooking = new VelocityNotificationBean.FormattedPupilBooking();
					formattedPupilBooking.setPupilBooking(pupilBooking);
					SimpleDateFormat dateFormat = new SimpleDateFormat("EE, d. MMM yyyy H:mm", locale);
					formattedPupilBooking.setFormattedDate(dateFormat.format(pupilBooking.getDate()));
					pupilBookingList.add(formattedPupilBooking);
				}
				
				// Load properties
				ResourceBundle bundle = ResourceBundle.getBundle(ClassUtils.getAsResourceBundlePath(VelocityNotificationBean.class, "NewBookingsMail"), locale);	
			
				// Subject
				message.setSubject(bundle.getString("subject"));
				
				// Parse the template
				Map<String, Object> model = new HashMap<String, Object>();
				model.put("followingPupilsHasBookings", bundle.getString("followingPupilsHasBookings"));
				model.put("pupilHeader", bundle.getString("pupilHeader"));
				model.put("dateHeader", bundle.getString("dateHeader"));
				model.put("pupilBookingList", pupilBookingList);
				String template = "";
				try {
					template = velocityBean.mergeTemplate(ClassUtils.getAsResourcePath(VelocityNotificationBean.class, "NewBookingsMail.vm"), model);
				} catch (VelocityException e) {
					throw new RuntimeException(e);
				}
								
				// Text
				message.setBody(template);
				message.setType(Type.HTML);
				message.setState(MessageState.FINAL);
				
				messageDAO.save(message);
			}

			// Send mails
			bookingDAO.teacherNotificationMailSent(pupilBookings);
		}
	}
	
	public synchronized void sendPupilNotificationMail() {
		log.info("Start sending notification mails to the pupil");
		
		Map<Pupil, List<PupilBooking>> pupilNotificationBookings = bookingDAO.getPupilNotificationBookings();
		
		if (pupilNotificationBookings.isEmpty() == false) {
			for (Pupil pupil : pupilNotificationBookings.keySet()) {				
				List<PupilBooking> bookings = pupilNotificationBookings.get(pupil);

				if (log.isDebugEnabled()) {
					log.debug("Sending mail with booking count: "+bookings.size()+" for pupil: "+pupil.getName());
				}
				
				// Create message to the teacher
				MailMessage message = new MailMessage();
		
				// Sender and recipient
				message.setSender(pupil.getTeacher());
				message.setRecipient(pupil);
				
				// The locale
				Locale locale = pupil.getTeacher().getLocale();
				
				// Build up bookingslist and format date
				List<FormattedBooking> bookingList = new ArrayList<FormattedBooking>();
				for (PupilBooking pupilBooking : bookings) {
					FormattedBooking formattedBooking = new FormattedBooking();
					formattedBooking.setLocation(pupilBooking.getPeriod().getLocation());
					SimpleDateFormat dateFormat = new SimpleDateFormat("EE, d. MMM yyyy H:mm", locale);
					formattedBooking.setFormattedDate(dateFormat.format(pupilBooking.getDate()));
					bookingList.add(formattedBooking);
				}
				
				// Load properties
				ResourceBundle bundle = ResourceBundle.getBundle(ClassUtils.getAsResourceBundlePath(VelocityNotificationBean.class, "NewBookingsMail"), locale);	
			
				// Subject
				message.setSubject(bundle.getString("subject"));
				
				// Parse the template
				Map<String, Object> model = new HashMap<String, Object>();
				model.put("newBookings", bundle.getString("newBookings"));
				model.put("locationHeader", bundle.getString("locationHeader"));
				model.put("dateHeader", bundle.getString("dateHeader"));
				model.put("bookingList", bookingList);
				model.put("regard", bundle.getString("regard"));
				model.put("from", pupil.getTeacher().getName());
				
				String template = "";
				try {
					template = velocityBean.mergeTemplate(ClassUtils.getAsResourcePath(VelocityNotificationBean.class, "PupilNotificationMail.vm"), model);
				} catch (VelocityException e) {
					throw new RuntimeException(e);
				}
								
				// Text
				message.setBody(template);
				message.setType(Type.HTML);
				message.setState(MessageState.FINAL);
				
				messageDAO.save(message);
			}
			
			bookingDAO.pupilNotificationMailSent(pupilNotificationBookings);
		}
	}

}
