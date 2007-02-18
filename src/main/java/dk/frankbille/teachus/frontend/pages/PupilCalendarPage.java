package dk.frankbille.teachus.frontend.pages;

import java.util.Date;

import org.joda.time.DateMidnight;
import org.joda.time.chrono.ISOChronology;
import org.joda.time.format.DateTimeFormatter;

import wicket.PageParameters;
import wicket.ResourceReference;
import wicket.markup.html.link.Link;
import wicket.util.string.Strings;
import dk.frankbille.teachus.dao.PeriodDAO;
import dk.frankbille.teachus.domain.Period;
import dk.frankbille.teachus.domain.Periods;
import dk.frankbille.teachus.domain.Pupil;
import dk.frankbille.teachus.frontend.TeachUsApplication;
import dk.frankbille.teachus.frontend.TeachUsSession;
import dk.frankbille.teachus.frontend.UserLevel;
import dk.frankbille.teachus.frontend.components.CalendarPanel;
import dk.frankbille.teachus.frontend.components.PeriodDateComponent;
import dk.frankbille.teachus.frontend.components.PupilPeriodDateComponent;
import dk.frankbille.teachus.frontend.utils.Formatters;
import dk.frankbille.teachus.frontend.utils.Icons;


public class PupilCalendarPage extends AuthenticatedBasePage {
	private static final long serialVersionUID = 1L;
	
	public PupilCalendarPage(PageParameters pageParameters) {
		super(UserLevel.PUPIL);
		
		DateMidnight pageDate = null;
		DateTimeFormatter formatIsoDate = Formatters.getFormatIsoDate();
		
		String dateString = pageParameters.getString("0"); //$NON-NLS-1$
		if (Strings.isEmpty(dateString) == false) {
			pageDate = formatIsoDate.parseDateTime(dateString).toDateMidnight();
		} else {
			pageDate = new DateMidnight(ISOChronology.getInstance());
		}
		
		if (TeachUsSession.get().getUserLevel() == UserLevel.PUPIL) {
			Pupil pupil = (Pupil) TeachUsSession.get().getPerson();
			initializePupilCalendar(pageDate, formatIsoDate, pupil);
		}
	}
	
	public PupilCalendarPage(Pupil pupil) {
		this(new Date(), pupil);
	}
	
	public PupilCalendarPage(Date pageDate, Pupil pupil) {
		super(UserLevel.PUPIL);
		
		DateTimeFormatter formatIsoDate = Formatters.getFormatIsoDate();
		initializePupilCalendar(new DateMidnight(pageDate), formatIsoDate, pupil);
	}
	

	private void initializePupilCalendar(DateMidnight pageDate, DateTimeFormatter formatIsoDate, final Pupil pupil) {		
		PeriodDAO periodDAO = TeachUsApplication.get().getPeriodDAO();
		
		final Periods periods = periodDAO.getPeriods(pupil.getTeacher());
		
		add(new CalendarPanel("calendar", pageDate, periods) {
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
				return new PupilPeriodDateComponent("period", pupil, period, new DateMidnight(date));
			}			
		});
	}

	@Override
	protected ResourceReference getPageIcon() {
		return Icons.CALENDAR;
	}

	@Override
	protected String getPageLabel() {
		return TeachUsSession.get().getString("General.calendar"); //$NON-NLS-1$
	}
	
}
