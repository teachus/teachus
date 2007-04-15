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
package dk.teachus.frontend.models;

import wicket.Component;
import wicket.model.LoadableDetachableModel;
import dk.teachus.dao.PersonDAO;
import dk.teachus.domain.Person;
import dk.teachus.frontend.TeachUsApplication;

public abstract class PersonModel<P extends Person> extends LoadableDetachableModel {
	private static final long serialVersionUID = 1L;

	private Long personId;
	
	public PersonModel(Long personId) {
		this.personId = personId;
	}
	
	public Long getPersonId() {
		return personId;
	}
	
	public void setPassword(Component component, String password) {
		getObject(component).setPassword(password);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public P getObject(Component component) {
		return (P) super.getObject(component);
	}

	public void save(Component component) {
		boolean newPerson = personId == null;
		
		P person = getObject(component);
		
		PersonDAO personDAO = TeachUsApplication.get().getPersonDAO();
		personDAO.save(person);
		personId = person.getId();
		
		if (newPerson) {
			onSavedNewPerson(person);
		}
	}
	
	@SuppressWarnings("unchecked")
	@Override
	protected Object load() {
		P person = null;
		
		if (personId != null) {
			PersonDAO personDAO = TeachUsApplication.get().getPersonDAO();
			person = (P) personDAO.getPerson(personId);
		}

		if (person == null) {
			person = createNewPerson();
		}
		
		return person;
	}
	
	protected abstract P createNewPerson();
	
	protected void onSavedNewPerson(P person) {
	}
}
