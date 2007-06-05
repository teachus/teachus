/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package dk.teachus.backend.domain.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.joda.time.DateMidnight;
import org.joda.time.DateTimeConstants;

import dk.teachus.backend.domain.DatePeriod;
import dk.teachus.backend.domain.Period;
import dk.teachus.backend.domain.Periods;

public class PeriodsImpl implements Periods {
	private static final long serialVersionUID = 1L;

	private List<Period> periods = new ArrayList<Period>();

	public List<Period> getPeriods() {
		return periods;
	}

	public void setPeriods(List<Period> periods) {
		this.periods = periods;
	}

	public void addPeriod(Period period) {		
		periods.add(period);
	}
	
	public boolean hasDate(DateMidnight date) {
		boolean hasDate = false;
		
		for (Period period : periods) {
			if (period.hasDate(date)) {
				hasDate = true;
				break;
			}
		}
		
		return hasDate;
	}
	
	public boolean containsDate(DateMidnight date) {
		boolean contains = false;
		
		for (Period period : periods) {
			if (period.dateIntervalContains(date)) {
				contains = true;
				break;
			}
		}
		
		return contains;
	}
	
	public boolean hasPeriodBefore(DateMidnight date) {
		boolean hasPeriodBefore = false;
		
		for (Period period : periods) {
			if (period.getBeginDate() == null) {
				hasPeriodBefore = true;
				break;
			} else {
				DateMidnight beginDate = new DateMidnight(period.getBeginDate());
				if (beginDate.isBefore(date) || beginDate.isEqual(date)) {
					hasPeriodBefore = true;
					break;
				}
			}
		}
		
		return hasPeriodBefore;
	}
	
	public boolean hasPeriodAfter(DateMidnight date) {
		boolean hasPeriodAfter = false;
		
		for (Period period : periods) {
			if (period.getEndDate() == null) {
				hasPeriodAfter = true;
				break;
			} else {
				DateMidnight endDate = new DateMidnight(period.getEndDate());
				if (endDate.isAfter(date) || endDate.isEqual(date)) {
					hasPeriodAfter = true;
					break;
				}
			}
		}
		
		return hasPeriodAfter;
	}
	
	public List<DatePeriod> generateDatesForWeek(DateMidnight startDate) {
		List<DatePeriod> dates = new ArrayList<DatePeriod>();
		DateMidnight sd = new DateMidnight(startDate).withDayOfWeek(DateTimeConstants.MONDAY);
		int week = sd.getWeekOfWeekyear();
		
		while(week == sd.getWeekOfWeekyear()) {			
			DatePeriod datePeriod = null;
			for (Period period : periods) {
				// Check if this period can handle the date at all
				if (period.dateIntervalContains(sd)) {				
					DateMidnight date = period.generateDate(sd);
					if (date != null) {
						if (datePeriod == null) {
							datePeriod = new DatePeriodImpl(date.toDate());
							dates.add(datePeriod);
						}
						
						datePeriod.addPeriod(period);
					}
				}
			}
			
			sd = sd.plusDays(1);
		}
		
		Collections.sort(dates, new Comparator<DatePeriod>() {
			public int compare(DatePeriod o1, DatePeriod o2) {
				return o1.getDate().compareTo(o2.getDate());
			}
		});
		
		return dates;
	}

	public List<DatePeriod> generateDates(DateMidnight weekDate, int numberOfDays) {
		weekDate = weekDate.withDayOfWeek(DateTimeConstants.MONDAY);
		
		List<DatePeriod> dates = new ArrayList<DatePeriod>();
		List<DatePeriod> weekDates = generateDatesForWeek(weekDate);
		do {
			dates.addAll(weekDates);
			weekDate = weekDate.plusWeeks(1);
			weekDates = generateDatesForWeek(weekDate);
		} while(dates.size()+weekDates.size() <= numberOfDays
				&& hasPeriodAfter(weekDate));
		
		return dates;
	}
	
	public int numberOfWeeksBack(DateMidnight lastDate, int numberOfDays) {
		int numberOfWeeks = 0;
		
		int dates = 0;
		while(hasPeriodBefore(lastDate) && dates < numberOfDays) {
			lastDate = lastDate.minusWeeks(1);
			dates += generateDatesForWeek(lastDate).size();
			
			if (dates <= numberOfDays) {
				numberOfWeeks++;
			}
		}
		
		return numberOfWeeks;
	}
}
