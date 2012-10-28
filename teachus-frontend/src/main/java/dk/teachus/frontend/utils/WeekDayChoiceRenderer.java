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
package dk.teachus.frontend.utils;

import java.util.List;

import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;

import dk.teachus.backend.domain.WeekDay;

public class WeekDayChoiceRenderer extends ChoiceRenderer<Object> {
	private static final long serialVersionUID = 1L;
	
	public static enum Format {
		SHORT,
		LONG;
	}
	
	private Format format;
	
	public WeekDayChoiceRenderer(Format format) {
		this.format = format;
	}

	@SuppressWarnings("unchecked") 
	@Override
	public Object getDisplayValue(Object object) {
		StringBuilder display = new StringBuilder();
		DateTimeFormatter formatter = null;
		if (format == Format.LONG) {
			formatter = Formatters.getFormatWeekDay();
		} else if (format == Format.SHORT) {
			formatter = Formatters.getFormatWeekDayShort();
		}
		
		if (object != null) {
			if (object instanceof List) {
				DateTime dt = new DateTime();
				List<WeekDay> weekDays = (List<WeekDay>) object;
				
				for (WeekDay weekDay : weekDays) {
					if (display.length() > 0) {
						display.append(", "); //$NON-NLS-1$
					}
				
					dt = dt.withDayOfWeek(weekDay.getYodaWeekDay());
					display.append(formatter.print(dt));

				}
			} else if (object instanceof WeekDay) {
				WeekDay weekDay = (WeekDay) object;
				DateTime dt = new DateTime().withDayOfWeek(weekDay.getYodaWeekDay());
				display.append(formatter.print(dt));
			}
		}
		
		return display;
	}
}
