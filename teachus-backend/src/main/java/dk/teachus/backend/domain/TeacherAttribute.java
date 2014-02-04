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
package dk.teachus.backend.domain;

import java.io.Serializable;
import java.util.TimeZone;

public interface TeacherAttribute extends Serializable {
	
	public static enum TeacherAttributeProperty {
		WELCOME_INTRODUCTION, TIMEZONE, CALENDAR_NARROW_TIMES
	}
	
	public static interface ValueChangeListener extends Serializable {
		void onValueChanged(TeacherAttributeProperty teacherAttributeProperty, String oldValue, String newValue);
	}
	
	Long getId();
	
	Teacher getTeacher();
	
	void setTeacher(Teacher teacher);
	
	String getWelcomeIntroduction();
	
	void setWelcomeIntroduction(String welcomeIntroduction);
	
	TimeZone getTimeZone();
	
	void setTimeZone(TimeZone timeZone);
	
	boolean getCalendarNarrowTimes();
	
	void setCalendarNarrowTimes(boolean calendarNarrowTimes);
	
	void addValueChangeListener(ValueChangeListener valueChangeListener);
	
	void removeValueChangeListener(ValueChangeListener valueChangeListener);
	
}
