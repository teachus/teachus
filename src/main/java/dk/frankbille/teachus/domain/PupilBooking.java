package dk.frankbille.teachus.domain;


public interface PupilBooking extends Booking {
	Pupil getPupil();
	void setPupil(Pupil pupil);
	
	boolean isNotificationSent();
	void setNotificationSent(boolean notificationSent);
}
