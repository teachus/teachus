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

import java.util.HashSet;
import java.util.Set;
import java.util.TimeZone;

import javax.jdo.annotations.Extension;
import javax.jdo.annotations.NotPersistent;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;

import dk.teachus.backend.domain.Teacher;
import dk.teachus.backend.domain.TeacherAttribute;

@PersistenceCapable(table = "teacher_attribute")
public class TeacherAttributeImpl extends AbstractJdoObject implements TeacherAttribute {
	private static final long serialVersionUID = 1L;
	
	@NotPersistent
	private Set<ValueChangeListener> valueChangeListeners;
	
	@Persistent(column = "teacher_id")
	@Extension(vendorName = "datanucleus", key = "implementation-classes", value = "dk.teachus.backend.domain.impl.TeacherImpl")
	private Teacher teacher;
	
	@Persistent(column = "welcome_introduction")
	private String welcomeIntroduction;
	
	@Persistent(column = "timezone")
	private String timeZone;
	
	@Persistent(column = "calendar_narrow_times")
	private boolean calendarNarrowTimes;
	
	@Override
	public Teacher getTeacher() {
		return teacher;
	}
	
	@Override
	public void setTeacher(final Teacher teacher) {
		this.teacher = teacher;
	}
	
	@Override
	public String getWelcomeIntroduction() {
		return welcomeIntroduction;
	}
	
	@Override
	public void setWelcomeIntroduction(final String welcomeIntroduction) {
		final String oldValue = this.welcomeIntroduction;
		this.welcomeIntroduction = welcomeIntroduction;
		
		fireValueChanged(TeacherAttributeProperty.WELCOME_INTRODUCTION, oldValue, welcomeIntroduction);
	}
	
	@Override
	public TimeZone getTimeZone() {
		return timeZone != null ? TimeZone.getTimeZone(timeZone) : null;
	}
	
	@Override
	public void setTimeZone(final TimeZone timeZone) {
		final String oldValue = this.timeZone;
		if (timeZone != null) {
			this.timeZone = timeZone.getID();
		} else {
			this.timeZone = null;
		}
		
		fireValueChanged(TeacherAttributeProperty.TIMEZONE, oldValue, this.timeZone);
	}
	
	@Override
	public boolean getCalendarNarrowTimes() {
		return calendarNarrowTimes;
	}
	
	@Override
	public void setCalendarNarrowTimes(final boolean calendarNarrowTimes) {
		final String oldValue = Boolean.toString(this.calendarNarrowTimes);
		this.calendarNarrowTimes = calendarNarrowTimes;
		
		fireValueChanged(TeacherAttributeProperty.CALENDAR_NARROW_TIMES, oldValue, Boolean.toString(calendarNarrowTimes));
	}
	
	public Set<ValueChangeListener> getValueChangeListeners() {
		if (valueChangeListeners == null) {
			valueChangeListeners = new HashSet<ValueChangeListener>();
		}
		
		return valueChangeListeners;
	}
	
	@Override
	public void addValueChangeListener(final ValueChangeListener valueChangeListener) {
		getValueChangeListeners().add(valueChangeListener);
	}
	
	@Override
	public void removeValueChangeListener(final ValueChangeListener valueChangeListener) {
		getValueChangeListeners().remove(valueChangeListener);
	}
	
	protected void fireValueChanged(final TeacherAttributeProperty property, final String oldValue, final String newValue) {
		for (final ValueChangeListener valueChangeListener : getValueChangeListeners()) {
			valueChangeListener.onValueChanged(property, oldValue, newValue);
		}
	}
	
}
