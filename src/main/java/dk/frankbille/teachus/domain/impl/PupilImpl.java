package dk.frankbille.teachus.domain.impl;

import dk.frankbille.teachus.domain.Pupil;
import dk.frankbille.teachus.domain.Teacher;


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
