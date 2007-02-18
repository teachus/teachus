package dk.frankbille.teachus.domain.impl;

import java.util.Date;

import dk.frankbille.teachus.domain.Period;
import dk.frankbille.teachus.domain.Pupil;
import dk.frankbille.teachus.domain.PupilBooking;

public class PupilBookingImpl extends AbstractHibernateObject implements PupilBooking {
	private static final long serialVersionUID = 1L;

	private Pupil pupil;

	private Period period;

	private Date date;

	private Date createDate = new Date();

	private boolean notificationSent;

	public Date getCreateDate() {
		return createDate;
	}

	public Date getDate() {
		return date;
	}

	public Period getPeriod() {
		return period;
	}

	public Pupil getPupil() {
		return pupil;
	}

	public boolean isNotificationSent() {
		return notificationSent;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public void setNotificationSent(boolean notificationSent) {
		this.notificationSent = notificationSent;
	}

	public void setPeriod(Period period) {
		this.period = period;
	}

	public void setPupil(Pupil pupil) {
		this.pupil = pupil;
	}

}
