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

import org.apache.wicket.Application;
import org.apache.wicket.RestartResponseAtInterceptPageException;
import org.apache.wicket.markup.html.link.Link;
import org.joda.time.DateMidnight;

import dk.teachus.backend.dao.BookingDAO;
import dk.teachus.backend.dao.PeriodDAO;
import dk.teachus.backend.domain.Bookings;
import dk.teachus.backend.domain.DatePeriod;
import dk.teachus.backend.domain.Period;
import dk.teachus.backend.domain.Periods;
import dk.teachus.backend.domain.TeachUsDate;
import dk.teachus.backend.domain.Teacher;
import dk.teachus.frontend.TeachUsApplication;
import dk.teachus.frontend.TeachUsSession;
import dk.teachus.frontend.UserLevel;
import dk.teachus.frontend.components.calendar.CalendarPanel;
import dk.teachus.frontend.components.calendar.PeriodDateComponent;
import dk.teachus.frontend.components.calendar.TeacherPeriodDateComponent;
import dk.teachus.frontend.pages.AuthenticatedBasePage;

public class TeacherCalendarPage extends AuthenticatedBasePage {
	private static final long serialVersionUID = 1L;

	private Teacher teacher;
	
	public TeacherCalendarPage() {
		this(TeachUsSession.get().createNewDate(new DateMidnight()));
	}
	
	public TeacherCalendarPage(TeachUsDate pageDate) {
		super(UserLevel.TEACHER, true);
		
		if (TeachUsSession.get().getUserLevel() != UserLevel.TEACHER) {
			throw new RestartResponseAtInterceptPageException(Application.get().getHomePage());
		}
		
		PeriodDAO periodDAO = TeachUsApplication.get().getPeriodDAO();
		
		teacher = (Teacher) TeachUsSession.get().getPerson();		
		
		final Periods periods = periodDAO.getPeriods(teacher);
		
		add(new CalendarPanel("calendar", pageDate, periods) { //$NON-NLS-1$
			private static final long serialVersionUID = 1L;
			private Bookings bookings;

			@Override
			protected Link createBackLink(String wicketId, final TeachUsDate previousWeekDate, final int numberOfWeeks) {
				return new Link(wicketId) {
					private static final long serialVersionUID = 1L;
		
					@Override
					public void onClick() {
						getRequestCycle().setResponsePage(new TeacherCalendarPage(previousWeekDate));
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
						getRequestCycle().setResponsePage(new TeacherCalendarPage(nextWeekDate));
					}			
				};
			}
			
			@Override
			protected void onIntervalDetermined(List<DatePeriod> dates, TeachUsDate firstDate, TeachUsDate lastDate) {
				BookingDAO bookingDAO = TeachUsApplication.get().getBookingDAO();
				bookings = bookingDAO.getBookings(teacher, firstDate, lastDate);
			}

			@Override
			protected PeriodDateComponent createPeriodDateComponent(String wicketId, Period period, TeachUsDate date) {
				return new TeacherPeriodDateComponent(wicketId, teacher, period, date, bookings);
			}
			
			@Override
			protected void navigationDateSelected(TeachUsDate date) {
				getRequestCycle().setResponsePage(new TeacherCalendarPage(date));
			}
			
		});
	}

	@Override
	protected String getPageLabel() {
		return TeachUsSession.get().getString("General.calendarFor")+" "+teacher.getName(); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Override
	protected AuthenticatedPageCategory getPageCategory() {
		return AuthenticatedPageCategory.CALENDAR;
	}

}
