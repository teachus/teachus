package dk.teachus.domain;

public interface Pupil extends Person {
	Teacher getTeacher();
	void setTeacher(Teacher teacher);
}
