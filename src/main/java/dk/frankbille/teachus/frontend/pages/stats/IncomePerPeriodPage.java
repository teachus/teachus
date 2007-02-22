package dk.frankbille.teachus.frontend.pages.stats;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jfree.data.category.DefaultCategoryDataset;
import org.joda.time.DateMidnight;

import wicket.ajax.AjaxRequestTarget;
import wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import wicket.markup.html.basic.Label;
import wicket.markup.html.form.DropDownChoice;
import wicket.markup.html.form.Form;
import wicket.model.IModel;
import wicket.model.Model;
import dk.frankbille.teachus.dao.BookingDAO;
import dk.frankbille.teachus.domain.PupilBooking;
import dk.frankbille.teachus.frontend.TeachUsApplication;
import dk.frankbille.teachus.frontend.TeachUsSession;
import dk.frankbille.teachus.frontend.components.jfreechart.BarChartResource;
import dk.frankbille.teachus.frontend.components.jfreechart.JFreeChartImage;
import dk.frankbille.teachus.frontend.utils.Formatters;

public class IncomePerPeriodPage extends AbstractStatisticsPage {
	private static final long serialVersionUID = 1L;

	public IncomePerPeriodPage() {
		this(new DateMidnight().getYear());
	}
	
	public IncomePerPeriodPage(int year) {
		add(new Label("perMonth", TeachUsSession.get().getString("IncomePerPeriodPage.perMonth")));

		BookingDAO bookingDAO = TeachUsApplication.get().getBookingDAO();
		
		List<Integer> yearsWithPaidBookings = bookingDAO.getYearsWithPaidBookings(getTeacher());
		
		Form form = new Form("form");
		add(form);
		final IModel yearModel = new Model(year);
		final DropDownChoice years = new DropDownChoice("years", yearModel, yearsWithPaidBookings);
		years.add(new AjaxFormComponentUpdatingBehavior("onchange") {
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
		
		Map<Integer, Double> months = new HashMap<Integer, Double>();
		for (PupilBooking booking : paidBookings) {
			int month = new DateMidnight(booking.getDate()).getMonthOfYear();
			double price = booking.getPeriod().getPrice();
			if (months.containsKey(month)) {
				months.put(month, months.get(month) + price);
			} else {
				months.put(month, price);
			}
		}
		
		
		DefaultCategoryDataset categoryDataset = new DefaultCategoryDataset();
		for (int i = 1; i <= 12; i++) {
			DateMidnight month = new DateMidnight().withMonthOfYear(i);
			double value = 0;
			
			if (months.containsKey(i)) {
				value = months.get(i);
			}
						
			String formattedMonth = Formatters.getFormatOnlyMonth().print(month);
			categoryDataset.addValue(value, new Integer(year), formattedMonth);
		}
//		categoryDataset.addValue(4700, "2007", "Januar");
//		categoryDataset.addValue(5550, "2007", "Februar");
//		categoryDataset.addValue(0, "2007", "Marts");
//		categoryDataset.addValue(0, "2007", "April");
//		categoryDataset.addValue(0, "2007", "Maj");
//		categoryDataset.addValue(0, "2007", "Juni");
//		categoryDataset.addValue(0, "2007", "Juli");
//		categoryDataset.addValue(0, "2007", "August");
//		categoryDataset.addValue(0, "2007", "September");
//		categoryDataset.addValue(0, "2007", "Oktober");
//		categoryDataset.addValue(0, "2007", "November");
//		categoryDataset.addValue(0, "2007", "December");
		
		add(new JFreeChartImage("perMonthChart", new BarChartResource(600, 300, categoryDataset, ""+year, "Kr")));
	}
	
	@Override
	protected String getPageLabel() {
		return TeachUsSession.get().getString("General.incomePerPeriod");
	}

}
