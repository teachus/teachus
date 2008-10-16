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

import java.util.TimeZone;

import junit.framework.TestCase;
import dk.teachus.backend.domain.impl.PeriodImpl;

public class TestPeriod extends TestCase {

	public void testIsTimeValid_simple() {
		TimeZone timeZone = TimeZone.getDefault();
		
		Period period = new PeriodImpl();
		period.setStartTime(new TeachUsDate(2006, 1, 1, 10, 0, 0, timeZone));
		period.setEndTime(new TeachUsDate(2008, 1, 1, 15, 0, 0, timeZone));
		period.setIntervalBetweenLessonStart(60);
		
		assertTrue(period.isTimeValid(new TeachUsDate(2007, 1, 1, 10, 0, 0, timeZone)));
		assertTrue(period.isTimeValid(new TeachUsDate(2007, 1, 1, 11, 0, 0, timeZone)));
		assertTrue(period.isTimeValid(new TeachUsDate(2007, 1, 1, 14, 0, 0, timeZone)));
		assertTrue(period.isTimeValid(new TeachUsDate(2007, 1, 1, 11, 0, 1, timeZone)));
		assertTrue(period.isTimeValid(new TeachUsDate(2007, 1, 1, 11, 0, 0, 1, timeZone)));
		
		assertFalse(period.isTimeValid(new TeachUsDate(2007, 1, 1, 15, 0, 0, timeZone)));
		assertFalse(period.isTimeValid(new TeachUsDate(2007, 1, 1, 9, 0, 0, timeZone)));
		assertFalse(period.isTimeValid(new TeachUsDate(2007, 1, 1, 12, 30, 0, timeZone)));
		assertFalse(period.isTimeValid(new TeachUsDate(2007, 1, 1, 16, 0, 0, timeZone)));
		assertFalse(period.isTimeValid(new TeachUsDate(2007, 1, 1, 11, 1, 0, timeZone)));
	}
	
	public void testIsTimeValid_halfHour() {
		TimeZone timeZone = TimeZone.getDefault();
		
		Period period = new PeriodImpl();
		period.setStartTime(new TeachUsDate(2007, 1, 1, 10, 0, 0, timeZone));
		period.setEndTime(new TeachUsDate(2007, 1, 1, 15, 0, 0, timeZone));
		period.setIntervalBetweenLessonStart(30);
		
		assertTrue(period.isTimeValid(new TeachUsDate(2007, 1, 1, 10, 0, 0, timeZone)));
		assertTrue(period.isTimeValid(new TeachUsDate(2007, 1, 1, 11, 0, 0, timeZone)));
		assertTrue(period.isTimeValid(new TeachUsDate(2007, 1, 1, 14, 0, 0, timeZone)));
		assertTrue(period.isTimeValid(new TeachUsDate(2007, 1, 1, 11, 0, 1, timeZone)));
		assertTrue(period.isTimeValid(new TeachUsDate(2007, 1, 1, 11, 0, 0, 1, timeZone)));
		assertTrue(period.isTimeValid(new TeachUsDate(2007, 1, 1, 11, 30, 0, timeZone)));
		assertTrue(period.isTimeValid(new TeachUsDate(2007, 1, 1, 10, 30, 0, timeZone)));
		assertTrue(period.isTimeValid(new TeachUsDate(2007, 1, 1, 14, 30, 0, timeZone)));
		
		assertFalse(period.isTimeValid(new TeachUsDate(2007, 1, 1, 15, 0, 0, timeZone)));
		assertFalse(period.isTimeValid(new TeachUsDate(2007, 1, 1, 9, 0, 0, timeZone)));
		assertFalse(period.isTimeValid(new TeachUsDate(2007, 1, 1, 16, 0, 0, timeZone)));
		assertFalse(period.isTimeValid(new TeachUsDate(2007, 1, 1, 11, 1, 0, timeZone)));
	}
	
	public void testIsTimeValid_15Min() {
		TimeZone timeZone = TimeZone.getDefault();
		
		Period period = new PeriodImpl();
		period.setStartTime(new TeachUsDate(2007, 1, 1, 10, 0, 0, timeZone));
		period.setEndTime(new TeachUsDate(2007, 1, 1, 15, 0, 0, timeZone));
		period.setIntervalBetweenLessonStart(15);
		
		assertTrue(period.isTimeValid(new TeachUsDate(2007, 1, 1, 10, 0, 0, timeZone)));
		assertTrue(period.isTimeValid(new TeachUsDate(2007, 1, 1, 11, 0, 0, timeZone)));
		assertTrue(period.isTimeValid(new TeachUsDate(2007, 1, 1, 14, 0, 0, timeZone)));
		assertTrue(period.isTimeValid(new TeachUsDate(2007, 1, 1, 11, 0, 1, timeZone)));
		assertTrue(period.isTimeValid(new TeachUsDate(2007, 1, 1, 11, 0, 0, 1, timeZone)));
		assertTrue(period.isTimeValid(new TeachUsDate(2007, 1, 1, 11, 30, 0, timeZone)));
		assertTrue(period.isTimeValid(new TeachUsDate(2007, 1, 1, 10, 30, 0, timeZone)));
		assertTrue(period.isTimeValid(new TeachUsDate(2007, 1, 1, 14, 30, 0, timeZone)));
		assertTrue(period.isTimeValid(new TeachUsDate(2007, 1, 1, 11, 15, 0, timeZone)));
		assertTrue(period.isTimeValid(new TeachUsDate(2007, 1, 1, 10, 30, 0, timeZone)));
		assertTrue(period.isTimeValid(new TeachUsDate(2007, 1, 1, 14, 45, 0, timeZone)));
		
		assertFalse(period.isTimeValid(new TeachUsDate(2007, 1, 1, 15, 0, 0, timeZone)));
		assertFalse(period.isTimeValid(new TeachUsDate(2007, 1, 1, 9, 0, 0, timeZone)));
		assertFalse(period.isTimeValid(new TeachUsDate(2007, 1, 1, 16, 0, 0, timeZone)));
		assertFalse(period.isTimeValid(new TeachUsDate(2007, 1, 1, 11, 1, 0, timeZone)));
	}
		
	public void testIsTimeValid_15Min_shiftedStartTime() {
		TimeZone timeZone = TimeZone.getDefault();
		
		Period period = new PeriodImpl();
		period.setStartTime(new TeachUsDate(2007, 1, 1, 10, 10, 0, timeZone));
		period.setEndTime(new TeachUsDate(2007, 1, 1, 15, 0, 0, timeZone));
		period.setIntervalBetweenLessonStart(15);
		
		assertTrue(period.isTimeValid(new TeachUsDate(2007, 1, 1, 10, 10, 0, timeZone)));
		assertTrue(period.isTimeValid(new TeachUsDate(2007, 1, 1, 10, 25, 0, timeZone)));
		assertTrue(period.isTimeValid(new TeachUsDate(2007, 1, 1, 14, 55, 0, timeZone)));
		assertTrue(period.isTimeValid(new TeachUsDate(2007, 1, 1, 12, 40, 0, timeZone)));
		
		assertFalse(period.isTimeValid(new TeachUsDate(2007, 1, 1, 15, 0, 0, timeZone)));
		assertFalse(period.isTimeValid(new TeachUsDate(2007, 1, 1, 9, 0, 0, timeZone)));
		assertFalse(period.isTimeValid(new TeachUsDate(2007, 1, 1, 16, 0, 0, timeZone)));
		assertFalse(period.isTimeValid(new TeachUsDate(2007, 1, 1, 11, 1, 0, timeZone)));
		assertFalse(period.isTimeValid(new TeachUsDate(2007, 1, 1, 10, 0, 0, timeZone)));
		assertFalse(period.isTimeValid(new TeachUsDate(2007, 1, 1, 11, 0, 0, timeZone)));
		assertFalse(period.isTimeValid(new TeachUsDate(2007, 1, 1, 14, 0, 0, timeZone)));
		assertFalse(period.isTimeValid(new TeachUsDate(2007, 1, 1, 14, 15, 0, timeZone)));
	}
	
	public void testIsTimeValid_70Min() {
		TimeZone timeZone = TimeZone.getDefault();
		
		Period period = new PeriodImpl();
		period.setStartTime(new TeachUsDate(2007, 1, 1, 10, 0, 0, timeZone));
		period.setEndTime(new TeachUsDate(2007, 1, 1, 15, 0, 0, timeZone));
		period.setIntervalBetweenLessonStart(70);
		
		assertTrue(period.isTimeValid(new TeachUsDate(2007, 1, 1, 10, 0, 0, timeZone)));
		assertTrue(period.isTimeValid(new TeachUsDate(2007, 1, 1, 11, 10, 0, timeZone)));
		assertTrue(period.isTimeValid(new TeachUsDate(2007, 1, 1, 12, 20, 0, timeZone)));
		assertTrue(period.isTimeValid(new TeachUsDate(2007, 1, 1, 13, 30, 0, timeZone)));
		assertTrue(period.isTimeValid(new TeachUsDate(2007, 1, 1, 14, 40, 0, timeZone)));
		
		assertFalse(period.isTimeValid(new TeachUsDate(2007, 1, 1, 11, 0, 0, timeZone)));
		assertFalse(period.isTimeValid(new TeachUsDate(2007, 1, 1, 12, 0, 0, timeZone)));
		assertFalse(period.isTimeValid(new TeachUsDate(2007, 1, 1, 13, 0, 0, timeZone)));
		assertFalse(period.isTimeValid(new TeachUsDate(2007, 1, 1, 14, 0, 0, timeZone)));
		assertFalse(period.isTimeValid(new TeachUsDate(2007, 1, 1, 15, 0, 0, timeZone)));
		assertFalse(period.isTimeValid(new TeachUsDate(2007, 1, 1, 15, 50, 0, timeZone)));
		assertFalse(period.isTimeValid(new TeachUsDate(2007, 1, 1, 10, 10, 0, timeZone)));
		assertFalse(period.isTimeValid(new TeachUsDate(2007, 1, 1, 8, 50, 0, timeZone)));
	}

	public void testMayBook_hour() {
		TimeZone timeZone = TimeZone.getDefault();
		
		Period period = new PeriodImpl();
		period.setStartTime(new TeachUsDate(2007, 1, 1, 10, 0, 0, timeZone));
		period.setEndTime(new TeachUsDate(2007, 1, 1, 15, 0, 0, timeZone));
		period.setIntervalBetweenLessonStart(60);
		period.setLessonDuration(60);

		assertTrue(period.mayBook(new TeachUsDate(2007, 1, 1, 10, 0, 0, timeZone)));
		assertTrue(period.mayBook(new TeachUsDate(2007, 1, 1, 11, 0, 0, timeZone)));
		assertTrue(period.mayBook(new TeachUsDate(2007, 1, 1, 14, 0, 0, timeZone)));
		assertTrue(period.mayBook(new TeachUsDate(2007, 1, 1, 14, 0, 0, 1, timeZone)));
		assertTrue(period.mayBook(new TeachUsDate(2007, 1, 1, 11, 0, 1, timeZone)));
		assertTrue(period.mayBook(new TeachUsDate(2007, 1, 1, 11, 0, 0, 1, timeZone)));
		
		assertFalse(period.mayBook(new TeachUsDate(2007, 1, 1, 15, 0, 0, timeZone)));
		assertFalse(period.mayBook(new TeachUsDate(2007, 1, 1, 9, 0, 0, timeZone)));
		assertFalse(period.mayBook(new TeachUsDate(2007, 1, 1, 12, 30, 0, timeZone)));
		assertFalse(period.mayBook(new TeachUsDate(2007, 1, 1, 16, 0, 0, timeZone)));
		assertFalse(period.mayBook(new TeachUsDate(2007, 1, 1, 11, 1, 0, timeZone)));
	}	
}
