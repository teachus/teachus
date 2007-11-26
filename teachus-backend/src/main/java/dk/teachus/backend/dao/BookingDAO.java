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
package dk.teachus.backend.dao;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.joda.time.DateMidnight;

import dk.teachus.backend.domain.Booking;
import dk.teachus.backend.domain.Bookings;
import dk.teachus.backend.domain.Period;
import dk.teachus.backend.domain.Pupil;
import dk.teachus.backend.domain.PupilBooking;
import dk.teachus.backend.domain.Teacher;
import dk.teachus.backend.domain.TeacherBooking;

public interface BookingDAO extends Serializable {

	void book(Booking booking);
	
	PupilBooking createPupilBookingObject();

	TeacherBooking createTeacherBookingObject();

	void deleteBooking(Booking booking);

	List<PupilBooking> getFutureBookingsForTeacher(Teacher teacher);

	List<PupilBooking> getUnpaidBookings(Pupil pupil);

	List<PupilBooking> getUnpaidBookings(Teacher teacher);

	List<PupilBooking> getUnsentBookings(Teacher teacher);
	
	Map<Pupil, List<PupilBooking>> getPupilNotificationBookings();

	void newBookingsMailSent(List<PupilBooking> pupilBookings);
	
	void pupilNotificationMailSent(Map<Pupil, List<PupilBooking>> pupilBookings);

	void changePaidStatus(PupilBooking pupilBooking);

	List<PupilBooking> getPaidBookings(Teacher teacher, Date startDate, Date endDate);

	List<PupilBooking> getUnPaidBookings(Teacher teacher, Date fromDate, Date toDate);
	
	List<Integer> getYearsWithPaidBookings(Teacher teacher);

	List<Integer> getYearsWithBookings(Teacher teacher);

	Bookings getBookings(Teacher teacher, DateMidnight fromDate, DateMidnight toDate);

	Booking getBooking(Long id);

	int getBookingCount(Period period);

	Date getLastBookingDate(Period period);
	
}
