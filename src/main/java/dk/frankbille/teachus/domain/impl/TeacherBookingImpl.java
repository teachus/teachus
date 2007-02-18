package dk.frankbille.teachus.domain.impl;

import dk.frankbille.teachus.domain.Teacher;
import dk.frankbille.teachus.domain.TeacherBooking;

public class TeacherBookingImpl extends BookingImpl implements TeacherBooking {
	private static final long serialVersionUID = 1L;

	private Teacher teacher;

	public Teacher getTeacher() {
		return teacher;
	}

	public void setTeacher(Teacher teacher) {
		this.teacher = teacher;
	}

}
