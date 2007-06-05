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

import java.util.List;

import dk.teachus.backend.bean.MailBean;
import dk.teachus.backend.bean.NewBookings;
import dk.teachus.backend.dao.BookingDAO;
import dk.teachus.backend.dao.PersonDAO;
import dk.teachus.backend.domain.PupilBooking;
import dk.teachus.backend.domain.Teacher;

public class NewBookingsImpl implements NewBookings {
	private static final long serialVersionUID = 1L;
	
	private BookingDAO bookingDAO;
	private PersonDAO personDAO;
	private MailBean mailBean;

	public NewBookingsImpl(BookingDAO bookingDAO, PersonDAO personDAO, MailBean mailBean) {
		this.bookingDAO = bookingDAO;
		this.personDAO = personDAO;
		this.mailBean = mailBean;
	}

	public void sendNewBookingsMail() {
		List<Teacher> teachers = personDAO.getPersons(Teacher.class);
		
		for (Teacher teacher : teachers) {
			List<PupilBooking> pupilBookings = bookingDAO.getUnsentBookings(teacher);
			
			mailBean.sendNewBookingsMail(teacher, pupilBookings);
			
			bookingDAO.newBookingsMailSent(pupilBookings);
		}
	}

}
