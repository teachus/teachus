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
package dk.teachus.backend.dao;

import java.io.Serializable;
import java.util.List;

import dk.teachus.backend.domain.Admin;
import dk.teachus.backend.domain.Person;
import dk.teachus.backend.domain.Pupil;
import dk.teachus.backend.domain.Teacher;
import dk.teachus.backend.domain.TeacherAttribute;

public interface PersonDAO extends Serializable {

	void save(Person person);

	Person authenticatePerson(String username, String password);

	Person authenticatePersonWithPrivateKey(String username, String privateKey);

	<P extends Person> List<P> getPersons(Class<P> personClass);

	List<Pupil> getPupils(Teacher teacher);
	
	Person getPerson(Long personId);

	void setInactive(Long personId);
	
	void saveAttribute(TeacherAttribute attribute);
	
	TeacherAttribute getAttribute(Teacher teacher);

	Person usernameExists(String username);
	
	void deleteTeacher(Teacher teacher);
	
	Admin createAdminObject();
	
	Teacher createTeacherObject();
	
	Pupil createPupilObject();

	void changeActiveState(Long personId);

}
