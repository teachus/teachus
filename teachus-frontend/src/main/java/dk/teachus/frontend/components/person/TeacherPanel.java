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
import dk.teachus.backend.domain.impl.NewCalendarNarrowTimesTeacherAttribute;
import dk.teachus.backend.domain.impl.NewCalendarTeacherAttribute;
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
	private NewCalendarTeacherAttribute newCalendarAttribute;
	private NewCalendarNarrowTimesTeacherAttribute newCalendarNarrowTimesAttribute;

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
		newCalendarAttribute.setTeacher(teacher);
		newCalendarNarrowTimesAttribute.setTeacher(teacher);
		
		TeachUsSession.get().saveNewTeacherAttribute(timeZoneAttribute);
		TeachUsSession.get().saveNewTeacherAttribute(newCalendarAttribute);
		TeachUsSession.get().saveNewTeacherAttribute(newCalendarNarrowTimesAttribute);
		
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
		 * Use new calendar
		 */
		IModel<Boolean> newCalendarModel = new Model<Boolean>() {
			private static final long serialVersionUID = 1L;
			
			@Override
			public Boolean getObject() {
				Teacher teacher = (Teacher) getModelObject();
				
				if (newCalendarAttribute == null && teacher.getId() != null) {
					newCalendarAttribute = TeachUsSession.get().getTeacherAttribute(NewCalendarTeacherAttribute.class, teacher);
				}
				
				if (newCalendarAttribute == null) {
					newCalendarAttribute = new NewCalendarTeacherAttribute();
				}
				
				return newCalendarAttribute.getBooleanValue();
			}
			
			@Override
			public void setObject(Boolean bool) {
				if (bool != null) {
					newCalendarAttribute.setBooleanValue(bool);
				} else { 
					newCalendarAttribute.setValue(null);
				}
			}
		};
		
		formPanel.addElement(new CheckBoxElement(TeachUsSession.get().getString("TeacherPanel.useNewCalendar"), newCalendarModel)); //$NON-NLS-1$
		
		/*
		 * In the new calendar, use narrow calendar time span (from start period to end period)
		 */
		IModel<Boolean> newCalendarNarrowTimesModel = new Model<Boolean>() {
			private static final long serialVersionUID = 1L;
			
			@Override
			public Boolean getObject() {
				Teacher teacher = (Teacher) getModelObject();
				
				if (newCalendarNarrowTimesAttribute == null && teacher.getId() != null) {
					newCalendarNarrowTimesAttribute = TeachUsSession.get().getTeacherAttribute(NewCalendarNarrowTimesTeacherAttribute.class, teacher);
				}
				
				if (newCalendarNarrowTimesAttribute == null) {
					newCalendarNarrowTimesAttribute = new NewCalendarNarrowTimesTeacherAttribute();
				}
				
				return newCalendarNarrowTimesAttribute.getBooleanValue();
			}
			
			@Override
			public void setObject(Boolean bool) {
				if (bool != null) {
					newCalendarNarrowTimesAttribute.setBooleanValue(bool);
				} else { 
					newCalendarNarrowTimesAttribute.setValue(null);
				}
			}
		};
		
		formPanel.addElement(new CheckBoxElement(TeachUsSession.get().getString("TeacherPanel.onlyShowPeriodsInNewCalendar"), newCalendarNarrowTimesModel)); //$NON-NLS-1$
	}
	
	public Teacher getModelObject() {
		return (Teacher) getDefaultModelObject();
	}

}
