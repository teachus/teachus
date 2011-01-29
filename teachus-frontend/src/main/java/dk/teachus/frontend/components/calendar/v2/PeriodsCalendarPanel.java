package dk.teachus.frontend.components.calendar.v2;

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
import org.joda.time.DateTimeConstants;
import org.joda.time.LocalTime;

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

public class PeriodsCalendarPanel extends CalendarPanelV2 {
	private static final long serialVersionUID = 1L;
	
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
	protected IModel<List<TimeSlot>> getTimeSlotModel(final TeachUsDate date) {
		return new AbstractReadOnlyModel<List<TimeSlot>>() {
			private static final long serialVersionUID = 1L;

			@Override
			public List<TimeSlot> getObject() {
				List<TimeSlot> timeSlots = new ArrayList<TimeSlot>();
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
						TeachUsDate startTime = period.getStartTime();
						TeachUsDate endTime = period.getEndTime();
						
						TeachUsDate time = startTime;
						while (time.isBefore(endTime)) {
							timeSlots.add(new TimeSlot(time.getLocalTime(), time.getLocalTime().plusMinutes(period.getLessonDuration()), period));
							
							time = time.plusMinutes(period.getIntervalBetweenLessonStart());
						}
					}
				}
				
				return timeSlots;
			}
			
			@Override
			public void detach() {
				periodsModel.detach();
			}
		};
	}

	@Override
	protected IModel<String> getTimeSlotContentModel(final TeachUsDate date, final TimeSlot timeSlot, final ListItem<TimeSlot> timeSlotItem) {
		final IModel<TeachUsDate> timeModel = new LoadableDetachableModel<TeachUsDate>() {
			private static final long serialVersionUID = 1L;

			@Override
			protected TeachUsDate load() {
				return date.toLocalTime(timeSlot.getStartTime());
			}
		};
		
		final IModel<Booking> bookingModel = new LoadableDetachableModel<Booking>() {
			private static final long serialVersionUID = 1L;

			@Override
			protected Booking load() {
				Period period = (Period) timeSlot.getPayload();
				Bookings bookings = bookingsModel.getObject();
				Booking booking = bookings.getBooking(period, timeModel.getObject());
				return booking;
			}
			
			@Override
			protected void onDetach() {
				timeModel.detach();
				bookingsModel.detach();
			}
		};
		
		// Click action
		timeSlotItem.add(new AjaxEventBehavior("onclick") { //$NON-NLS-1$
			private static final long serialVersionUID = 1L;

			@Override
			protected void onEvent(AjaxRequestTarget target) {
				Booking booking = bookingModel.getObject();
				if (booking != null) {
					TeachUsApplication.get().getBookingDAO().deleteBooking(booking);
				} else {
					TeacherBooking teacherBooking = TeachUsApplication.get().getBookingDAO().createTeacherBookingObject();
					teacherBooking.setActive(true);
					teacherBooking.setDate(timeModel.getObject());
					teacherBooking.setPeriod((Period) timeSlotItem.getModelObject().getPayload());
					teacherBooking.setTeacher(TeachUsSession.get().getTeacher());
					TeachUsApplication.get().getBookingDAO().book(teacherBooking);
				}
				
				bookingModel.detach();
				
				target.addComponent(timeSlotItem);
			}
			
			@Override
			public boolean isEnabled(Component component) {
				Booking booking = bookingModel.getObject();
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
				Booking booking = bookingModel.getObject();
				if (booking != null) {
					if (booking instanceof PupilBooking) {
						return "pupilBooked"; //$NON-NLS-1$
					}
					
					return "booked"; //$NON-NLS-1$
				}
				
				return null;
			}
		}, " ")); //$NON-NLS-1$
		
		return new AbstractReadOnlyModel<String>() {
			private static final long serialVersionUID = 1L;

			@Override
			public String getObject() {
				if (bookingModel.getObject() != null) {
					if (bookingModel.getObject() instanceof PupilBooking) {
						PupilBooking pupilBooking = (PupilBooking) bookingModel.getObject();
						return pupilBooking.getPupil().getName();
					}
					
					return TeachUsSession.get().getString("PeriodsCalendarPanel.booked"); //$NON-NLS-1$
				}
				
				return null;
			}
			
			@Override
			public void detach() {
				bookingModel.detach();
			}
		};
	}
	
}
