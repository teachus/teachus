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

import dk.teachus.backend.domain.Teacher;
import dk.teachus.backend.domain.TeacherAttribute;

public abstract class AbstractTeacherAttribute extends AbstractHibernateObject implements TeacherAttribute {
	private Teacher teacher;

	private String value;

	public Teacher getTeacher() {
		return teacher;
	}

	public String getValue() {
		return value;
	}

	public void setTeacher(Teacher teacher) {
		this.teacher = teacher;
	}

	public void setValue(String value) {
		this.value = value;
	}

}