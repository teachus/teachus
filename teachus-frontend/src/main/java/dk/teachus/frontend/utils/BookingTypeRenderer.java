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

import org.apache.commons.lang.ClassUtils;
import org.apache.wicket.markup.html.form.ChoiceRenderer;

import dk.teachus.backend.domain.Booking;
import dk.teachus.backend.domain.PupilBooking;
import dk.teachus.backend.domain.TeacherBooking;
import dk.teachus.frontend.TeachUsSession;

public class BookingTypeRenderer extends ChoiceRenderer<Class<? extends Booking>> {
	private static final long serialVersionUID = 1L;

	@Override
	public Object getDisplayValue(Class<? extends Booking> bookingClass) {
		String display = ""; //$NON-NLS-1$
		
		if (bookingClass != null) {
			if (ClassUtils.isAssignable(bookingClass, PupilBooking.class)) {
				display = TeachUsSession.get().getString("BookingType.pupil");
			} else if (ClassUtils.isAssignable(bookingClass, TeacherBooking.class)) {
				display = TeachUsSession.get().getString("BookingType.teacher");				
			} else {
				throw new IllegalArgumentException("Unsupported booking type: "+bookingClass);
			}
		}
		
		return display;
	}
}
