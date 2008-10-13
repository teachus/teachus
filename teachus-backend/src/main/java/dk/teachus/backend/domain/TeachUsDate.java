package dk.teachus.backend.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.TimeZone;

import org.joda.time.DateMidnight;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

public class TeachUsDate implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private DateTime dateTime;
	private DateTimeZone timeZone;
	
	public TeachUsDate() {
		this(null);
	}
	
	public TeachUsDate(DateTime dateTime) {
		this(dateTime, (TimeZone) null);
	}
	
	public TeachUsDate(Date date, TimeZone timeZone) {
		this(date != null ? new DateTime(date) : null, timeZone);
	}
	
	public TeachUsDate(DateTime dateTime, DateTimeZone timeZone) {
		this(dateTime, timeZone != null ? timeZone.toTimeZone() : null);
	}
	
	public TeachUsDate(DateTime dateTime, TimeZone timeZone) {
		this.dateTime = dateTime;
		setTimeZone(timeZone);
	}
	
	public Date getDate() {
		return dateTime != null ? dateTime.toDate() : null;
	}
	
	public void setDate(Date date) {
		if (date != null) {
			dateTime = new DateTime(date);
			dateTime = dateTime.withZone(timeZone);
		} else {
			dateTime = null;
		}
	}
	
	public DateTime getDateTime() {
		return dateTime;
	}
	
	public void setDateTime(DateTime dateTime) {
		this.dateTime = dateTime;
		this.dateTime = this.dateTime.withZone(timeZone);
	}
	
	public DateMidnight getDateMidnight() {
		return dateTime != null ? new DateMidnight(dateTime) : null;
	}
	
	public TimeZone getTimeZone() {
		return dateTime != null ? dateTime.getZone().toTimeZone() : null;
	}
	
	public void setTimeZone(TimeZone timeZone) {
		if (timeZone != null) {
			this.timeZone = DateTimeZone.forTimeZone(timeZone);
		} else {
			this.timeZone = DateTimeZone.UTC;
		}
		
		if (dateTime != null) {
			dateTime = dateTime.withZone(this.timeZone);
		}
	}
	
}
