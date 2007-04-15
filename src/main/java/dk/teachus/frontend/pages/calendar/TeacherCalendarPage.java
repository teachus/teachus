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

import java.util.Date;
import java.util.List;

import org.joda.time.DateMidnight;

import wicket.Application;
import wicket.RestartResponseAtInterceptPageException;
import wicket.markup.html.link.Link;
import dk.teachus.dao.BookingDAO;
import dk.teachus.dao.PeriodDAO;
import dk.teachus.domain.Bookings;
import dk.teachus.domain.DatePeriod;
import dk.teachus.domain.Period;
import dk.teachus.domain.Periods;
import dk.teachus.domain.Teacher;
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
		this(new DateMidnight());
	}
	
	public TeacherCalendarPage(DateMidnight pageDate) {
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
			protected Link createBackLink(String wicketId, final DateMidnight previousWeekDate, final int numberOfWeeks) {
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
			protected Link createForwardLink(String wicketId, final DateMidnight nextWeekDate) {
				return new Link(wicketId) {
					private static final long serialVersionUID = 1L;
		
					@Override
					public void onClick() {
						getRequestCycle().setResponsePage(new TeacherCalendarPage(nextWeekDate));
					}			
				};
			}
			
			@Override
			protected void onIntervalDetermined(List<DatePeriod> dates, DateMidnight firstDate, DateMidnight lastDate) {
				BookingDAO bookingDAO = TeachUsApplication.get().getBookingDAO();
				bookings = bookingDAO.getBookings(teacher, firstDate, lastDate);
			}

			@Override
			protected PeriodDateComponent createPeriodDateComponent(String wicketId, Period period, Date date) {
				DateMidnight dateMidnight = new DateMidnight(date);
				
				return new TeacherPeriodDateComponent(wicketId, teacher, period, dateMidnight, bookings);
			}
			
			@Override
			protected void navigationDateSelected(DateMidnight date) {
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
