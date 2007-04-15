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

import java.util.List;

import dk.teachus.dao.PersonDAO;
import dk.teachus.domain.Admin;
import dk.teachus.frontend.TeachUsApplication;
import dk.teachus.frontend.TeachUsSession;
import dk.teachus.frontend.UserLevel;
import dk.teachus.frontend.models.AdminModel;

public class AdminsPage extends PersonsPage<Admin> {
	private static final long serialVersionUID = 1L;
	
	public AdminsPage() {
		super(UserLevel.ADMIN);
	}

	@Override
	protected String getPageLabel() {
		return TeachUsSession.get().getString("General.administrators"); //$NON-NLS-1$
	}

	@Override
	protected List<Admin> getPersons() {
		PersonDAO personDAO = TeachUsApplication.get().getPersonDAO();
		return personDAO.getPersons(Admin.class);
	}

	@Override
	protected String getNewPersonLabel() {
		return TeachUsSession.get().getString("AdminsPage.newAdministrator"); //$NON-NLS-1$
	}

	@Override
	protected boolean showNewPersonLink() {
		return true;
	}
	
	@Override
	protected PersonPage getPersonPage(Long personId) {
		return new AdminPage(new AdminModel(personId));
	}

	@Override
	protected AuthenticatedPageCategory getPageCategory() {
		return AuthenticatedPageCategory.ADMINS;
	}

}
