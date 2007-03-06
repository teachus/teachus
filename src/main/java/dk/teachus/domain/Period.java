package dk.teachus.domain;

import java.util.Date;
import java.util.List;

import org.joda.time.DateTime;

import dk.teachus.domain.impl.PeriodImpl.WeekDay;

public interface Period {

	Long getId();
	
	String getName();
	
	Date getEndDate();

	Date getEndTime();

	Date getBeginDate();

	Date getStartTime();

	List<WeekDay> getWeekDays();
	
	Teacher getTeacher();
	
	double getPrice();
	
	/**
	 * @return The duration of a lesson in minutes
	 */
	int getLessonDuration();
	
	/**
	 * @return How often a lesson can start. F.ex. 15 on this property
	 * gives: 10.10, 10.15, 10.30 and 10.45
	 */
	int getIntervalBetweenLessonStart();
	
	void setName(String name);

	void setEndDate(Date endDate);

	void setEndTime(Date endTime);

	void setBeginDate(Date startDate);

	void setStartTime(Date startTime);
	
	void setTeacher(Teacher teacher);
	
	void setPrice(double price);
	
	void setLessonDuration(int minutes);
	
	void setIntervalBetweenLessonStart(int minutes);

	/*
	 * Utility methods
	 */
	void addWeekDay(WeekDay weekDay);

	boolean hasWeekDay(Date date);

	boolean dateIntervalContains(Date date);

	boolean hasDate(Date date);

	Date generateDate(Date startDate);
	
	boolean isTimeValid(DateTime time);

	boolean mayBook(DateTime time);

	boolean conflicts(DateTime bookedTime, DateTime time);

	boolean inLesson(DateTime bookedTime, DateTime time);
	
}