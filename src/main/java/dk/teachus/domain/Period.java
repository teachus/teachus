package dk.teachus.domain;

import java.util.Date;
import java.util.List;

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
	
	void setName(String name);

	void setEndDate(Date endDate);

	void setEndTime(Date endTime);

	void setBeginDate(Date startDate);

	void setStartTime(Date startTime);
	
	void setTeacher(Teacher teacher);
	
	void setPrice(double price);
	
	void setLessonDuration(int minutes);

	/*
	 * Utility methods
	 */
	void addWeekDay(WeekDay weekDay);

	boolean hasWeekDay(Date date);

	boolean dateIntervalContains(Date date);

	boolean hasDate(Date date);

	Date generateDate(Date startDate);

}