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

import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.joda.time.PeriodType;

public class DateUtils {

	public static int intervalMinutes(DateTime date1, DateTime date2) {
		int intervalMinutes = 0;
		
		if (date1 != null && date2 != null) {
			intervalMinutes = new Duration(date1, date2).toPeriod(PeriodType.minutes()).getMinutes();
		}
		
		return intervalMinutes;
	}
	
	public static DateTime resetDateTime(DateTime time, DateTime resetTo) {
		DateTime timeDate = time;
		DateTime resetTimeDate = resetTo;
		
		timeDate = timeDate.withDate(resetTimeDate.getYear(), resetTimeDate.getMonthOfYear(),
				resetTimeDate.getDayOfMonth()).withSecondOfMinute(0)
				.withMillisOfSecond(0);
		
		return timeDate;
	}

}
