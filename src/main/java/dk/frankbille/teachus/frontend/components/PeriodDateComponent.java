package dk.frankbille.teachus.frontend.components;

import java.util.Date;

import org.joda.time.DateMidnight;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;

import wicket.Component;
import wicket.markup.html.WebMarkupContainer;
import wicket.markup.html.basic.Label;
import wicket.markup.html.panel.Panel;
import wicket.markup.repeater.RepeatingView;
import dk.frankbille.teachus.dao.BookingDAO;
import dk.frankbille.teachus.domain.Period;
import dk.frankbille.teachus.domain.Pupil;
import dk.frankbille.teachus.domain.PupilBooking;
import dk.frankbille.teachus.domain.PupilBookings;
import dk.frankbille.teachus.frontend.TeachUsApplication;
import dk.frankbille.teachus.frontend.utils.Formatters;

public abstract class PeriodDateComponent extends Panel {
	
	public PeriodDateComponent(String id, final Pupil pupil, final Period period, Date d) {
		super(id);
		
		// Checks
		if (period.hasDate(d) == false) {
			throw new IllegalArgumentException("Date: "+d+" is not in the period: "+period); //$NON-NLS-1$ //$NON-NLS-2$
		}
		
		BookingDAO bookingDAO = TeachUsApplication.get().getBookingDAO();
		
		DateMidnight date = new DateMidnight(d);		
		
		// Load bookings for the date
		PupilBookings bookings = bookingDAO.getBookingsForDate(period, d);
		
		
		// Header
		{
			add(new Label("weekday", Formatters.getFormatWeekDay().print(date))); //$NON-NLS-1$
			add(new Label("date", Formatters.getFormatPrettyDate().print(date))); //$NON-NLS-1$
		}
		
		// Body
		{
			RepeatingView rows = new RepeatingView("rows"); //$NON-NLS-1$
			add(rows);
			
			DateTime time = new DateTime(period.getStartTime()).withDate(date.getYear(), date.getMonthOfYear(), date.getDayOfMonth());
			DateTime end = new DateTime(period.getEndTime()).withDate(date.getYear(), date.getMonthOfYear(), date.getDayOfMonth());

			DateTimeFormatter timeFormat = Formatters.getFormatTime();
			
			while(time.isBefore(end)) {
				final WebMarkupContainer row = new WebMarkupContainer(rows.newChildId());
				rows.add(row);
				
				row.add(new Label("hour", timeFormat.print(time))); //$NON-NLS-1$
				
				// Look for a booking on this time
				PupilBooking pupilBooking = bookings.getBooking(time.toDate());
				
				row.add(getTimeContent("content", pupilBooking, pupil, period, time)); //$NON-NLS-1$
				
				time = time.plusHours(1);
			}
		}
	}
	
	protected abstract Component getTimeContent(String wicketId, PupilBooking pupilBooking, Pupil pupil, Period period, DateTime time);

}
