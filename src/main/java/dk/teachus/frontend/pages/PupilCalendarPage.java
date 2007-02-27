package dk.teachus.frontend.pages;

import java.util.Date;

import org.joda.time.DateMidnight;
import org.joda.time.format.DateTimeFormatter;

import wicket.ResourceReference;
import wicket.markup.html.link.Link;
import dk.teachus.dao.PeriodDAO;
import dk.teachus.domain.Period;
import dk.teachus.domain.Periods;
import dk.teachus.domain.Pupil;
import dk.teachus.frontend.TeachUsApplication;
import dk.teachus.frontend.TeachUsSession;
import dk.teachus.frontend.UserLevel;
import dk.teachus.frontend.components.CalendarPanel;
import dk.teachus.frontend.components.PeriodDateComponent;
import dk.teachus.frontend.components.PupilPeriodDateComponent;
import dk.teachus.frontend.utils.Formatters;
import dk.teachus.frontend.utils.Resources;


public class PupilCalendarPage extends AuthenticatedBasePage {
	private static final long serialVersionUID = 1L;
	
	private Pupil pupil;
	
	public PupilCalendarPage() {
		this(null);
	}
	
	public PupilCalendarPage(Pupil pupil) {
		this(new Date(), pupil);
	}
	
	public PupilCalendarPage(Date pageDate, Pupil pupil) {
		super(UserLevel.PUPIL);
		
		if (pupil == null) {
			pupil = (Pupil) TeachUsSession.get().getPerson();
		}
		
		this.pupil = pupil;
				
		DateTimeFormatter formatIsoDate = Formatters.getFormatIsoDate();
		initializePupilCalendar(new DateMidnight(pageDate), formatIsoDate, pupil);
	}
	

	private void initializePupilCalendar(DateMidnight pageDate, DateTimeFormatter formatIsoDate, final Pupil pupil) {		
		PeriodDAO periodDAO = TeachUsApplication.get().getPeriodDAO();
		
		final Periods periods = periodDAO.getPeriods(pupil.getTeacher());
		
		add(new CalendarPanel("calendar", pageDate, periods) { //$NON-NLS-1$
			private static final long serialVersionUID = 1L;

			@Override
			protected Link createBackLink(String wicketId, final DateMidnight previousWeekDate) {
				return new Link(wicketId) {
					private static final long serialVersionUID = 1L;
		
					@Override
					public void onClick() {
						getRequestCycle().setResponsePage(new PupilCalendarPage(previousWeekDate.toDate(), pupil));
					}			
				};
			}

			@Override
			protected Link createForwardLink(String wicketId, final DateMidnight nextWeekDate) {
				return new Link(wicketId) {
					private static final long serialVersionUID = 1L;
		
					@Override
					public void onClick() {
						getRequestCycle().setResponsePage(new PupilCalendarPage(nextWeekDate.toDate(), pupil));
					}			
				};
			}

			@Override
			protected PeriodDateComponent createPeriodDateComponent(String wicketId, Period period, Date date) {
				return new PupilPeriodDateComponent("period", pupil, period, new DateMidnight(date)); //$NON-NLS-1$
			}
			
			@Override
			protected void navigationDateSelected(DateMidnight date) {
				getRequestCycle().setResponsePage(new PupilCalendarPage(date.toDate(), pupil));
			}
		});
	}

	@Override
	protected ResourceReference getPageIcon() {
		return Resources.CALENDAR;
	}

	@Override
	protected String getPageLabel() {
		return TeachUsSession.get().getString("General.calendarFor")+" "+pupil.getName(); //$NON-NLS-1$ //$NON-NLS-2$
	}
	
}
