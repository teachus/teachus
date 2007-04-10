package dk.teachus.domain;

import java.io.Serializable;
import java.util.Date;

public interface Booking extends Serializable {
	Long getId();
	
	boolean isActive();
	void setActive(boolean active);
	
	Period getPeriod();
	void setPeriod(Period period);
	
	Date getDate();
	void setDate(Date date);
	
	Date getCreateDate();
	void setCreateDate(Date createDate);
	
	Teacher getTeacher();
	void setTeacher(Teacher teacher);
}
