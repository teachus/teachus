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
package dk.teachus.backend.dao.hibernate;

import java.util.List;

import org.joda.time.DateTime;

import dk.teachus.backend.domain.TeacherStatistics;
import dk.teachus.backend.test.SpringTestCase;

public class TestStatisticsDAO extends SpringTestCase {
	private static final long serialVersionUID = 1L;

	public void testGetTeachers() {
		createTeacher();
		
		createTeacherBooking(2L, 2L, new DateTime(2007, 5, 15, 14, 0, 0, 0));
		
		List<TeacherStatistics> teachers = getStatisticsDAO().getTeachers();
		endTransaction();
		assertNotNull(teachers);
		
		assertEquals(2, teachers.size());
		
		TeacherStatistics teacherStatistics = teachers.get(0);
		
		assertEquals(3, teacherStatistics.getPeriodCount());
		assertEquals(22, teacherStatistics.getPupilCount());
		assertEquals(130, teacherStatistics.getPupilBookingCount());
		assertEquals(1, teacherStatistics.getTeacherBookingCount());
	}

}
