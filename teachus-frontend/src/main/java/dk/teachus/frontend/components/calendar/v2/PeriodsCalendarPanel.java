	package dk.teachus.frontend.components.calendar.v2;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.util.string.Strings;
import org.joda.time.DateTimeConstants;
import org.joda.time.LocalTime;
import org.joda.time.PeriodType;

import dk.teachus.backend.domain.Booking;
import dk.teachus.backend.domain.Bookings;
import dk.teachus.backend.domain.DatePeriod;
import dk.teachus.backend.domain.Period;
import dk.teachus.backend.domain.Periods;
import dk.teachus.backend.domain.PupilBooking;
import dk.teachus.backend.domain.TeachUsDate;
import dk.teachus.backend.domain.TeacherBooking;
import dk.teachus.backend.domain.impl.NewCalendarNarrowTimesTeacherAttribute;
import dk.teachus.frontend.TeachUsApplication;
import dk.teachus.frontend.TeachUsSession;
import dk.teachus.frontend.components.calendar.v2.PeriodsCalendarPanel.PeriodBookingTimeSlotPayload;
import dk.teachus.frontend.utils.Formatters;

public class PeriodsCalendarPanel extends CalendarPanelV2<PeriodBookingTimeSlotPayload> {
	private static final long serialVersionUID = 1L;
	
	static class PeriodBookingTimeSlotPayload implements Serializable {
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
	
	private IModel<List<DatePeriod>> periodsModel;

	private IModel<Bookings> bookingsModel;
	
	public PeriodsCalendarPanel(String id, IModel<TeachUsDate> weekDateModel) {
		super(id, weekDateModel);
		
		periodsModel = new LoadableDetachableModel<List<DatePeriod>>() {
			private static final long serialVersionUID = 1L;

			@Override
			protected List<DatePeriod> load() {
				Periods periods = TeachUsApplication.get().getPeriodDAO().getPeriods(TeachUsSession.get().getTeacher(), true);
				return periods.generateDatesForWeek(PeriodsCalendarPanel.this.getModelObject());
			}
		};
		
		bookingsModel = new LoadableDetachableModel<Bookings>() {
			private static final long serialVersionUID = 1L;

			@Override
			protected Bookings load() {
				return TeachUsApplication.get().getBookingDAO().getBookings(TeachUsSession.get().getTeacher(), getFromDate(), getToDate());
			}
		};
	}
	
	@Override
	protected LocalTime getCalendarStartTime() {
		LocalTime calendarStartTime = super.getCalendarStartTime();

		NewCalendarNarrowTimesTeacherAttribute narrowTimes = TeachUsSession.get().getTeacherAttribute(NewCalendarNarrowTimesTeacherAttribute.class);
		if (narrowTimes != null && narrowTimes.getBooleanValue()) {
			LocalTime earliestStart = new LocalTime(23, 59, 59, 999);
			List<DatePeriod> periods = periodsModel.getObject();
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

		NewCalendarNarrowTimesTeacherAttribute narrowTimes = TeachUsSession.get().getTeacherAttribute(NewCalendarNarrowTimesTeacherAttribute.class);
		if (narrowTimes != null && narrowTimes.getBooleanValue()) {
			LocalTime latestEnd = new LocalTime(0, 0, 0, 0);
			List<DatePeriod> periods = periodsModel.getObject();
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
				List<DatePeriod> periods = periodsModel.getObject();
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
				periodsModel.detach();
				bookingsModel.detach();
			}
		};
	}

	@Override
	protected List<String> getTimeSlotContent(final TeachUsDate date, final TimeSlot<PeriodBookingTimeSlotPayload> timeSlot, final ListItem<TimeSlot<PeriodBookingTimeSlotPayload>> timeSlotItem) {
		final IModel<TeachUsDate> timeModel = new LoadableDetachableModel<TeachUsDate>() {
			private static final long serialVersionUID = 1L;

			@Override
			protected TeachUsDate load() {
				return date.withTime(timeSlot.getStartTime());
			}
		};
		
		// Click action
		timeSlotItem.add(new AjaxEventBehavior("onclick") { //$NON-NLS-1$
			private static final long serialVersionUID = 1L;

			@Override
			protected void onEvent(AjaxRequestTarget target) {
				PeriodBookingTimeSlotPayload payload = timeSlot.getPayload();
				Booking booking = payload.getBooking();
				if (booking != null) {
					TeachUsApplication.get().getBookingDAO().deleteBooking(booking);
					payload.setBooking(null);
				} else {
					TeacherBooking teacherBooking = TeachUsApplication.get().getBookingDAO().createTeacherBookingObject();
					teacherBooking.setActive(true);
					teacherBooking.setDate(timeModel.getObject());
					teacherBooking.setPeriod(payload.getPeriod());
					teacherBooking.setTeacher(TeachUsSession.get().getTeacher());
					TeachUsApplication.get().getBookingDAO().book(teacherBooking);
					payload.setBooking(teacherBooking);
				}
				
				target.addComponent(timeSlotItem);
			}
			
			@Override
			public boolean isEnabled(Component component) {
				Booking booking = timeSlot.getPayload().getBooking();
				if (booking != null) {
					if (booking instanceof PupilBooking) {
						return false;
					}
				}
				
				return true;
			}
		});
		
		timeSlotItem.add(new AttributeAppender("class", true, new AbstractReadOnlyModel<String>() { //$NON-NLS-1$
			private static final long serialVersionUID = 1L;

			@Override
			public String getObject() {
				Booking booking = timeSlot.getPayload().getBooking();
				if (booking != null) {
					if (booking instanceof PupilBooking) {
						return "pupilBooked"; //$NON-NLS-1$
					}
					
					return "booked"; //$NON-NLS-1$
				}
				
				return null;
			}
		}, " ")); //$NON-NLS-1$
		
		List<String> contentLines = new ArrayList<String>();
		Period period = timeSlot.getPayload().getPeriod();
		
		// Time line
		TeachUsDate startTime = date.withTime(timeSlot.getStartTime());
		TeachUsDate endTime = date.withTime(timeSlot.getEndTime());
		org.joda.time.Period minutesDuration = new org.joda.time.Period(startTime.getDateTime(), endTime.getDateTime(), PeriodType.minutes());
		String timePriceLine = TIME_FORMAT.print(startTime.getDateTime()) + "-" + TIME_FORMAT.print(endTime.getDateTime()) + " - "+Math.round(minutesDuration.getMinutes())+"m";
		if (period.getPrice() > 0) {
			timePriceLine += " - "+Formatters.getFormatCurrency().format(period.getPrice());
		}
		contentLines.add(timePriceLine);
		
		// Period
		if (Strings.isEmpty(period.getLocation()) == false) {
			contentLines.add(period.getLocation());
		}
		
		Booking booking = timeSlot.getPayload().getBooking();
		if (booking != null) {
			if (booking instanceof PupilBooking) {
				PupilBooking pupilBooking = (PupilBooking) booking;
				contentLines.add(pupilBooking.getPupil().getName());
			} else {
				contentLines.add(TeachUsSession.get().getString("PeriodsCalendarPanel.booked")); //$NON-NLS-1$
			}
		}
		
		return contentLines;
	}
	
	@Override
	protected Component createTimeSlotDetailsComponent(String wicketId, TimeSlot<PeriodBookingTimeSlotPayload> timeSlot) {
		Booking booking = timeSlot.getPayload().getBooking();
		if (booking instanceof PupilBooking) {
			return new PeriodBookingTimeSlotDetails(wicketId, (PupilBooking) booking);
		} else {
			return super.createTimeSlotDetailsComponent(wicketId, timeSlot);
		}
	}
	
}
