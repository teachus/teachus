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
package dk.teachus.frontend.components.calendar;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.util.string.Strings;
import org.joda.time.DateTimeConstants;
import org.joda.time.LocalTime;
import org.joda.time.PeriodType;

import dk.teachus.backend.dao.BookingDAO;
import dk.teachus.backend.domain.Booking;
import dk.teachus.backend.domain.Bookings;
import dk.teachus.backend.domain.DatePeriod;
import dk.teachus.backend.domain.Period;
import dk.teachus.backend.domain.Periods;
import dk.teachus.backend.domain.TeachUsDate;
import dk.teachus.backend.domain.Teacher;
import dk.teachus.backend.domain.impl.CalendarNarrowTimesTeacherAttribute;
import dk.teachus.frontend.TeachUsApplication;
import dk.teachus.frontend.TeachUsSession;
import dk.teachus.frontend.components.calendar.PeriodsCalendarPanel.PeriodBookingTimeSlotPayload;
import dk.teachus.frontend.utils.Formatters;

public abstract class PeriodsCalendarPanel extends CalendarPanel<PeriodBookingTimeSlotPayload> {
	private static final long serialVersionUID = 1L;
	
	public static class PeriodBookingTimeSlotPayload implements Serializable {
		private static final long serialVersionUID = 1L;
		
		private Period period;
		private Booking booking;
		
		public Period getPeriod() {
			return period;
		}
		
		public void setPeriod(Period period) {
			this.period = period;
		}
		
		public Booking getBooking() {
			return booking;
		}
		
		public void setBooking(Booking booking) {
			this.booking = booking;
		}
	}
	
	private class DefaultPeriodsModel extends LoadableDetachableModel<Periods> {
		private static final long serialVersionUID = 1L;

		@Override
		protected Periods load() {
			return TeachUsApplication.get().getPeriodDAO().getPeriods(getTeacher(), true);
		}
	}
	
	private IModel<List<DatePeriod>> datePeriodsModel;

	private IModel<Bookings> bookingsModel;

	public PeriodsCalendarPanel(String id, IModel<TeachUsDate> weekDateModel) {
		this(id, weekDateModel, null);
	}
	
	public PeriodsCalendarPanel(String id, IModel<TeachUsDate> weekDateModel, IModel<Periods> periodsModel) {
		super(id, weekDateModel);
		
		final IModel<Periods> thePeriodsModel;
		if (periodsModel == null) {
			thePeriodsModel = new DefaultPeriodsModel();
		} else {
			thePeriodsModel = periodsModel;
		}
		
		this.datePeriodsModel = new LoadableDetachableModel<List<DatePeriod>>() {
			private static final long serialVersionUID = 1L;
			
			@Override
			protected List<DatePeriod> load() {
				Periods periods = thePeriodsModel.getObject();
				return periods.generateDatesForWeek(PeriodsCalendarPanel.this.getModelObject());
			}
			
			protected void onDetach() {
				thePeriodsModel.detach();
			}
		};
		
		bookingsModel = new LoadableDetachableModel<Bookings>() {
			private static final long serialVersionUID = 1L;

			@Override
			protected Bookings load() {
				BookingDAO bookingDAO = TeachUsApplication.get().getBookingDAO();
				Bookings bookings = bookingDAO.getBookings(getTeacher(), getFromDate(), getToDate());
				return bookings;
			}
		};
	}
	
	@Override
	protected LocalTime getCalendarStartTime() {
		LocalTime calendarStartTime = super.getCalendarStartTime();

		CalendarNarrowTimesTeacherAttribute narrowTimes = TeachUsSession.get().getTeacherAttribute(CalendarNarrowTimesTeacherAttribute.class);
		if (narrowTimes != null && narrowTimes.getBooleanValue()) {
			LocalTime earliestStart = new LocalTime(23, 59, 59, 999);
			List<DatePeriod> periods = datePeriodsModel.getObject();
			for (DatePeriod datePeriod : periods) {
				List<Period> periodList = datePeriod.getPeriods();
				for (Period period : periodList) {
					LocalTime periodStartTime = period.getStartTime().getLocalTime();
					if (periodStartTime.isBefore(earliestStart)) {
						earliestStart = periodStartTime;
					}
				}
			}
			calendarStartTime = earliestStart;
		}
		
		return calendarStartTime;
	}
	
	@Override
	protected LocalTime getCalendarEndTime() {
		LocalTime calendarEndTime = super.getCalendarEndTime();

		CalendarNarrowTimesTeacherAttribute narrowTimes = TeachUsSession.get().getTeacherAttribute(CalendarNarrowTimesTeacherAttribute.class);
		if (narrowTimes != null && narrowTimes.getBooleanValue()) {
			LocalTime latestEnd = new LocalTime(0, 0, 0, 0);
			List<DatePeriod> periods = datePeriodsModel.getObject();
			for (DatePeriod datePeriod : periods) {
				List<Period> periodList = datePeriod.getPeriods();
				for (Period period : periodList) {
					LocalTime periodEndTime = period.getEndTime().getLocalTime();
					if (periodEndTime.isAfter(latestEnd)) {
						latestEnd = periodEndTime;
					}
				}
			}
			calendarEndTime = latestEnd;
		}
		
		return calendarEndTime;
	}
	
	private TeachUsDate getFromDate() {
		return getModelObject().withDayOfWeek(DateTimeConstants.MONDAY);
	}
	
	private TeachUsDate getToDate() {
		return getModelObject().withDayOfWeek(DateTimeConstants.SUNDAY);
	}

	@Override
	protected IModel<List<TimeSlot<PeriodBookingTimeSlotPayload>>> getTimeSlotModel(final TeachUsDate date) {		
		return new AbstractReadOnlyModel<List<TimeSlot<PeriodBookingTimeSlotPayload>>>() {
			private static final long serialVersionUID = 1L;

			@Override
			public List<TimeSlot<PeriodBookingTimeSlotPayload>> getObject() {
				List<TimeSlot<PeriodBookingTimeSlotPayload>> timeSlots = new ArrayList<TimeSlot<PeriodBookingTimeSlotPayload>>();
				List<DatePeriod> periods = datePeriodsModel.getObject();
				DatePeriod currentDatePeriod = null;
				for (DatePeriod datePeriod : periods) {
					if (datePeriod.getDate().getDateMidnight().equals(date.getDateMidnight())) {
						currentDatePeriod = datePeriod;
						break;
					}
				}
				
				if (currentDatePeriod != null) {
					List<Period> periodsList = currentDatePeriod.getPeriods();
					for (Period period : periodsList) {
						TeachUsDate startTime = period.getStartTime().withDate(date);
						TeachUsDate endTime = period.getEndTime().withDate(date);
						
						TeachUsDate time = startTime;
						while (time.isBefore(endTime)) {
							/*
							 * Booking
							 */
							Bookings bookings = bookingsModel.getObject();
							Booking booking = bookings.getBooking(period, time);
							
							PeriodBookingTimeSlotPayload payload = new PeriodBookingTimeSlotPayload();
							payload.setPeriod(period);
							payload.setBooking(booking);
							
							timeSlots.add(new TimeSlot<PeriodBookingTimeSlotPayload>(time.getLocalTime(), time.getLocalTime().plusMinutes(period.getLessonDuration()), payload));
							
							time = time.plusMinutes(period.getIntervalBetweenLessonStart());
						}
					}
				}
				
				return timeSlots;
			}
			
			@Override
			public void detach() {
				datePeriodsModel.detach();
				bookingsModel.detach();
			}
		};
	}

	@Override
	protected final List<String> getTimeSlotContent(final TeachUsDate date, final TimeSlot<PeriodBookingTimeSlotPayload> timeSlot, final ListItem<TimeSlot<PeriodBookingTimeSlotPayload>> timeSlotItem) {
		// Click action
		timeSlotItem.add(new AjaxEventBehavior("onclick") { //$NON-NLS-1$
			private static final long serialVersionUID = 1L;

			@Override
			protected void onEvent(AjaxRequestTarget target) {
				onTimeSlotClicked(timeSlot, date.withTime(timeSlot.getStartTime()), target);
				
				target.addComponent(timeSlotItem);
			}
			
			@Override
			public boolean isEnabled(Component component) {
				return isTimeSlotBookable(timeSlot);
			}
		});
		
		timeSlotItem.add(new AttributeModifier("class", true, new AbstractReadOnlyModel<String>() { //$NON-NLS-1$
			private static final long serialVersionUID = 1L;

			@Override
			public String getObject() {
				return getTimeSlotClass(timeSlot);
			}
		}) {
			private static final long serialVersionUID = 1L;

			@Override
			protected String newValue(String currentValue, String replacementValue) {
				return "daytimelesson" + (replacementValue != null ? " "+replacementValue : ""); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			}
		});
		
		List<String> contentLines = new ArrayList<String>();
		Period period = timeSlot.getPayload().getPeriod();
		
		// Time line
		TeachUsDate startTime = date.withTime(timeSlot.getStartTime());
		TeachUsDate endTime = date.withTime(timeSlot.getEndTime());
		org.joda.time.Period minutesDuration = new org.joda.time.Period(startTime.getDateTime(), endTime.getDateTime(), PeriodType.minutes());
		String timePriceLine = TIME_FORMAT.print(startTime.getDateTime()) + "-" + TIME_FORMAT.print(endTime.getDateTime()) + " - "+Math.round(minutesDuration.getMinutes())+"m"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		if (period.getPrice() > 0) {
			timePriceLine += " - "+Formatters.getFormatCurrency().format(period.getPrice()); //$NON-NLS-1$
		}
		contentLines.add(timePriceLine);
		
		// Period
		if (Strings.isEmpty(period.getLocation()) == false) {
			contentLines.add(period.getLocation());
		}

		appendToTimeSlotContent(contentLines, timeSlot);
		
		return contentLines;
	}
	
	protected abstract Teacher getTeacher();
	
	protected abstract boolean isTimeSlotBookable(TimeSlot<PeriodBookingTimeSlotPayload> timeSlot);
	
	protected void appendToTimeSlotContent(List<String> contentLines, TimeSlot<PeriodBookingTimeSlotPayload> timeSlot) {
	}
	
	/**
	 * @param timeSlot
	 * @return CSS class or classes (separated by space), which is added to the timeslot UI item for additional UI
	 */
	protected String getTimeSlotClass(TimeSlot<PeriodBookingTimeSlotPayload> timeSlot) {
		return null;
	}
	
	protected void onTimeSlotClicked(TimeSlot<PeriodBookingTimeSlotPayload> timeSlot, TeachUsDate date, AjaxRequestTarget target) {
	}
	
}
