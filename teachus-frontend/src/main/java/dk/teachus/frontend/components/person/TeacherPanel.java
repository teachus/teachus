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
import dk.teachus.backend.domain.TeacherAttribute;
import dk.teachus.backend.domain.impl.TeacherAttributeImpl;
import dk.teachus.frontend.TeachUsSession;
import dk.teachus.frontend.components.form.CheckBoxElement;
import dk.teachus.frontend.components.form.DropDownElement;
import dk.teachus.frontend.components.form.FormPanel;
import dk.teachus.frontend.models.TeacherModel;
import dk.teachus.frontend.pages.persons.TeachersPage;

public class TeacherPanel extends PersonPanel {
	private static final long serialVersionUID = 1L;
	private TeacherAttribute teacherAttribute;
	
	public TeacherPanel(final String id, final TeacherModel teacherModel) {
		super(id, teacherModel);
	}
	
	@Override
	protected Class<TeachersPage> getPersonsPageClass() {
		return TeachersPage.class;
	}
	
	@Override
	protected boolean allowUserEditing(final Person loggedInPerson, final Person editPerson) {
		boolean allow = false;
		
		if (loggedInPerson instanceof Admin) {
			allow = true;
		} else if (loggedInPerson instanceof Teacher) {
			allow = loggedInPerson.getId().equals(editPerson.getId());
		}
		
		return allow;
	}
	
	@Override
	protected final void onSave(final Person person) {
		final Teacher teacher = (Teacher) person;
		
		teacherAttribute.setTeacher(teacher);
		
		TeachUsSession.get().saveNewTeacherAttribute(teacherAttribute);
		
		onSave(teacher);
	}
	
	protected void onSave(final Teacher teacher) {
		
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
	protected void appendElements(final FormPanel formPanel) {
		/*
		 * TimeZone
		 */
		final IModel<TimeZone> inputModel = new Model<TimeZone>() {
			private static final long serialVersionUID = 1L;
			
			@Override
			public TimeZone getObject() {
				return getTeacherAttribute().getTimeZone();
			}
			
			@Override
			public void setObject(final TimeZone timeZone) {
				getTeacherAttribute().setTimeZone(timeZone);
			}
		};
		
		formPanel.addElement(DropDownElement.createTimeZoneElement(TeachUsSession.get().getString("General.timeZone"), inputModel, true)); //$NON-NLS-1$
		
		/*
		 * In the calendar, use narrow calendar time span (from start period to end period)
		 */
		final IModel<Boolean> newCalendarNarrowTimesModel = new Model<Boolean>() {
			private static final long serialVersionUID = 1L;
			
			@Override
			public Boolean getObject() {
				return getTeacherAttribute().getCalendarNarrowTimes();
			}
			
			@Override
			public void setObject(final Boolean bool) {
				getTeacherAttribute().setCalendarNarrowTimes(bool);
			}
		};
		
		formPanel.addElement(new CheckBoxElement(TeachUsSession.get().getString("TeacherPanel.onlyShowPeriodsInNewCalendar"), newCalendarNarrowTimesModel)); //$NON-NLS-1$
	}
	
	public Teacher getModelObject() {
		return (Teacher) getDefaultModelObject();
	}
	
	private TeacherAttribute getTeacherAttribute() {
		if (teacherAttribute == null) {
			teacherAttribute = TeachUsSession.get().getTeacherAttribute(getModelObject());
		}
		if (teacherAttribute == null) {
			teacherAttribute = new TeacherAttributeImpl();
		}
		return teacherAttribute;
	}
	
}
