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
package dk.teachus.frontend.components.person;

import java.util.TimeZone;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import dk.teachus.backend.domain.Admin;
import dk.teachus.backend.domain.Person;
import dk.teachus.backend.domain.Teacher;
import dk.teachus.backend.domain.impl.CalendarNarrowTimesTeacherAttribute;
import dk.teachus.backend.domain.impl.TimeZoneAttribute;
import dk.teachus.frontend.TeachUsSession;
import dk.teachus.frontend.components.form.CheckBoxElement;
import dk.teachus.frontend.components.form.DropDownElement;
import dk.teachus.frontend.components.form.FormPanel;
import dk.teachus.frontend.models.TeacherModel;
import dk.teachus.frontend.pages.persons.TeachersPage;

public class TeacherPanel extends PersonPanel {
	private static final long serialVersionUID = 1L;
	private TimeZoneAttribute timeZoneAttribute;
	private CalendarNarrowTimesTeacherAttribute calendarNarrowTimesAttribute;

	public TeacherPanel(String id, TeacherModel teacherModel) {
		super(id, teacherModel);
	}
	
	@Override
	protected Class<TeachersPage> getPersonsPageClass() {
		return TeachersPage.class;
	}

	@Override
	protected boolean allowUserEditing(Person loggedInPerson, Person editPerson) {
		boolean allow = false;
		
		if (loggedInPerson instanceof Admin) {
			allow = true;
		} else if (loggedInPerson instanceof Teacher) {
			allow = loggedInPerson.getId().equals(editPerson.getId());
		}
		
		return allow;
	}
	
	@Override
	protected final void onSave(Person person) {
		Teacher teacher = (Teacher) person;
		
		timeZoneAttribute.setTeacher(teacher);
		calendarNarrowTimesAttribute.setTeacher(teacher);
		
		TeachUsSession.get().saveNewTeacherAttribute(timeZoneAttribute);
		TeachUsSession.get().saveNewTeacherAttribute(calendarNarrowTimesAttribute);
		
		onSave(teacher);
	}
	
	protected void onSave(Teacher teacher) {
		
	}
	
	@Override
	protected boolean isThemeVisible() {
		return true;
	}
	
	@Override
	protected boolean isCurrencyVisible() {
		return true;
	}
	
	@Override
	protected void appendElements(FormPanel formPanel) {
		/*
		 * TimeZone
		 */
		IModel<TimeZone> inputModel = new Model<TimeZone>() {
			private static final long serialVersionUID = 1L;
			
			@Override
			public TimeZone getObject() {
				Teacher teacher = (Teacher) getModelObject();
				
				if (timeZoneAttribute == null && teacher.getId() != null) {
					timeZoneAttribute = TeachUsSession.get().getTeacherAttribute(TimeZoneAttribute.class, teacher);
				}
				
				if (timeZoneAttribute == null) {
					timeZoneAttribute = new TimeZoneAttribute();
				}
				
				return timeZoneAttribute.getTimeZone();
			}
			
			@Override
			public void setObject(TimeZone timeZone) {
				if (timeZone != null) {
					timeZoneAttribute.setTimeZone(timeZone);
				} else { 
					timeZoneAttribute.setValue(null);
				}
			}
		};
		
		formPanel.addElement(DropDownElement.createTimeZoneElement(TeachUsSession.get().getString("General.timeZone"), inputModel, true)); //$NON-NLS-1$
		
		/*
		 * In the calendar, use narrow calendar time span (from start period to end period)
		 */
		IModel<Boolean> newCalendarNarrowTimesModel = new Model<Boolean>() {
			private static final long serialVersionUID = 1L;
			
			@Override
			public Boolean getObject() {
				Teacher teacher = (Teacher) getModelObject();
				
				if (calendarNarrowTimesAttribute == null && teacher.getId() != null) {
					calendarNarrowTimesAttribute = TeachUsSession.get().getTeacherAttribute(CalendarNarrowTimesTeacherAttribute.class, teacher);
				}
				
				if (calendarNarrowTimesAttribute == null) {
					calendarNarrowTimesAttribute = new CalendarNarrowTimesTeacherAttribute();
				}
				
				return calendarNarrowTimesAttribute.getBooleanValue();
			}
			
			@Override
			public void setObject(Boolean bool) {
				if (bool != null) {
					calendarNarrowTimesAttribute.setBooleanValue(bool);
				} else { 
					calendarNarrowTimesAttribute.setValue(null);
				}
			}
		};
		
		formPanel.addElement(new CheckBoxElement(TeachUsSession.get().getString("TeacherPanel.onlyShowPeriodsInNewCalendar"), newCalendarNarrowTimesModel)); //$NON-NLS-1$
	}
	
	public Teacher getModelObject() {
		return (Teacher) getDefaultModelObject();
	}

}
