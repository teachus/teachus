package dk.frankbille.teachus.domain;

import java.io.Serializable;
import java.util.Date;

public interface PupilBooking extends Serializable {
	Long getId();
	
	Pupil getPupil();
	void setPupil(Pupil pupil);
	
	Period getPeriod();
	void setPeriod(Period period);
	
	Date getDate();
	void setDate(Date date);
	
	Date getCreateDate();
	void setCreateDate(Date createDate);
	
	boolean isNotificationSent();
	void setNotificationSent(boolean notificationSent);
}
