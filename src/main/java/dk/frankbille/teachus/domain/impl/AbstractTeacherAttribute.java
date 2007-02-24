package dk.frankbille.teachus.domain.impl;

import dk.frankbille.teachus.domain.Teacher;
import dk.frankbille.teachus.domain.TeacherAttribute;

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
