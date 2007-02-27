package dk.teachus.domain.impl;

import dk.teachus.domain.Pupil;
import dk.teachus.domain.Teacher;


public class PupilImpl extends PersonImpl implements Pupil {
	private static final long serialVersionUID = 1L;
	
	private Teacher teacher;

	public Teacher getTeacher() {
		return teacher;
	}

	public void setTeacher(Teacher teacher) {
		this.teacher = teacher;
	}	
	
}
