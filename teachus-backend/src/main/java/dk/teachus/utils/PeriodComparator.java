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
package dk.teachus.utils;

import java.util.Comparator;
import java.util.Date;

import org.joda.time.DateTime;

import dk.teachus.backend.domain.Period;

public class PeriodComparator implements Comparator<Period> {

	public int compare(Period o1, Period o2) {
		int compare = 0;
		
		if (o1 != null && o2 != null) {
			if (o1.getStartTime() != null && o2.getStartTime() != null) {
				DateTime startTime1 = convertAndReset(o1.getStartTime().getDate());
				DateTime startTime2 = convertAndReset(o2.getStartTime().getDate());
				compare = startTime1.compareTo(startTime2);
			} else if (o1.getStartTime() != null) {
				compare = -1;
			} else if (o2.getStartTime() != null) {
				compare = 1;
			}
		} else if (o1 != null) {
			compare = -1;
		} else if (o2 != null) {
			compare = 1;
		}
		
		return compare;
	}
	
	private DateTime convertAndReset(Date date) {
		DateTime now = new DateTime();
		DateTime dateTime = new DateTime(date);
		
		dateTime = dateTime.withYear(now.getYear());
		dateTime = dateTime.withMonthOfYear(now.getMonthOfYear());
		dateTime = dateTime.withDayOfMonth(now.getDayOfMonth());
		dateTime = dateTime.withSecondOfMinute(0);
		dateTime = dateTime.withMillisOfSecond(0);
		
		return dateTime;
	}

}
