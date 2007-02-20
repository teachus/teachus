package dk.frankbille.teachus.domain.impl;

import dk.frankbille.teachus.domain.Pupil;
import dk.frankbille.teachus.domain.PupilBooking;

public class PupilBookingImpl extends BookingImpl implements PupilBooking {
	private static final long serialVersionUID = 1L;

	private Pupil pupil;

	private boolean notificationSent;
	
	private boolean paid;

	public Pupil getPupil() {
		return pupil;
	}

	public boolean isNotificationSent() {
		return notificationSent;
	}

	public boolean isPaid() {
		return paid;
	}

	public void setNotificationSent(boolean notificationSent) {
		this.notificationSent = notificationSent;
	}

	public void setPaid(boolean paid) {
		this.paid = paid;
	}

	public void setPupil(Pupil pupil) {
		this.pupil = pupil;
	}

}
