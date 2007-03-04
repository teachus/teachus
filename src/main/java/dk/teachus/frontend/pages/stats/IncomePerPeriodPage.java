package dk.teachus.frontend.pages.stats;

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
import dk.teachus.dao.BookingDAO;
import dk.teachus.domain.PupilBooking;
import dk.teachus.frontend.TeachUsApplication;
import dk.teachus.frontend.TeachUsSession;
import dk.teachus.frontend.components.jfreechart.BarChartResource;
import dk.teachus.frontend.components.jfreechart.JFreeChartImage;
import dk.teachus.frontend.utils.Formatters;

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
		if (months.isEmpty() == false) {
			for (int i = 1; i <= 12; i++) {
				DateMidnight month = new DateMidnight().withMonthOfYear(i);
				double value = 0;
				
				if (months.containsKey(i)) {
					value = months.get(i);
				}
							
				String formattedMonth = Formatters.getFormatOnlyMonth().print(month);
				categoryDataset.addValue(value, new Integer(year), formattedMonth);
			}
		}
		
		add(new JFreeChartImage("perMonthChart", new BarChartResource(600, 300, categoryDataset, ""+year, "Kr")));
	}
	
	@Override
	protected String getPageLabel() {
		return TeachUsSession.get().getString("General.incomePerPeriod");
	}

	@Override
	protected AuthenticatedPageCategory getPageCategory() {
		return AuthenticatedPageCategory.STATISTICS;
	}

}
