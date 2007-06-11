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
package dk.teachus.frontend.components.calendar;

import java.util.Date;

import org.joda.time.DateMidnight;
import org.joda.time.DateTime;

import wicket.MarkupContainer;
import wicket.markup.html.link.Link;
import dk.teachus.backend.dao.BookingDAO;
import dk.teachus.backend.domain.Booking;
import dk.teachus.backend.domain.Bookings;
import dk.teachus.backend.domain.Period;
import dk.teachus.backend.domain.Pupil;
import dk.teachus.backend.domain.PupilBooking;
import dk.teachus.backend.domain.Teacher;
import dk.teachus.backend.domain.TeacherBooking;
import dk.teachus.frontend.pages.calendar.PupilCalendarPage;

public class TeacherPeriodDateComponent extends BookingPeriodDateComponent {
	private static final long serialVersionUID = 1L;

	private Teacher teacher;

	public TeacherPeriodDateComponent(String id, Teacher teacher, Period period, DateMidnight date, Bookings bookings) {
		super(id, period, date, bookings);
		
		this.teacher = teacher;
	}

	@Override
	protected Booking createNewBookingObject(BookingDAO bookingDAO) {
		TeacherBooking teacherBooking = bookingDAO.createTeacherBookingObject();
		teacherBooking.setTeacher(teacher);
		return teacherBooking;
	}

	@Override
	protected boolean isChangeable(Booking booking) {
		return booking instanceof TeacherBooking;
	}

	@Override
	protected boolean shouldDisplayStringInsteadOfOccupiedIcon() {
		return true;
	}

	@Override
	protected boolean mayChangeBooking(Period period, DateTime bookingStartTime) {
		return true;
	}
	
	@Override
	protected MarkupContainer getBookingDisplayStringLink(String linkId, Booking booking) {
		MarkupContainer displayLink = null;
		
		if (booking instanceof PupilBooking) {
			PupilBooking pupilBooking = (PupilBooking) booking;
			final Pupil pupil = pupilBooking.getPupil();
			final Date date = pupilBooking.getDate();
			
			displayLink = new Link(linkId) {
				private static final long serialVersionUID = 1L;

				@Override
				public void onClick() {
					getRequestCycle().setResponsePage(new PupilCalendarPage(date, pupil));
				}				
			};
		}
		
		return displayLink;
	}
	
	@Override
	protected BookingPeriodDateComponent createNewInstance(String id, Period period, DateMidnight date, Bookings bookings) {
		return new TeacherPeriodDateComponent(id, teacher, period, date, bookings);
	}
}