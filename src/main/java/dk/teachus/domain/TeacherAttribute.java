package dk.teachus.domain;

import java.io.Serializable;

public interface TeacherAttribute extends Serializable {

	Long getId();
	
	Teacher getTeacher();
	void setTeacher(Teacher teacher);
	
	String getValue();
	void setValue(String value);
	
}
