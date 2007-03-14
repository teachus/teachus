package dk.teachus.frontend.pages.calendar;

import java.util.Date;
import java.util.List;

import org.joda.time.DateMidnight;

import wicket.RestartResponseAtInterceptPageException;
import wicket.markup.html.link.Link;
import wicket.protocol.http.WebApplication;
import dk.teachus.dao.BookingDAO;
import dk.teachus.dao.PeriodDAO;
import dk.teachus.domain.Bookings;
import dk.teachus.domain.DatePeriod;
import dk.teachus.domain.Period;
import dk.teachus.domain.Periods;
import dk.teachus.domain.Teacher;
import dk.teachus.frontend.TeachUsApplication;
import dk.teachus.frontend.TeachUsSession;
import dk.teachus.frontend.UserLevel;
import dk.teachus.frontend.components.calendar.CalendarPanel;
import dk.teachus.frontend.components.calendar.PeriodDateComponent;
import dk.teachus.frontend.components.calendar.TeacherPeriodDateComponent;
import dk.teachus.frontend.pages.AuthenticatedBasePage;

public class TeacherCalendarPage extends AuthenticatedBasePage {
	private static final long serialVersionUID = 1L;

	private Teacher teacher;
	
	public TeacherCalendarPage() {
		this(new DateMidnight());
	}
	
	public TeacherCalendarPage(DateMidnight pageDate) {
		super(UserLevel.TEACHER, true);
		
		if (TeachUsSession.get().getUserLevel() != UserLevel.TEACHER) {
			throw new RestartResponseAtInterceptPageException(WebApplication.get().getHomePage());
		}
		
		PeriodDAO periodDAO = TeachUsApplication.get().getPeriodDAO();
		
		teacher = (Teacher) TeachUsSession.get().getPerson();		
		
		final Periods periods = periodDAO.getPeriods(teacher);
		
		add(new CalendarPanel("calendar", pageDate, periods) { //$NON-NLS-1$
			private static final long serialVersionUID = 1L;
			private Bookings bookings;

			@Override
			protected Link createBackLink(String wicketId, final DateMidnight previousWeekDate, final int numberOfWeeks) {
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
			protected Link createForwardLink(String wicketId, final DateMidnight nextWeekDate) {
				return new Link(wicketId) {
					private static final long serialVersionUID = 1L;
		
					@Override
					public void onClick() {
						getRequestCycle().setResponsePage(new TeacherCalendarPage(nextWeekDate));
					}			
				};
			}
			
			@Override
			protected void onIntervalDetermined(List<DatePeriod> dates, DateMidnight firstDate, DateMidnight lastDate) {
				BookingDAO bookingDAO = TeachUsApplication.get().getBookingDAO();
				bookings = bookingDAO.getBookings(teacher, firstDate, lastDate);
			}

			@Override
			protected PeriodDateComponent createPeriodDateComponent(String wicketId, Period period, Date date) {
				DateMidnight dateMidnight = new DateMidnight(date);
				
				return new TeacherPeriodDateComponent(wicketId, teacher, period, dateMidnight, bookings);
			}
			
			@Override
			protected void navigationDateSelected(DateMidnight date) {
				getRequestCycle().setResponsePage(new TeacherCalendarPage(date));
			}
			
		});
	}

	@Override
	protected String getPageLabel() {
		return TeachUsSession.get().getString("General.calendarFor")+" "+teacher.getName(); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Override
	protected AuthenticatedPageCategory getPageCategory() {
		return AuthenticatedPageCategory.CALENDAR;
	}

}
