package dk.teachus.frontend.pages;

import java.util.Date;

import org.joda.time.DateMidnight;

import dk.teachus.dao.PeriodDAO;
import dk.teachus.domain.Period;
import dk.teachus.domain.Periods;
import dk.teachus.domain.Teacher;
import dk.teachus.frontend.TeachUsApplication;
import dk.teachus.frontend.TeachUsSession;
import dk.teachus.frontend.UserLevel;
import dk.teachus.frontend.components.CalendarPanel;
import dk.teachus.frontend.components.PeriodDateComponent;
import dk.teachus.frontend.components.TeacherPeriodDateComponent;
import dk.teachus.frontend.utils.Resources;
import wicket.ResourceReference;
import wicket.RestartResponseAtInterceptPageException;
import wicket.markup.html.link.Link;
import wicket.protocol.http.WebApplication;

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

			@Override
			protected Link createBackLink(String wicketId, final DateMidnight previousWeekDate) {
				return new Link(wicketId) {
					private static final long serialVersionUID = 1L;
		
					@Override
					public void onClick() {
						getRequestCycle().setResponsePage(new TeacherCalendarPage(previousWeekDate));
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
			protected PeriodDateComponent createPeriodDateComponent(String wicketId, Period period, Date date) {
				return new TeacherPeriodDateComponent(wicketId, teacher, period, new DateMidnight(date));
			}
			
			@Override
			protected void navigationDateSelected(DateMidnight date) {
				getRequestCycle().setResponsePage(new TeacherCalendarPage(date));
			}
			
		});
	}

	@Override
	protected ResourceReference getPageIcon() {
		return Resources.CALENDAR;
	}

	@Override
	protected String getPageLabel() {
		return TeachUsSession.get().getString("General.calendarFor")+" "+teacher.getName(); //$NON-NLS-1$ //$NON-NLS-2$
	}

}