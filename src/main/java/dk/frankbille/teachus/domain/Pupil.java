package dk.frankbille.teachus.domain;

public interface Pupil extends Person {
	Teacher getTeacher();
	void setTeacher(Teacher teacher);
}
