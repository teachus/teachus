package dk.teachus.backend.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.TimeZone;

import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.joda.time.DateMidnight;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.Duration;
import org.joda.time.PeriodType;

public class TeachUsDate implements Serializable, Comparable<TeachUsDate> {
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
	
	public TeachUsDate(int year, int monthOfYear, int dayOfMonth, TimeZone timeZone) {
		this(new DateMidnight(year, monthOfYear, dayOfMonth), timeZone);
	}

	public TeachUsDate(DateMidnight dateMidnight, TimeZone timeZone) {
		this(dateMidnight.toDateTime(), timeZone);
	}

	public TeachUsDate(int year, int monthOfYear, int dayOfMonth, int hourOfDay, int minuteOfHour, int secondOfMinute, TimeZone timeZone) {
		this(year, monthOfYear, dayOfMonth, hourOfDay, minuteOfHour, secondOfMinute, 0, timeZone);
	}
	
	public TeachUsDate(int year, int monthOfYear, int dayOfMonth, int hourOfDay, int minuteOfHour, int secondOfMinute, int millisOfSecond, TimeZone timeZone) {
		this(new DateTime(year, monthOfYear, dayOfMonth, hourOfDay, minuteOfHour, secondOfMinute, millisOfSecond), timeZone);
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

	public TeachUsDate minusHours(int hours) {
		return dateTime != null ? new TeachUsDate(dateTime.minusHours(hours), timeZone) : this;
	}

	public TeachUsDate minusWeeks(int minutes) {
		return dateTime != null ? new TeachUsDate(dateTime.minusWeeks(minutes), timeZone) : this;
	}

	public TeachUsDate minusMonths(int months) {
		return dateTime != null ? new TeachUsDate(dateTime.minusMonths(months), timeZone) : this;
	}

	public TeachUsDate minusYears(int years) {
		return dateTime != null ? new TeachUsDate(dateTime.minusYears(years), timeZone) : this;
	}
	
	public TeachUsDate plusMinutes(int minutes) {
		return dateTime != null ? new TeachUsDate(dateTime.plusMinutes(minutes), timeZone) : this;
	}

	public TeachUsDate plusHours(int hours) {
		return dateTime != null ? new TeachUsDate(dateTime.plusHours(hours), timeZone) : this;
	}

	public TeachUsDate plusDays(int days) {
		return dateTime != null ? new TeachUsDate(dateTime.plusDays(days), timeZone) : this;
	}

	public TeachUsDate plusWeeks(int weeks) {
		return dateTime != null ? new TeachUsDate(dateTime.plusWeeks(weeks), timeZone) : this;
	}

	public TeachUsDate plusMonths(int months) {
		return dateTime != null ? new TeachUsDate(dateTime.plusMonths(months), timeZone) : this;
	}

	public TeachUsDate plusYears(int years) {
		return dateTime != null ? new TeachUsDate(dateTime.plusYears(years), timeZone) : this;
	}

	public TeachUsDate withMillisOfSecond(int millisOfSecond) {
		return dateTime != null ? new TeachUsDate(dateTime.withMillisOfSecond(millisOfSecond), timeZone) : this;
	}

	public TeachUsDate withSecondOfMinute(int secondOfMinute) {
		return dateTime != null ? new TeachUsDate(dateTime.withSecondOfMinute(secondOfMinute), timeZone) : this;
	}

	public TeachUsDate withMinuteOfHour(int minuteOfHour) {
		return dateTime != null ? new TeachUsDate(dateTime.withMinuteOfHour(minuteOfHour), timeZone) : this;
	}

	public TeachUsDate withHourOfDay(int hourOfDay) {
		return dateTime != null ? new TeachUsDate(dateTime.withHourOfDay(hourOfDay), timeZone) : this;
	}

	public TeachUsDate withTime(int hourOfDay, int minuteOfHour, int secondOfMinute, int millisOfSecond) {
		return dateTime != null ? new TeachUsDate(dateTime.withTime(hourOfDay, minuteOfHour, secondOfMinute, millisOfSecond), timeZone) : this;
	}

	public TeachUsDate withDayOfWeek(int dayOfWeek) {
		return dateTime != null ? new TeachUsDate(dateTime.withDayOfWeek(dayOfWeek), timeZone) : this;
	}

	public TeachUsDate withDayOfMonth(int dayOfMonth) {
		return dateTime != null ? new TeachUsDate(dateTime.withDayOfMonth(dayOfMonth), timeZone) : this;
	}

	public TeachUsDate withMonthOfYear(int monthOfYear) {
		return dateTime != null ? new TeachUsDate(dateTime.withMonthOfYear(monthOfYear), timeZone) : this;
	}

	public TeachUsDate withYear(int year) {
		return dateTime != null ? new TeachUsDate(dateTime.withYear(year), timeZone) : this;
	}

	public TeachUsDate withDate(int year, int monthOfYear, int dayOfMonth) {
		return dateTime != null ? new TeachUsDate(dateTime.withDate(year, monthOfYear, dayOfMonth), timeZone) : this;
	}
	
	public boolean isBefore(TeachUsDate date) {
		return dateTime != null ? dateTime.isBefore(date.getDateTime()) : true;
	}

	public boolean isAfter(TeachUsDate date) {
		return dateTime != null ? dateTime.isAfter(date.getDateTime()) : true;
	}

	public int getMinuteOfHour() {
		return dateTime != null ? dateTime.getMinuteOfHour() : -1;
	}

	public int getMinuteOfDay() {
		return dateTime != null ? dateTime.getMinuteOfDay() : -1;
	}

	public int getHourOfDay() {
		return dateTime != null ? dateTime.getHourOfDay() : -1;
	}

	public int getDayOfMonth() {
		return dateTime != null ? dateTime.getDayOfMonth() : -1;
	}

	public int getWeekOfWeekyear() {
		return dateTime != null ? dateTime.getWeekOfWeekyear() : -1;
	}

	public int getMonthOfYear() {
		return dateTime != null ? dateTime.getMonthOfYear() : -1;
	}

	public int getYear() {
		return dateTime != null ? dateTime.getYear() : -1;
	}

	public int intervalMinutes(TeachUsDate date) {
		int intervalMinutes = 0;
		
		if (dateTime != null && date.getDateTime() != null) {
			intervalMinutes = new Duration(dateTime, date.getDateTime()).toPeriod(PeriodType.minutes()).getMinutes();
		}
		
		return intervalMinutes;
	}
	
	@Override
	public boolean equals(Object obj) {
		return EqualsBuilder.reflectionEquals(this, obj);
	}
	
	@Override
	public int hashCode() {
		return HashCodeBuilder.reflectionHashCode(this);
	}
	
	public int compareTo(TeachUsDate tud) {
		return CompareToBuilder.reflectionCompare(this, tud);
	}
	
	@Override
	public String toString() {
		return dateTime != null ? dateTime.toString() : "No time. TZ: "+timeZone;
	}
	
}
