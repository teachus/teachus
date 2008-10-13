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
package dk.teachus.backend.domain.impl;

import java.util.List;

import org.joda.time.DateTime;

import dk.teachus.backend.domain.Booking;
import dk.teachus.backend.domain.Bookings;
import dk.teachus.backend.domain.Period;

public class BookingsImpl implements Bookings {
	private static final long serialVersionUID = 1L;

	private List<Booking> bookings;
	
	public BookingsImpl(List<Booking> pupilBookings) {
		bookings = pupilBookings;
	}

	public Booking getBooking(DateTime time) {
		Booking booking = null;
		
		for (Booking foundBooking : bookings) {
			DateTime dt1 = foundBooking.getDate().getDateTime();
			DateTime dt2 = time;
			
			if (dt1.toDateMidnight().equals(dt2.toDateMidnight())) {
				if (dt1.getHourOfDay() == dt2.getHourOfDay()
						&& dt1.getMinuteOfHour() == dt2.getMinuteOfHour()) {
					booking = foundBooking;
					break;
				}
			}
		}
		
		return booking;
	}

	public List<Booking> getBookingList() {
		return bookings;
	}
	
	public boolean mayBook(Period period, DateTime time) {
		boolean mayBook = false;
		
		{
			boolean conflicts = false;
			
			for (Booking booking : bookings) {
				if (booking.getPeriod().getId().equals(period.getId())) {
					if (period.conflicts(booking.getDate().getDateTime(), time)) {
						conflicts = true;
						break;
					}
				}
			}
			
			mayBook = conflicts == false;
		}
		
		return mayBook;
	}
	
	public boolean isBeforeBooking(Period period, DateTime time) {
		boolean beforeBooking = false;
		
		{
			boolean inLesson = false;
			
			for (Booking booking : bookings) {
				if (booking.getPeriod().getId().equals(period.getId())) {
					if (period.inLesson(new DateTime(booking.getDate()), time)) {
						inLesson = true;
						break;
					}
				}
			}
			
			if (inLesson == false) {
				beforeBooking = true;
			}
		}
		
		return beforeBooking;
	}

}
