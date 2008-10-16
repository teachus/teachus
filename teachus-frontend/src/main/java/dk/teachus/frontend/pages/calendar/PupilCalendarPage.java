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
package dk.teachus.frontend.pages.calendar;

import java.util.List;

import org.apache.wicket.markup.html.link.Link;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;

import dk.teachus.backend.dao.BookingDAO;
import dk.teachus.backend.dao.PeriodDAO;
import dk.teachus.backend.domain.Bookings;
import dk.teachus.backend.domain.DatePeriod;
import dk.teachus.backend.domain.Period;
import dk.teachus.backend.domain.Periods;
import dk.teachus.backend.domain.Pupil;
import dk.teachus.backend.domain.TeachUsDate;
import dk.teachus.frontend.TeachUsApplication;
import dk.teachus.frontend.TeachUsSession;
import dk.teachus.frontend.UserLevel;
import dk.teachus.frontend.components.calendar.CalendarPanel;
import dk.teachus.frontend.components.calendar.PeriodDateComponent;
import dk.teachus.frontend.components.calendar.PupilPeriodDateComponent;
import dk.teachus.frontend.pages.AuthenticatedBasePage;
import dk.teachus.frontend.utils.Formatters;


public class PupilCalendarPage extends AuthenticatedBasePage {
	private static final long serialVersionUID = 1L;
	
	private Pupil pupil;
	
	public PupilCalendarPage() {
		this(null);
	}
	
	public PupilCalendarPage(Pupil pupil) {
		this(TeachUsSession.get().createNewDate(new DateTime()), pupil);
	}
	
	public PupilCalendarPage(TeachUsDate pageDate, Pupil pupil) {
		super(UserLevel.PUPIL);
		
		if (pupil == null) {
			pupil = (Pupil) TeachUsSession.get().getPerson();
		}
		
		this.pupil = pupil;
				
		DateTimeFormatter formatIsoDate = Formatters.getFormatIsoDate();
		initializePupilCalendar(pageDate, formatIsoDate, pupil);
	}
	

	private void initializePupilCalendar(TeachUsDate pageDate, DateTimeFormatter formatIsoDate, final Pupil pupil) {		
		PeriodDAO periodDAO = TeachUsApplication.get().getPeriodDAO();
		
		final Periods periods = periodDAO.getPeriods(pupil.getTeacher());
		
		add(new CalendarPanel("calendar", pageDate, periods) { //$NON-NLS-1$
			private static final long serialVersionUID = 1L;
			private Bookings bookings;

			@Override
			protected Link createBackLink(String wicketId, final TeachUsDate previousWeekDate, final int numberOfWeeks) {
				return new Link(wicketId) {
					private static final long serialVersionUID = 1L;
		
					@Override
					public void onClick() {
						getRequestCycle().setResponsePage(new PupilCalendarPage(previousWeekDate, pupil));
					}
					
					@Override
					public boolean isEnabled() {
						return numberOfWeeks > 0;
					}
				};
			}

			@Override
			protected Link createForwardLink(String wicketId, final TeachUsDate nextWeekDate) {
				return new Link(wicketId) {
					private static final long serialVersionUID = 1L;
		
					@Override
					public void onClick() {
						getRequestCycle().setResponsePage(new PupilCalendarPage(nextWeekDate, pupil));
					}			
				};
			}
			
			@Override
			protected void onIntervalDetermined(List<DatePeriod> dates, TeachUsDate firstDate, TeachUsDate lastDate) {
				BookingDAO bookingDAO = TeachUsApplication.get().getBookingDAO();
				bookings = bookingDAO.getBookings(pupil.getTeacher(), firstDate, lastDate);
			}

			@Override
			protected PeriodDateComponent createPeriodDateComponent(String wicketId, Period period, TeachUsDate date) {
				return new PupilPeriodDateComponent("period", pupil, period, date, bookings); //$NON-NLS-1$
			}
			
			@Override
			protected void navigationDateSelected(TeachUsDate date) {
				getRequestCycle().setResponsePage(new PupilCalendarPage(date, pupil));
			}
		});
	}
	
	@Override
	protected String getPageLabel() {
		return TeachUsSession.get().getString("General.calendarFor")+" "+pupil.getName(); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Override
	protected AuthenticatedPageCategory getPageCategory() {
		return AuthenticatedPageCategory.CALENDAR;
	}
	
}
