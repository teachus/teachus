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

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import dk.teachus.backend.domain.Pupil;
import dk.teachus.backend.domain.Teacher;

@Entity
@Table(name = "pupil")
public class PupilImpl extends PersonImpl implements Pupil {
	private static final long serialVersionUID = 1L;
	
	@OneToOne
	@JoinColumn(name = "teacher_id")
	private Teacher teacher;
	
	private String notes;
	
	@Override
	public Teacher getTeacher() {
		return teacher;
	}
	
	@Override
	public void setTeacher(final Teacher teacher) {
		this.teacher = teacher;
	}
	
	@Override
	public String getNotes() {
		return notes;
	}
	
	@Override
	public void setNotes(final String notes) {
		this.notes = notes;
	}
	
}
