/**
 * 
 */
package dk.teachus.frontend.pages.stats.admin;

import java.io.Serializable;

import org.joda.time.DateMidnight;
import org.joda.time.DateTime;

import dk.teachus.backend.domain.Teacher;

public class Entry implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private DateTime date;
	private Teacher teacher;
	private String text;
	
	public Entry(Teacher teacher, DateTime date, String text) {
		this.date = date;
		this.teacher = teacher;
		this.text = text;
	}
	
	public Teacher getTeacher() {
		return teacher;
	}
	
	public DateMidnight getDate() {
		return date.toDateMidnight();
	}
	
	public DateTime getDateTime() {
		return date;
	}
	
	public String getText() {
		return text;
	}
}