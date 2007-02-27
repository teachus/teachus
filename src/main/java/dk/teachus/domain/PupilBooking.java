package dk.teachus.domain;


public interface PupilBooking extends Booking {
	Pupil getPupil();
	void setPupil(Pupil pupil);
	
	boolean isNotificationSent();
	void setNotificationSent(boolean notificationSent);
	
	boolean isPaid();
	void setPaid(boolean paid);
}
