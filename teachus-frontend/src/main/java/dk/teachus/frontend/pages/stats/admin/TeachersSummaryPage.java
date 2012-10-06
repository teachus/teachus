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
package dk.teachus.frontend.pages.stats.admin;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.model.Model;

import dk.teachus.backend.domain.TeacherStatistics;
import dk.teachus.frontend.TeachUsApplication;
import dk.teachus.frontend.TeachUsSession;
import dk.teachus.frontend.components.list.ListPanel;

public class TeachersSummaryPage extends AbstractAdminStatisticsPage {
	private static final long serialVersionUID = 1L;

	public TeachersSummaryPage() {
		List<IColumn> columns = new ArrayList<IColumn>();
		columns.add(new PropertyColumn(new Model(TeachUsSession.get().getString("General.teacher")), "teacher.name")); //$NON-NLS-1$ //$NON-NLS-2$
		columns.add(new PropertyColumn(new Model(TeachUsSession.get().getString("TeachersSummaryPage.numberOfPupils")), "pupilCount")); //$NON-NLS-1$ //$NON-NLS-2$
		columns.add(new PropertyColumn(new Model(TeachUsSession.get().getString("TeachersSummaryPage.numberOfPeriods")), "periodCount")); //$NON-NLS-1$ //$NON-NLS-2$
		columns.add(new PropertyColumn(new Model(TeachUsSession.get().getString("TeachersSummaryPage.numberOfPupilBookings")), "pupilBookingCount")); //$NON-NLS-1$ //$NON-NLS-2$
		columns.add(new PropertyColumn(new Model(TeachUsSession.get().getString("TeachersSummaryPage.numberOfTeacherBookings")), "teacherBookingCount")); //$NON-NLS-1$ //$NON-NLS-2$
		columns.add(new PropertyColumn(new Model(TeachUsSession.get().getString("TeachersSummaryPage.numberOfBookings")), "totalBookingCount")); //$NON-NLS-1$ //$NON-NLS-2$

		
		
		List<TeacherStatistics> data = TeachUsApplication.get().getStatisticsDAO().getTeachers();

		add(new ListPanel("teachersSummary", columns, data)); //$NON-NLS-1$
	}

}
