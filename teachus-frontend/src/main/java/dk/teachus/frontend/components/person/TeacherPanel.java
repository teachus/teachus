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

import dk.teachus.backend.dao.PersonDAO;
import dk.teachus.backend.domain.Admin;
import dk.teachus.backend.domain.Person;
import dk.teachus.backend.domain.Teacher;
import dk.teachus.backend.domain.impl.TimeZoneAttribute;
import dk.teachus.frontend.TeachUsApplication;
import dk.teachus.frontend.TeachUsSession;
import dk.teachus.frontend.components.form.DropDownElement;
import dk.teachus.frontend.components.form.FormPanel;
import dk.teachus.frontend.models.TeacherModel;
import dk.teachus.frontend.pages.persons.TeachersPage;

public class TeacherPanel extends PersonPanel {
	private static final long serialVersionUID = 1L;
	private TimeZoneAttribute attribute;

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
		
		attribute.setTeacher(teacher);
		
		PersonDAO personDAO = TeachUsApplication.get().getPersonDAO();
		personDAO.saveAttribute(attribute);
		
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
		IModel inputModel = new Model() {
			private static final long serialVersionUID = 1L;
			
			@Override
			public Object getObject() {
				Teacher teacher = (Teacher) getModelObject();
				
				if (attribute == null && teacher.getId() != null) {
					PersonDAO personDAO = TeachUsApplication.get().getPersonDAO();
					attribute = personDAO.getAttribute(TimeZoneAttribute.class, teacher);
				}
				
				if (attribute == null) {
					attribute = new TimeZoneAttribute();
				}
				
				return attribute.getTimeZone();
			}
			
			@Override
			public void setObject(Object object) {
				if (object != null) {
					TimeZone timeZone = (TimeZone) object;
					attribute.setTimeZone(timeZone);
				} else { 
					attribute.setValue(null);
				}
			}
		};
		
		formPanel.addElement(DropDownElement.createTimeZoneElement(TeachUsSession.get().getString("General.timeZone"), inputModel, true));
	}

}
