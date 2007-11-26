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

import org.apache.velocity.exception.VelocityException;

import dk.teachus.backend.bean.NewBookings;
import dk.teachus.backend.bean.VelocityBean;
import dk.teachus.backend.dao.BookingDAO;
import dk.teachus.backend.dao.MessageDAO;
import dk.teachus.backend.dao.PersonDAO;
import dk.teachus.backend.domain.Pupil;
import dk.teachus.backend.domain.PupilBooking;
import dk.teachus.backend.domain.Teacher;
import dk.teachus.backend.domain.impl.MailMessage;
import dk.teachus.backend.domain.impl.MailMessage.Type;
import dk.teachus.utils.ClassUtils;

public class NewBookingsImpl implements NewBookings {
	private static final long serialVersionUID = 1L;
	
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

	public NewBookingsImpl(BookingDAO bookingDAO, PersonDAO personDAO, VelocityBean velocityBean, MessageDAO messageDAO) {
		this.bookingDAO = bookingDAO;
		this.personDAO = personDAO;
		this.velocityBean = velocityBean;
		this.messageDAO = messageDAO;
	}

	public synchronized void sendNewBookingsMail() {
		List<Teacher> teachers = personDAO.getPersons(Teacher.class);
		
		for (Teacher teacher : teachers) {
			List<PupilBooking> pupilBookings = bookingDAO.getUnsentBookings(teacher);
			
			if (pupilBookings.isEmpty() == false) {
				// Create message to the teacher
				MailMessage message = new MailMessage();
		
				// Sender and recipient
				message.setSender(teacher);
				message.addRecipient(teacher);
				
				// The locale
				Locale locale = teacher.getLocale();
				
				// Build up bookingslist and format date
				List<NewBookingsImpl.FormattedPupilBooking> pupilBookingList = new ArrayList<NewBookingsImpl.FormattedPupilBooking>();
				for (PupilBooking pupilBooking : pupilBookings) {
					NewBookingsImpl.FormattedPupilBooking formattedPupilBooking = new NewBookingsImpl.FormattedPupilBooking();
					formattedPupilBooking.setPupilBooking(pupilBooking);
					SimpleDateFormat dateFormat = new SimpleDateFormat("EE, d. MMM yyyy H:mm", locale);
					formattedPupilBooking.setFormattedDate(dateFormat.format(pupilBooking.getDate()));
					pupilBookingList.add(formattedPupilBooking);
				}
				
				// Load properties
				ResourceBundle bundle = ResourceBundle.getBundle(ClassUtils.getAsResourceBundlePath(NewBookingsImpl.class, "NewBookingsMail"), locale);	
			
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
					template = velocityBean.mergeTemplate(ClassUtils.getAsResourcePath(NewBookingsImpl.class, "NewBookingsMail.vm"), model);
				} catch (VelocityException e) {
					throw new RuntimeException(e);
				}
								
				// Text
				message.setBody(template);
				message.setType(Type.HTML);
				
				messageDAO.save(message);
			}

			// Send mails
			bookingDAO.newBookingsMailSent(pupilBookings);
		}
	}
	
	public synchronized void sendPupilNotificationMail() {
		Map<Pupil, List<PupilBooking>> pupilNotificationBookings = bookingDAO.getPupilNotificationBookings();
		
		if (pupilNotificationBookings.isEmpty() == false) {
			for (Pupil pupil : pupilNotificationBookings.keySet()) {
				List<PupilBooking> bookings = pupilNotificationBookings.get(pupil);
				
				// Create message to the teacher
				MailMessage message = new MailMessage();
		
				// Sender and recipient
				message.setSender(pupil.getTeacher());
				message.addRecipient(pupil);
				
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
				ResourceBundle bundle = ResourceBundle.getBundle(ClassUtils.getAsResourceBundlePath(NewBookingsImpl.class, "NewBookingsMail"), locale);	
			
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
					template = velocityBean.mergeTemplate(ClassUtils.getAsResourcePath(NewBookingsImpl.class, "PupilNotificationMail.vm"), model);
				} catch (VelocityException e) {
					throw new RuntimeException(e);
				}
								
				// Text
				message.setBody(template);
				message.setType(Type.HTML);
				
				messageDAO.save(message);
			}
			
			bookingDAO.pupilNotificationMailSent(pupilNotificationBookings);
		}
	}

}
