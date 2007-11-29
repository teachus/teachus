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
package dk.teachus.frontend.pages.persons;

import java.util.ArrayList;
import java.util.List;

import dk.teachus.backend.dao.PersonDAO;
import dk.teachus.backend.domain.Teacher;
import dk.teachus.frontend.TeachUsApplication;
import dk.teachus.frontend.TeachUsSession;
import dk.teachus.frontend.UserLevel;
import dk.teachus.frontend.components.FunctionsColumn.FunctionItem;
import dk.teachus.frontend.models.TeacherModel;

public class TeachersPage extends PersonsPage<Teacher> {
	private static final long serialVersionUID = 1L;

	public TeachersPage() {
		super(UserLevel.ADMIN);
	}

	@Override
	protected String getPageLabel() {
		return TeachUsSession.get().getString("General.teachers"); //$NON-NLS-1$
	}

	@Override
	protected List<Teacher> getPersons() {
		PersonDAO personDAO = TeachUsApplication.get().getPersonDAO();
		return personDAO.getPersons(Teacher.class);
	}

	@Override
	protected String getNewPersonLabel() {
		return TeachUsSession.get().getString("TeachersPage.newTeacher"); //$NON-NLS-1$
	}

	@Override
	protected boolean showNewPersonLink() {
		return true;
	}
	
	@Override
	protected TeacherPage getPersonPage(Long personId) {
		return new TeacherPage(new TeacherModel(personId));
	}

	@Override
	protected AuthenticatedPageCategory getPageCategory() {
		return AuthenticatedPageCategory.TEACHERS;
	}
	
	@Override
	protected List<FunctionItem> getFunctions() {
		List<FunctionItem> functions = new ArrayList<FunctionItem>();
		
		// Activate/Inactivate
		functions.add(new FunctionItem() {
			private static final long serialVersionUID = 1L;

			@Override
			public String getLabel(Object object) {
				Teacher teacher = (Teacher) object;
				String label = null;
				
				if (teacher.isActive()) {
					label = TeachUsSession.get().getString("General.inactivate"); //$NON-NLS-1$
				} else {
					label = TeachUsSession.get().getString("General.activate"); //$NON-NLS-1$
				}
				
				return label;
			}
			
			@Override
			public void onEvent(Object object) {
				Teacher teacher = (Teacher) object;
				
				TeachUsApplication.get().getPersonDAO().changeActiveState(teacher.getId());
				
				getRequestCycle().setResponsePage(TeachersPage.class);
			}
		});
		
		// Delete
		functions.add(new FunctionItem(TeachUsSession.get().getString("General.delete")) { //$NON-NLS-1$
			private static final long serialVersionUID = 1L;

			@Override
			public void onEvent(Object object) {
				Teacher teacher = (Teacher) object;
				
				PersonDAO personDAO = TeachUsApplication.get().getPersonDAO();
				personDAO.deleteTeacher(teacher);
				
				getRequestCycle().setResponsePage(TeachersPage.class);
			}
			
			@Override
			public String getClickConfirmText(Object object) {
				Teacher teacher = (Teacher) object;
				String deleteText = TeachUsSession.get().getString("TeachersPage.deleteConfirm"); //$NON-NLS-1$
				deleteText = deleteText.replace("{personname}", teacher.getName()); //$NON-NLS-1$
				return deleteText;
			}
		});
		
		// Login
		functions.add(new FunctionItem("Login") {
			private static final long serialVersionUID = 1L;

			@Override
			public void onEvent(Object object) {
				Teacher teacher = (Teacher) object;
				
				TeachUsSession.get().setAuthenticatedPerson(teacher);
				
				getRequestCycle().setResponsePage(TeachUsApplication.get().getHomePage());
			}
			
			@Override
			public String getClickConfirmText(Object object) {
				Teacher teacher = (Teacher) object;
				String loginText = "Are you sure you wish to login as the teacher: '{personname}'? This should only be used when requested by the teacher.";
				loginText = loginText.replace("{personname}", teacher.getName());
				return loginText;
			}
		});
		
		return functions;
	}

}
