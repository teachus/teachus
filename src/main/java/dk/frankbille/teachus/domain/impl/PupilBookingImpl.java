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

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Period getPeriod() {
		return period;
	}

	public void setPeriod(Period period) {
		this.period = period;
	}

	public Pupil getPupil() {
		return pupil;
	}

	public void setPupil(Pupil pupil) {
		this.pupil = pupil;
	}

}
