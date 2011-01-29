package dk.teachus.frontend.components.calendar;

import java.util.List;

import org.apache.wicket.markup.html.link.Link;

import dk.teachus.backend.dao.BookingDAO;
import dk.teachus.backend.domain.Bookings;
import dk.teachus.backend.domain.DatePeriod;
import dk.teachus.backend.domain.Period;
import dk.teachus.backend.domain.Periods;
import dk.teachus.backend.domain.TeachUsDate;
import dk.teachus.frontend.TeachUsApplication;
import dk.teachus.frontend.TeachUsSession;
import dk.teachus.frontend.pages.calendar.TeacherCalendarPage;

public class TeacherCalendarPanel extends CalendarPanel {
	private static final long serialVersionUID = 1L;
	
	private Bookings bookings;
	
	public TeacherCalendarPanel(String wicketId, TeachUsDate pageDate, Periods periods) {
		super(wicketId, pageDate, periods);
	}

	@Override
	protected Link createBackLink(String wicketId, final TeachUsDate previousWeekDate, final int numberOfWeeks) {
		return new Link(wicketId) {
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick() {
				getRequestCycle().setResponsePage(new TeacherCalendarPage(previousWeekDate));
			}		
			
			@Override
			public boolean isEnabled() {
				return numberOfWeeks > 0;
			}	
		};
	}

	@Override
	protected Link createForwardLink(String wicketId, final TeachUsDate nextWeekDate) {
		return new Link(wicketId) {
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick() {
				getRequestCycle().setResponsePage(new TeacherCalendarPage(nextWeekDate));
			}			
		};
	}
	
	@Override
	protected void onIntervalDetermined(List<DatePeriod> dates, TeachUsDate firstDate, TeachUsDate lastDate) {
		BookingDAO bookingDAO = TeachUsApplication.get().getBookingDAO();
		bookings = bookingDAO.getBookings(TeachUsSession.get().getTeacher(), firstDate, lastDate);
	}

	@Override
	protected PeriodDateComponent createPeriodDateComponent(String wicketId, Period period, TeachUsDate date) {
		return new TeacherPeriodDateComponent(wicketId, TeachUsSession.get().getTeacher(), period, date, bookings);
	}
	
	@Override
	protected void navigationDateSelected(TeachUsDate date) {
		getRequestCycle().setResponsePage(new TeacherCalendarPage(date));
	}
	
}
