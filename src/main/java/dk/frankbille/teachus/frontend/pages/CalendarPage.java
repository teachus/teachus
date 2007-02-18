package dk.frankbille.teachus.frontend.pages;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.joda.time.DateMidnight;
import org.joda.time.DateTimeConstants;
import org.joda.time.chrono.ISOChronology;
import org.joda.time.format.DateTimeFormatter;

import wicket.PageParameters;
import wicket.ResourceReference;
import wicket.behavior.SimpleAttributeModifier;
import wicket.markup.html.WebMarkupContainer;
import wicket.markup.html.basic.Label;
import wicket.markup.html.image.Image;
import wicket.markup.html.link.BookmarkablePageLink;
import wicket.markup.html.link.Link;
import wicket.markup.repeater.RepeatingView;
import wicket.util.string.Strings;
import dk.frankbille.teachus.dao.PeriodDAO;
import dk.frankbille.teachus.domain.DatePeriod;
import dk.frankbille.teachus.domain.Period;
import dk.frankbille.teachus.domain.Periods;
import dk.frankbille.teachus.domain.Pupil;
import dk.frankbille.teachus.frontend.TeachUsApplication;
import dk.frankbille.teachus.frontend.TeachUsSession;
import dk.frankbille.teachus.frontend.UserLevel;
import dk.frankbille.teachus.frontend.components.PupilPeriodDateComponent;
import dk.frankbille.teachus.frontend.utils.Formatters;
import dk.frankbille.teachus.frontend.utils.Icons;


public class CalendarPage extends AuthenticatedBasePage {
	private static final long serialVersionUID = 1L;
	
	public CalendarPage(PageParameters pageParameters) {
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
			initializePupilCalendar(pageDate, formatIsoDate);
		}
	}

	private void initializePupilCalendar(DateMidnight pageDate, DateTimeFormatter formatIsoDate) {		
		PeriodDAO periodDAO = TeachUsApplication.get().getPeriodDAO();
		
		Pupil pupil = (Pupil) TeachUsSession.get().getPerson();
		Periods periods = periodDAO.getPeriods(pupil.getTeacher());
		
		
		DateMidnight weekDate = pageDate.withDayOfWeek(DateTimeConstants.MONDAY);
		DateMidnight firstWeekDate = weekDate;
		List<DatePeriod> dates = new ArrayList<DatePeriod>();
		List<DatePeriod> weekDates = periods.generateDatesForWeek(weekDate.toDate());
		do {
			dates.addAll(weekDates);
			weekDate = weekDate.plusWeeks(1);
			weekDates = periods.generateDatesForWeek(weekDate.toDate());
		} while(dates.size()+weekDates.size() <= 7);
		
		
		// Weeks
		RepeatingView weeks = new RepeatingView("weeks"); //$NON-NLS-1$
		add(weeks);
		
		int w = -1;
		RepeatingView days = null;
		DateTimeFormatter formatWeekOfYear = Formatters.getFormatWeekOfYear();
		Label weekHeader = null;
		int daysInWeek = 0;
		for (DatePeriod datePeriod : dates) {
			Date date = datePeriod.getDate();
			DateMidnight dm = new DateMidnight(date);
			
			if (dm.getWeekOfWeekyear() != w) {
				if (weekHeader != null) {
					weekHeader.add(new SimpleAttributeModifier("colspan", ""+daysInWeek)); //$NON-NLS-1$ //$NON-NLS-2$
				}
				daysInWeek = 0;
				
				WebMarkupContainer week = new WebMarkupContainer(weeks.newChildId());
				weeks.add(week);
				
				weekHeader = new Label("weekHeader", formatWeekOfYear.print(dm)); //$NON-NLS-1$
				week.add(weekHeader);
				
				days = new RepeatingView("days"); //$NON-NLS-1$
				week.add(days);
				
				w = dm.getWeekOfWeekyear();
			}
			
			WebMarkupContainer day = new WebMarkupContainer(days.newChildId());
			days.add(day);
			
			// Periods
			RepeatingView periodsView = new RepeatingView("periods"); //$NON-NLS-1$
			day.add(periodsView);
			
			for (Period period : datePeriod.getPeriods()) {
				WebMarkupContainer periodCell = new WebMarkupContainer(periodsView.newChildId());
				periodsView.add(periodCell);
				
				periodCell.add(new PupilPeriodDateComponent("period", pupil, period, date)); //$NON-NLS-1$
			}
			
			daysInWeek++;
		}
		
		if (weekHeader != null) {
			weekHeader.add(new SimpleAttributeModifier("colspan", ""+daysInWeek)); //$NON-NLS-1$ //$NON-NLS-2$
		}
		
				
		// Navigation
		PageParameters ppBack = new PageParameters();
		ppBack.add("0", formatIsoDate.print(firstWeekDate.minusWeeks(periods.calculateNumberOfWeeks(firstWeekDate.minusDays(1).toDate(), 7)))); //$NON-NLS-1$
		Link backLink = new BookmarkablePageLink("backLink", CalendarPage.class, ppBack); //$NON-NLS-1$
		backLink.add(new Image("backIcon", new ResourceReference(CalendarPage.class, "resources/left.png"))); //$NON-NLS-1$ //$NON-NLS-2$
		add(backLink);
		
		PageParameters ppForward = new PageParameters();
		ppForward.add("0", formatIsoDate.print(weekDate)); //$NON-NLS-1$
		Link forwardLink = new BookmarkablePageLink("forwardLink", CalendarPage.class, ppForward); //$NON-NLS-1$
		forwardLink.add(new Image("forwardIcon", new ResourceReference(CalendarPage.class, "resources/right.png"))); //$NON-NLS-1$ //$NON-NLS-2$
		add(forwardLink);
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
