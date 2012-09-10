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

import java.util.Date;

import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.joda.time.DateTime;
import org.joda.time.ReadableInstant;
import org.joda.time.ReadablePartial;

public class TimeChoiceRenderer<T> extends ChoiceRenderer<T> {
	private static final long serialVersionUID = 1L;

	@Override
	public Object getDisplayValue(T object) {
		String display = ""; //$NON-NLS-1$
		
		if (object != null) {
			if (object instanceof Integer) {
				Integer minutesOfDay = (Integer) object;
				DateTime dt = new DateTime().withTime(0, 0, 0, 0).plusMinutes(minutesOfDay);
				display = Formatters.getFormatTime().print(dt);
			} else if (object instanceof Date) {
				Date date = (Date) object;
				display = Formatters.getFormatTime().print(new DateTime(date));
			} else if (object instanceof ReadableInstant) {
				ReadableInstant date = (ReadableInstant) object;
				display = Formatters.getFormatTime().print(date);
			} else if (object instanceof ReadablePartial) {
				ReadablePartial date = (ReadablePartial) object;
				display = Formatters.getFormatTime().print(date);
			}
		}
		
		return display;
	}
}
