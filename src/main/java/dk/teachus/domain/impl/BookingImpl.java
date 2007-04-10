package dk.teachus.domain.impl;

import java.util.Date;

import dk.teachus.domain.Booking;
import dk.teachus.domain.Period;
import dk.teachus.domain.Teacher;

public abstract class BookingImpl extends AbstractHibernateObject implements Booking {
	private static final long serialVersionUID = 1L;

	private boolean active = true;
	
	private Period period;

	private Date date;

	private Date createDate = new Date();

	private Teacher teacher;

	public Date getCreateDate() {
		return createDate;
	}

	public Date getDate() {
		return date;
	}

	public Period getPeriod() {
		return period;
	}

	public Teacher getTeacher() {
		return teacher;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public void setPeriod(Period period) {
		this.period = period;
	}

	public void setTeacher(Teacher teacher) {
		this.teacher = teacher;
	}

}
