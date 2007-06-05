package dk.teachus.backend.domain;


public interface TeacherStatistics {

	long getTotalBookingCount();

	long getPeriodCount();

	void setPeriodCount(long periodCount);

	long getPupilBookingCount();

	void setPupilBookingCount(long pupilBookingCount);

	long getPupilCount();

	void setPupilCount(long pupilCount);

	Teacher getTeacher();

	void setTeacher(Teacher teacher);

	long getTeacherBookingCount();

	void setTeacherBookingCount(long teacherBookingCount);

}