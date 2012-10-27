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
import org.joda.time.DateMidnight;
import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.LocalTime;
import org.joda.time.PeriodType;

import dk.teachus.backend.dao.BookingDAO;
import dk.teachus.backend.domain.Booking;
import dk.teachus.backend.domain.Bookings;
import dk.teachus.backend.domain.DatePeriod;
import dk.teachus.backend.domain.Period;
import dk.teachus.backend.domain.Periods;
import dk.teachus.backend.domain.Teacher;
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
		
		public void setPeriod(final Period period) {
			this.period = period;
		}
		
		public Booking getBooking() {
			return booking;
		}
		
		public void setBooking(final Booking booking) {
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
	
	public PeriodsCalendarPanel(final String id, final IModel<DateMidnight> weekDateModel) {
		this(id, weekDateModel, null);
	}
	
	public PeriodsCalendarPanel(final String id, final IModel<DateMidnight> weekDateModel, final IModel<Periods> periodsModel) {
		super(id, weekDateModel);
		
		final IModel<Periods> thePeriodsModel;
		if (periodsModel == null) {
			thePeriodsModel = new DefaultPeriodsModel();
		} else {
			thePeriodsModel = periodsModel;
		}
		
		datePeriodsModel = new LoadableDetachableModel<List<DatePeriod>>() {
			private static final long serialVersionUID = 1L;
			
			@Override
			protected List<DatePeriod> load() {
				final Periods periods = thePeriodsModel.getObject();
				return periods.generateDatesForWeek(PeriodsCalendarPanel.this.getModelObject());
			}
			
			@Override
			protected void onDetach() {
				thePeriodsModel.detach();
			}
		};
		
		bookingsModel = new LoadableDetachableModel<Bookings>() {
			private static final long serialVersionUID = 1L;
			
			@Override
			protected Bookings load() {
				final BookingDAO bookingDAO = TeachUsApplication.get().getBookingDAO();
				final Bookings bookings = bookingDAO.getBookings(getTeacher(), getFromDate(), getToDate());
				return bookings;
			}
		};
	}
	
	@Override
	protected LocalTime getCalendarStartTime() {
		LocalTime calendarStartTime = super.getCalendarStartTime();
		
		final boolean narrowTimes = TeachUsSession.get().getTeacherAttribute().getCalendarNarrowTimes();
		if (narrowTimes) {
			LocalTime earliestStart = new LocalTime(23, 59, 59, 999);
			final List<DatePeriod> periods = datePeriodsModel.getObject();
			for (final DatePeriod datePeriod : periods) {
				final List<Period> periodList = datePeriod.getPeriods();
				for (final Period period : periodList) {
					final LocalTime periodStartTime = period.getStartTime();
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
		
		final boolean narrowTimes = TeachUsSession.get().getTeacherAttribute().getCalendarNarrowTimes();
		if (narrowTimes) {
			LocalTime latestEnd = new LocalTime(0, 0, 0, 0);
			final List<DatePeriod> periods = datePeriodsModel.getObject();
			for (final DatePeriod datePeriod : periods) {
				final List<Period> periodList = datePeriod.getPeriods();
				for (final Period period : periodList) {
					final LocalTime periodEndTime = period.getEndTime();
					if (periodEndTime.isAfter(latestEnd)) {
						latestEnd = periodEndTime;
					}
				}
			}
			calendarEndTime = latestEnd;
		}
		
		return calendarEndTime;
	}
	
	private DateMidnight getFromDate() {
		return getModelObject().withDayOfWeek(DateTimeConstants.MONDAY);
	}
	
	private DateMidnight getToDate() {
		return getModelObject().withDayOfWeek(DateTimeConstants.SUNDAY);
	}
	
	@Override
	protected IModel<List<TimeSlot<PeriodBookingTimeSlotPayload>>> getTimeSlotModel(final DateMidnight date) {
		return new AbstractReadOnlyModel<List<TimeSlot<PeriodBookingTimeSlotPayload>>>() {
			private static final long serialVersionUID = 1L;
			
			@Override
			public List<TimeSlot<PeriodBookingTimeSlotPayload>> getObject() {
				final List<TimeSlot<PeriodBookingTimeSlotPayload>> timeSlots = new ArrayList<TimeSlot<PeriodBookingTimeSlotPayload>>();
				final List<DatePeriod> periods = datePeriodsModel.getObject();
				DatePeriod currentDatePeriod = null;
				for (final DatePeriod datePeriod : periods) {
					if (datePeriod.getDate().equals(date)) {
						currentDatePeriod = datePeriod;
						break;
					}
				}
				
				if (currentDatePeriod != null) {
					final List<Period> periodsList = currentDatePeriod.getPeriods();
					for (final Period period : periodsList) {
						final DateTime startTime = period.getStartTime().toDateTime(date);
						final DateTime endTime = period.getEndTime().toDateTime(date);
						
						DateTime time = startTime;
						while (time.isBefore(endTime)) {
							/*
							 * Booking
							 */
							final Bookings bookings = bookingsModel.getObject();
							final Booking booking = bookings.getBooking(period, time);
							
							final PeriodBookingTimeSlotPayload payload = new PeriodBookingTimeSlotPayload();
							payload.setPeriod(period);
							payload.setBooking(booking);
							
							timeSlots.add(new TimeSlot<PeriodBookingTimeSlotPayload>(time.toLocalTime(), time.toLocalTime().plusMinutes(
									period.getLessonDuration()), payload));
							
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
	protected final List<String> getTimeSlotContent(final DateMidnight date, final TimeSlot<PeriodBookingTimeSlotPayload> timeSlot,
			final ListItem<TimeSlot<PeriodBookingTimeSlotPayload>> timeSlotItem) {
		// Click action
		timeSlotItem.add(new AjaxEventBehavior("onclick") { //$NON-NLS-1$
					private static final long serialVersionUID = 1L;
					
					@Override
					protected void onEvent(final AjaxRequestTarget target) {
						onTimeSlotClicked(timeSlot, timeSlot.getStartTime().toDateTime(date), target);
						
						target.add(timeSlotItem);
					}
					
					@Override
					public boolean isEnabled(final Component component) {
						return isTimeSlotBookable(timeSlot);
					}
				});
		
		timeSlotItem.add(AttributeModifier.append("class", new AbstractReadOnlyModel<String>() { //$NON-NLS-1$
					private static final long serialVersionUID = 1L;
					
					@Override
					public String getObject() {
						return getTimeSlotClass(timeSlot);
					}
				}));
		
		final List<String> contentLines = new ArrayList<String>();
		final Period period = timeSlot.getPayload().getPeriod();
		
		// Time line
		final DateTime startTime = timeSlot.getStartTime().toDateTime(date);
		final DateTime endTime = timeSlot.getEndTime().toDateTime(date);
		final org.joda.time.Period minutesDuration = new org.joda.time.Period(startTime, endTime, PeriodType.minutes());
		String timePriceLine = CalendarPanel.TIME_FORMAT.print(startTime)
				+ "-" + CalendarPanel.TIME_FORMAT.print(endTime) + " - " + Math.round(minutesDuration.getMinutes()) + "m"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		if (period.getPrice() > 0) {
			timePriceLine += " - " + Formatters.getFormatCurrency().format(period.getPrice()); //$NON-NLS-1$
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
	
	protected void appendToTimeSlotContent(final List<String> contentLines, final TimeSlot<PeriodBookingTimeSlotPayload> timeSlot) {
	}
	
	/**
	 * @param timeSlot
	 * @return CSS class or classes (separated by space), which is added to the timeslot UI item for additional UI
	 */
	protected String getTimeSlotClass(final TimeSlot<PeriodBookingTimeSlotPayload> timeSlot) {
		return null;
	}
	
	protected void onTimeSlotClicked(final TimeSlot<PeriodBookingTimeSlotPayload> timeSlot, final DateTime date, final AjaxRequestTarget target) {
	}
	
}
