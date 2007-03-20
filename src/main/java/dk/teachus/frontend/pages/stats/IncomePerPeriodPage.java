package dk.teachus.frontend.pages.stats;

import java.awt.Color;
import java.awt.Paint;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joda.time.DateMidnight;
import org.joda.time.DateTime;

import wicket.ajax.AjaxRequestTarget;
import wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import wicket.markup.html.basic.Label;
import wicket.markup.html.form.DropDownChoice;
import wicket.markup.html.form.Form;
import wicket.model.IModel;
import wicket.model.Model;
import dk.teachus.dao.BookingDAO;
import dk.teachus.domain.PupilBooking;
import dk.teachus.frontend.TeachUsApplication;
import dk.teachus.frontend.TeachUsSession;
import dk.teachus.frontend.components.jfreechart.BarChartResource;
import dk.teachus.frontend.components.jfreechart.JFreeChartImage;
import dk.teachus.frontend.components.jfreechart.PaintedDefaultCategoryDataset;
import dk.teachus.frontend.utils.Formatters;

public class IncomePerPeriodPage extends AbstractStatisticsPage {
	private static final long serialVersionUID = 1L;

	public IncomePerPeriodPage() {
		this(new DateMidnight().getYear());
	}
	
	public IncomePerPeriodPage(int year) {
		add(new Label("perMonth", TeachUsSession.get().getString("IncomePerPeriodPage.perMonth"))); //$NON-NLS-1$ //$NON-NLS-2$

		BookingDAO bookingDAO = TeachUsApplication.get().getBookingDAO();
		
		List<Integer> yearsWithPaidBookings = bookingDAO.getYearsWithBookings(getTeacher());
		
		Form form = new Form("form"); //$NON-NLS-1$
		add(form);
		final IModel yearModel = new Model(year);
		final DropDownChoice years = new DropDownChoice("years", yearModel, yearsWithPaidBookings); //$NON-NLS-1$
		years.add(new AjaxFormComponentUpdatingBehavior("onchange") { //$NON-NLS-1$
			private static final long serialVersionUID = 1L;

			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				int year = (Integer) yearModel.getObject(years);
				getRequestCycle().setResponsePage(new IncomePerPeriodPage(year));
			}			
		});
		form.add(years);
		
			
		Date fromDate = new DateMidnight().withYear(year).withMonthOfYear(1).withDayOfMonth(1).toDate();
		Date toDate = new DateMidnight().withYear(year).withMonthOfYear(12).withDayOfMonth(31).toDate();
		
		List<PupilBooking> paidBookings = bookingDAO.getPaidBookings(getTeacher(), fromDate, toDate);
		
		// Get the unpaid bookings
		List<PupilBooking> allUnPaidBookings = bookingDAO.getUnPaidBookings(getTeacher(), fromDate, toDate);
		
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

		// Paid bookings
		PaintedDefaultCategoryDataset paidCategoryDataset = createCategoryDataset(paidBookings, TeachUsSession.get().getString("IncomePerPeriodPage.paidBookings"), new Color(25, 204, 25)); //$NON-NLS-1$
		
		// Unpaid bookings
		PaintedDefaultCategoryDataset unPaidCategoryDataset = createCategoryDataset(unPaidBookings, TeachUsSession.get().getString("IncomePerPeriodPage.unPaidBookings"), Color.RED); //$NON-NLS-1$
		appendDataset(paidCategoryDataset, unPaidCategoryDataset);
		
		// Future bookings
		PaintedDefaultCategoryDataset futureCategoryDataset = createCategoryDataset(futureBookings, TeachUsSession.get().getString("IncomePerPeriodPage.futureLessons"), new Color(25, 25, 204)); //$NON-NLS-1$
		appendDataset(paidCategoryDataset, futureCategoryDataset);
		
		
		
		
		add(new JFreeChartImage("perMonthChart", new BarChartResource(600, 300, paidCategoryDataset, ""+year, null) { //$NON-NLS-1$ //$NON-NLS-2$
			private static final long serialVersionUID = 1L;

			@Override
			protected boolean getCreateLegend() {
				return true;
			}
		}));
	}

	private void appendDataset(PaintedDefaultCategoryDataset toDataset, PaintedDefaultCategoryDataset fromDataset) {
		for (int i = 0; i < fromDataset.getColumnCount(); i++) {
			Comparable columnKey = fromDataset.getColumnKey(i);
			Comparable rowKey = fromDataset.getRowKey(0);
			Paint paint = fromDataset.getPaint(rowKey);
			toDataset.addValue(fromDataset.getValue(rowKey, columnKey), rowKey, columnKey, paint);
		}
	}

	private PaintedDefaultCategoryDataset createCategoryDataset(List<PupilBooking> bookings, Comparable dataSetLabel, Paint paint) {
		Map<Integer, Double> months = new HashMap<Integer, Double>();
		for (PupilBooking booking : bookings) {
			int month = new DateMidnight(booking.getDate()).getMonthOfYear();
			double price = booking.getPeriod().getPrice();
			if (months.containsKey(month)) {
				months.put(month, months.get(month) + price);
			} else {
				months.put(month, price);
			}
		}
		
		
		PaintedDefaultCategoryDataset categoryDataset = new PaintedDefaultCategoryDataset();
		if (months.isEmpty() == false) {
			for (int i = 1; i <= 12; i++) {
				DateMidnight month = new DateMidnight().withMonthOfYear(i);
				double value = 0;
				
				if (months.containsKey(i)) {
					value = months.get(i);
				}
							
				String formattedMonth = Formatters.getFormatOnlyMonth().print(month);
				categoryDataset.addValue(value, dataSetLabel, formattedMonth, paint);
			}
		}
		return categoryDataset;
	}
	
	@Override
	protected String getPageLabel() {
		return TeachUsSession.get().getString("General.incomePerPeriod"); //$NON-NLS-1$
	}

	@Override
	protected AuthenticatedPageCategory getPageCategory() {
		return AuthenticatedPageCategory.STATISTICS;
	}

}
