package dk.teachus.frontend.pages.stats.teacher;

import java.awt.Paint;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import org.joda.time.DateMidnight;
import org.joda.time.DateTime;

import wicket.PageParameters;
import wicket.RestartResponseAtInterceptPageException;
import wicket.ajax.AjaxRequestTarget;
import wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import wicket.markup.html.basic.Label;
import wicket.markup.html.form.DropDownChoice;
import wicket.markup.html.form.Form;
import wicket.model.IModel;
import wicket.model.Model;
import wicket.util.string.Strings;
import dk.teachus.backend.dao.BookingDAO;
import dk.teachus.backend.domain.PupilBooking;
import dk.teachus.frontend.TeachUsApplication;
import dk.teachus.frontend.TeachUsSession;
import dk.teachus.frontend.components.jfreechart.BarChartResource;
import dk.teachus.frontend.components.jfreechart.JFreeChartImage;
import dk.teachus.frontend.components.jfreechart.PaintedDefaultCategoryDataset;

public class LessonsPerHourPage extends AbstractTeacherStatisticsPage {
	private static final long serialVersionUID = 1L;
	
	public LessonsPerHourPage(PageParameters pageParameters) {
		int year = 0;
		if (Strings.isEmpty(pageParameters.getString("0"))) { //$NON-NLS-1$
			year = new DateMidnight().getYear(); 
		} else {
			year = pageParameters.getInt("0"); //$NON-NLS-1$
		}
		
		BookingDAO bookingDAO = TeachUsApplication.get().getBookingDAO();		
		
		final List<Integer> yearsWithPaidBookings = bookingDAO.getYearsWithBookings(getPerson());
		
		if (yearsWithPaidBookings.contains(year) == false) {
			throw new RestartResponseAtInterceptPageException(LessonsPerHourPage.class);
		}
		
		createFilterForm(year, yearsWithPaidBookings);
		
		Date fromDate = new DateMidnight().withYear(year).withMonthOfYear(1).withDayOfMonth(1).toDate();
		Date toDate = new DateMidnight().withYear(year).withMonthOfYear(12).withDayOfMonth(31).toDate();
		
		// Paid
		List<PupilBooking> paidBookings = bookingDAO.getPaidBookings(getPerson(), fromDate, toDate);
		
		// Get the unpaid bookings
		List<PupilBooking> allUnPaidBookings = bookingDAO.getUnPaidBookings(getPerson(), fromDate, toDate);
		
		// Extract the unpaid bookings which is before now
		List<PupilBooking> unPaidBookings = new ArrayList<PupilBooking>();
		List<PupilBooking> futureBookings = new ArrayList<PupilBooking>();
		DateTime now = new DateTime();
		for (PupilBooking booking : allUnPaidBookings) {
			if (now.isAfter(new DateTime(booking.getDate()))) {
				unPaidBookings.add(booking);
			} else {
				futureBookings.add(booking);
			}
		}
		
		PaintedDefaultCategoryDataset dataset = createDataset(paidBookings, TeachUsSession.get().getString("General.paidBookings"), COLOR_GREEN); //$NON-NLS-1$
		
		PaintedDefaultCategoryDataset unpaidDataset = createDataset(unPaidBookings, TeachUsSession.get().getString("General.unPaidBookings"), COLOR_RED); //$NON-NLS-1$
		appendDataset(dataset, unpaidDataset);
		
		PaintedDefaultCategoryDataset futureDataset = createDataset(futureBookings, TeachUsSession.get().getString("General.futureLessons"), COLOR_BLUE); //$NON-NLS-1$
		appendDataset(dataset, futureDataset);
		
		add(new JFreeChartImage("lessonsPerHourChart", new BarChartResource(600, 400, dataset, TeachUsSession.get().getString("LessonsPerHourPage.hourOfDay"), TeachUsSession.get().getString("LessonsPerHourPage.numberOfLessons")) { //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			private static final long serialVersionUID = 1L;

			@Override
			protected NumberFormat getYAxisNumberFormat() {
				return NumberFormat.getIntegerInstance();
			}
			
			@Override
			protected boolean getCreateLegend() {
				return true;
			}
		}));
	}

	private void createFilterForm(int year, final List<Integer> yearsWithPaidBookings) {
		Form form = new Form("form"); //$NON-NLS-1$
		add(form);
		
		form.add(new Label("yearsLabel", TeachUsSession.get().getString("General.year"))); //$NON-NLS-1$ //$NON-NLS-2$
		
		final IModel yearModel = new Model(year);
		final DropDownChoice years = new DropDownChoice("years", yearModel, yearsWithPaidBookings) { //$NON-NLS-1$
			private static final long serialVersionUID = 1L;
			
			@Override
			public boolean isVisible() {
				return yearsWithPaidBookings.isEmpty() == false;
			}
		}; 
		years.add(new AjaxFormComponentUpdatingBehavior("onchange") { //$NON-NLS-1$
			private static final long serialVersionUID = 1L;

			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				int year = (Integer) yearModel.getObject(years);
				PageParameters pageParameters = new PageParameters();
				pageParameters.add("0", ""+year); //$NON-NLS-1$ //$NON-NLS-2$
				getRequestCycle().setResponsePage(LessonsPerHourPage.class, pageParameters);
			}			
		});
		form.add(years);
	}

	private PaintedDefaultCategoryDataset createDataset(List<PupilBooking> bookings, String categoryTitle, Paint paint) {
		SortedMap<Integer, Integer> lessonsPerHour = new TreeMap<Integer, Integer>();
		
		for (PupilBooking booking : bookings) {
			int hour = new DateTime(booking.getDate()).getHourOfDay();
			if (lessonsPerHour.get(hour) != null) {
				lessonsPerHour.put(hour, lessonsPerHour.get(hour)+1);
			} else {
				lessonsPerHour.put(hour, 1);
			}
		}

		PaintedDefaultCategoryDataset dataset = new PaintedDefaultCategoryDataset();
		for (Integer hour : lessonsPerHour.keySet()) {
			Integer count = lessonsPerHour.get(hour);
			
			dataset.addValue(count, categoryTitle, new Integer(hour), paint);
		}
		return dataset;
	}

	@Override
	protected String getPageLabel() {
		return TeachUsSession.get().getString("General.lessonsPerHour"); //$NON-NLS-1$
	}

}
