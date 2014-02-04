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
	
	public PeriodsImpl() {
	}
	
	public PeriodsImpl(final List<Period> periods) {
		this.periods = periods;
	}
	
	@Override
	public List<Period> getPeriods() {
		return periods;
	}
	
	@Override
	public void setPeriods(final List<Period> periods) {
		this.periods = periods;
	}
	
	@Override
	public void addPeriod(final Period period) {
		periods.add(period);
	}
	
	@Override
	public boolean hasDate(final DateMidnight date) {
		boolean hasDate = false;
		
		for (final Period period : getValidPeriods()) {
			if (period.hasDate(date)) {
				hasDate = true;
				break;
			}
		}
		
		return hasDate;
	}
	
	@Override
	public boolean containsDate(final DateMidnight date) {
		boolean contains = false;
		
		for (final Period period : getValidPeriods()) {
			if (period.dateIntervalContains(date)) {
				contains = true;
				break;
			}
		}
		
		return contains;
	}
	
	@Override
	public boolean hasPeriodBefore(final DateMidnight dateMidnight) {
		boolean hasPeriodBefore = false;
		
		for (final Period period : getValidPeriods()) {
			final DateMidnight beginDate = period.getBeginDate();
			if (beginDate == null) {
				hasPeriodBefore = true;
				break;
			} else {
				if (beginDate.isBefore(dateMidnight) || beginDate.equals(dateMidnight)) {
					hasPeriodBefore = true;
					break;
				}
			}
		}
		
		return hasPeriodBefore;
	}
	
	@Override
	public boolean hasPeriodAfter(final DateMidnight dateMidnight) {
		boolean hasPeriodAfter = false;
		
		for (final Period period : getValidPeriods()) {
			if (period.getEndDate() == null) {
				hasPeriodAfter = true;
				break;
			} else {
				final DateMidnight endDate = period.getEndDate();
				if (endDate.isAfter(dateMidnight) || endDate.isEqual(dateMidnight)) {
					hasPeriodAfter = true;
					break;
				}
			}
		}
		
		return hasPeriodAfter;
	}
	
	@Override
	public List<DatePeriod> generateDatesForWeek(final DateMidnight startDate) {
		final List<DatePeriod> dates = new ArrayList<DatePeriod>();
		DateMidnight sd = startDate.withDayOfWeek(DateTimeConstants.MONDAY);
		final int week = sd.getWeekOfWeekyear();
		
		while (week == sd.getWeekOfWeekyear()) {
			DatePeriod datePeriod = null;
			for (final Period period : getValidPeriods()) {
				// Check if this period can handle the date at all
				if (period.dateIntervalContains(sd)) {
					final DateMidnight date = period.generateDate(sd);
					if (date != null) {
						if (datePeriod == null) {
							datePeriod = new DatePeriodImpl(date);
							dates.add(datePeriod);
						}
						
						datePeriod.addPeriod(period);
					}
				}
			}
			
			sd = sd.plusDays(1);
		}
		
		Collections.sort(dates, new Comparator<DatePeriod>() {
			@Override
			public int compare(final DatePeriod o1, final DatePeriod o2) {
				return o1.getDate().compareTo(o2.getDate());
			}
		});
		
		return dates;
	}
	
	@Override
	public List<DatePeriod> generateDates(final DateMidnight weekDate, final int numberOfDays) {
		return generateDates(weekDate, numberOfDays, false);
	}
	
	@Override
	public List<DatePeriod> generateDates(DateMidnight weekDate, final int numberOfDays, final boolean explicitNumberOfDays) {
		weekDate = weekDate.withDayOfWeek(DateTimeConstants.MONDAY);
		
		final List<DatePeriod> dates = new ArrayList<DatePeriod>();
		List<DatePeriod> weekDates = generateDatesForWeek(weekDate);
		if (numberOfDays > 0) {
			do {
				for (final DatePeriod datePeriod : weekDates) {
					dates.add(datePeriod);
					
					if (explicitNumberOfDays) {
						if (dates.size() >= numberOfDays) {
							break;
						}
					}
				}
				weekDate = weekDate.plusWeeks(1);
				weekDates = generateDatesForWeek(weekDate);
			} while (dates.size() + weekDates.size() <= numberOfDays && hasPeriodAfter(weekDate));
		}
		
		return dates;
	}
	
	@Override
	public int numberOfWeeksBack(DateMidnight lastDate, final int numberOfDays) {
		int numberOfWeeks = 0;
		
		int dates = 0;
		while (hasPeriodBefore(lastDate) && dates < numberOfDays) {
			lastDate = lastDate.minusWeeks(1);
			dates += generateDatesForWeek(lastDate).size();
			
			if (dates <= numberOfDays) {
				numberOfWeeks++;
			}
		}
		
		return numberOfWeeks;
	}
	
	private List<Period> getValidPeriods() {
		final List<Period> validPeriods = new ArrayList<Period>();
		
		if (periods != null) {
			for (final Period period : periods) {
				if (period.isValid()) {
					validPeriods.add(period);
				}
			}
		}
		
		return validPeriods;
	}
}
