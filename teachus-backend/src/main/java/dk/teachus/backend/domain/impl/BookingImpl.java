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

import dk.teachus.backend.domain.Booking;
import dk.teachus.backend.domain.Period;
import dk.teachus.backend.domain.TeachUsDate;
import dk.teachus.backend.domain.Teacher;

public abstract class BookingImpl extends AbstractHibernateObject implements Booking {
	private static final long serialVersionUID = 1L;

	private boolean active = true;
	
	private Period period;

	private TeachUsDate date;

	private TeachUsDate createDate;
	
	private TeachUsDate updateDate;

	private Teacher teacher;

	public TeachUsDate getCreateDate() {
		return createDate;
	}

	public TeachUsDate getDate() {
		return date;
	}

	public Period getPeriod() {
		return period;
	}

	public Teacher getTeacher() {
		return teacher;
	}

	public TeachUsDate getUpdateDate() {
		return updateDate;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public void setCreateDate(TeachUsDate createDate) {
		this.createDate = createDate;
	}

	public void setDate(TeachUsDate date) {
		this.date = date;
	}

	public void setPeriod(Period period) {
		this.period = period;
	}

	public void setTeacher(Teacher teacher) {
		this.teacher = teacher;
	}

	public void setUpdateDate(TeachUsDate updateDate) {
		this.updateDate = updateDate;
	}

}
