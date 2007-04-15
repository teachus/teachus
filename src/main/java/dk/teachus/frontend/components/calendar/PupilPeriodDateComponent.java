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

import org.joda.time.DateMidnight;
import org.joda.time.DateTime;

import dk.teachus.dao.BookingDAO;
import dk.teachus.domain.Booking;
import dk.teachus.domain.Bookings;
import dk.teachus.domain.Period;
import dk.teachus.domain.Pupil;
import dk.teachus.domain.PupilBooking;
import dk.teachus.frontend.TeachUsSession;
import dk.teachus.frontend.UserLevel;

public class PupilPeriodDateComponent extends BookingPeriodDateComponent {
	private static final long serialVersionUID = 1L;
	
	private Pupil pupil;

	public PupilPeriodDateComponent(String id, Pupil pupil, Period period, DateMidnight date, Bookings bookings) {
		super(id, period, date, bookings);
		
		this.pupil = pupil;
	}

	@Override
	protected Booking createNewBookingObject(BookingDAO bookingDAO) {
		PupilBooking pupilBooking = bookingDAO.createPupilBookingObject();
		pupilBooking.setPupil(pupil);
		return pupilBooking;
	}

	@Override
	protected boolean isChangeable(Booking booking) {
		boolean changeable = false;
		if (booking != null) {
			if (booking instanceof PupilBooking) {
				PupilBooking pupilBooking = (PupilBooking) booking;
				changeable = pupilBooking.getPupil().getId().equals(pupil.getId());
			}
		}
		return changeable;
	}

	@Override
	protected boolean shouldDisplayStringInsteadOfOccupiedIcon() {
		return false;
	}

	@Override
	protected boolean mayChangeBooking(Period period, DateTime bookingStartTime) {
		boolean mayChangeBooking = false;

		if (TeachUsSession.get().getUserLevel() == UserLevel.TEACHER) {
			mayChangeBooking = true;
		} else {
			DateTime today = new DateTime().withTime(23, 59, 59, 999);
			mayChangeBooking = bookingStartTime.isAfter(today);
		}

		return mayChangeBooking;
	}
	
	@Override
	protected BookingPeriodDateComponent createNewInstance(String id, Period period, DateMidnight date, Bookings bookings) {
		return new PupilPeriodDateComponent(id, pupil, period, date, bookings);
	}

}
