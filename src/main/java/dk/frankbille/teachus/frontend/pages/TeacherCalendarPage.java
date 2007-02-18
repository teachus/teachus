package dk.frankbille.teachus.frontend.pages;

import java.util.Date;

import org.joda.time.DateMidnight;

import dk.frankbille.teachus.dao.PeriodDAO;
import dk.frankbille.teachus.domain.Period;
import dk.frankbille.teachus.domain.Periods;
import dk.frankbille.teachus.domain.Teacher;
import dk.frankbille.teachus.frontend.TeachUsApplication;
import dk.frankbille.teachus.frontend.TeachUsSession;
import dk.frankbille.teachus.frontend.UserLevel;
import dk.frankbille.teachus.frontend.components.CalendarPanel;
import dk.frankbille.teachus.frontend.components.PeriodDateComponent;
import dk.frankbille.teachus.frontend.components.TeacherPeriodDateComponent;
import dk.frankbille.teachus.frontend.utils.Icons;
import wicket.ResourceReference;
import wicket.RestartResponseAtInterceptPageException;
import wicket.markup.html.link.Link;
import wicket.protocol.http.WebApplication;

public class TeacherCalendarPage extends AuthenticatedBasePage {
	private static final long serialVersionUID = 1L;

	public TeacherCalendarPage() {
		this(new DateMidnight());
	}
	
	public TeacherCalendarPage(DateMidnight pageDate) {
		super(UserLevel.TEACHER);
		
		if (TeachUsSession.get().getUserLevel() != UserLevel.TEACHER) {
			throw new RestartResponseAtInterceptPageException(WebApplication.get().getHomePage());
		}
		
		PeriodDAO periodDAO = TeachUsApplication.get().getPeriodDAO();
		
		final Teacher teacher = (Teacher) TeachUsSession.get().getPerson();		
		
		final Periods periods = periodDAO.getPeriods(teacher);
		
		add(new CalendarPanel("calendar", pageDate, periods) {
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
			
		});
	}

	@Override
	protected ResourceReference getPageIcon() {
		return Icons.CALENDAR;
	}

	@Override
	protected String getPageLabel() {
		return TeachUsSession.get().getString("General.calendar");
	}

}
