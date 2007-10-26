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

import java.io.Serializable;

import dk.teachus.backend.domain.Teacher;
import dk.teachus.backend.domain.TeacherStatistics;


public class TeacherStatisticsImpl implements Serializable, TeacherStatistics {
	private static final long serialVersionUID = 1L;

	private Teacher teacher;

	private long pupilCount;

	private long periodCount;

	private long pupilBookingCount;

	private long teacherBookingCount;

	public TeacherStatisticsImpl(Teacher teacher, long pupilCount, long periodCount, long pupilBookingCount, long teacherBookingCount) {
		this.teacher = teacher;
		this.pupilCount = pupilCount;
		this.periodCount = periodCount;
		this.pupilBookingCount = pupilBookingCount;
		this.teacherBookingCount = teacherBookingCount;
	}

	public long getTotalBookingCount() {
		return pupilBookingCount + teacherBookingCount;
	}

	public long getPeriodCount() {
		return periodCount;
	}

	public void setPeriodCount(long periodCount) {
		this.periodCount = periodCount;
	}

	public long getPupilBookingCount() {
		return pupilBookingCount;
	}

	public void setPupilBookingCount(long pupilBookingCount) {
		this.pupilBookingCount = pupilBookingCount;
	}

	public long getPupilCount() {
		return pupilCount;
	}

	public void setPupilCount(long pupilCount) {
		this.pupilCount = pupilCount;
	}

	public Teacher getTeacher() {
		return teacher;
	}

	public void setTeacher(Teacher teacher) {
		this.teacher = teacher;
	}

	public long getTeacherBookingCount() {
		return teacherBookingCount;
	}

	public void setTeacherBookingCount(long teacherBookingCount) {
		this.teacherBookingCount = teacherBookingCount;
	}

}