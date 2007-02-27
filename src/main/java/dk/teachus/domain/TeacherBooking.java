package dk.teachus.domain;

public interface TeacherBooking extends Booking {
	Teacher getTeacher();
	void setTeacher(Teacher teacher);
}
