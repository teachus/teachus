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

import org.apache.wicket.Application;
import org.apache.wicket.RestartResponseAtInterceptPageException;
import org.joda.time.DateMidnight;

import dk.teachus.backend.dao.PeriodDAO;
import dk.teachus.backend.domain.Periods;
import dk.teachus.backend.domain.TeachUsDate;
import dk.teachus.backend.domain.Teacher;
import dk.teachus.frontend.TeachUsApplication;
import dk.teachus.frontend.TeachUsSession;
import dk.teachus.frontend.UserLevel;
import dk.teachus.frontend.components.calendar.TeacherCalendarPanel;
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
		
		teacher = TeachUsSession.get().getTeacher();
		
		PeriodDAO periodDAO = TeachUsApplication.get().getPeriodDAO();
		
		final Periods periods = periodDAO.getPeriods(teacher);
		
		add(new TeacherCalendarPanel("calendar", pageDate, periods));
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
