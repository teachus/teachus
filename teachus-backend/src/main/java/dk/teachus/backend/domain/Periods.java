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
package dk.teachus.backend.domain;

import java.io.Serializable;
import java.util.List;

import org.joda.time.DateMidnight;

public interface Periods extends Serializable {

	List<Period> getPeriods();
	void setPeriods(List<Period> result);

	void addPeriod(Period period);

	boolean hasDate(DateMidnight date);
	
	boolean containsDate(DateMidnight date);
	
	boolean hasPeriodBefore(DateMidnight date);
	
	boolean hasPeriodAfter(DateMidnight date);

	List<DatePeriod> generateDatesForWeek(DateMidnight startDate);
	
	List<DatePeriod> generateDates(DateMidnight startDate, int numberOfDays);

	int numberOfWeeksBack(DateMidnight lastDate, int numberOfDays);

}