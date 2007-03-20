package dk.teachus.frontend.pages.stats;

import java.awt.Color;
import java.awt.Paint;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joda.time.DateMidnight;
import org.joda.time.DateTime;

import wicket.ajax.AjaxRequestTarget;
import wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import wicket.extensions.markup.html.repeater.data.table.IColumn;
import wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import wicket.markup.html.basic.Label;
import wicket.markup.html.form.DropDownChoice;
import wicket.markup.html.form.Form;
import wicket.model.IModel;
import wicket.model.Model;
import dk.teachus.dao.BookingDAO;
import dk.teachus.domain.PupilBooking;
import dk.teachus.frontend.TeachUsApplication;
import dk.teachus.frontend.TeachUsSession;
import dk.teachus.frontend.components.ListPanel;
import dk.teachus.frontend.components.RendererPropertyColumn;
import dk.teachus.frontend.components.jfreechart.BarChartResource;
import dk.teachus.frontend.components.jfreechart.JFreeChartImage;
import dk.teachus.frontend.components.jfreechart.PaintedDefaultCategoryDataset;
import dk.teachus.frontend.utils.CurrencyChoiceRenderer;
import dk.teachus.frontend.utils.Formatters;
import dk.teachus.frontend.utils.MonthChoiceRenderer;

public class IncomePerPeriodPage extends AbstractStatisticsPage {
	private static class MonthIncome implements Serializable {
		private static final long serialVersionUID = 1L;

		private int month;

		private double paid;

		private double unpaid;

		private double future;
		
		private int paidLessonCount;
		
		private int unpaidLessonCount;
		
		private int futureLessonCount;
		
		public void addFuture(double future) {
			this.future += future;
		}
		
		public void addFutureLesson() {
			futureLessonCount++;
		}
		
		public void addPaid(double paid) {
			this.paid += paid;
		}
		
		public void addPaidLesson() {
			paidLessonCount++;
		}

		public void addUnpaid(double unpaid) {
			this.unpaid += unpaid;
		}
		
		public void addUnpaidLesson() {
			unpaidLessonCount++;
		}
		
		public double getFuture() {
			return future;
		}

		public int getFutureLessonCount() {
			return futureLessonCount;
		}
		public int getLessonCount() {
			return paidLessonCount;
		}

		public int getMonth() {
			return month;
		}

		public double getPaid() {
			return paid;
		}

		public double getTotal() {
			return paid + unpaid + future;
		}

		public double getTotalLessonCount() {
			return paidLessonCount + unpaidLessonCount + futureLessonCount;
		}

		public double getUnpaid() {
			return unpaid;
		}

		public int getUnpaidLessonCount() {
			return unpaidLessonCount;
		}

		public void setMonth(int month) {
			this.month = month;
		}
	}

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
		
		
		
		// Add list of data in static form
		IColumn[] columns = new IColumn[] {
				new RendererPropertyColumn(new Model(TeachUsSession.get().getString("General.month")), "month", new MonthChoiceRenderer()), //$NON-NLS-1$ //$NON-NLS-2$
				new RendererPropertyColumn(new Model(TeachUsSession.get().getString("General.paid")), "paid", new CurrencyChoiceRenderer()), //$NON-NLS-1$ //$NON-NLS-2$
				new PropertyColumn(new Model(TeachUsSession.get().getString("IncomePerPeriodPage.paidBookings")), "paidLessonCount"), //$NON-NLS-1$ //$NON-NLS-2$
				new RendererPropertyColumn(new Model(TeachUsSession.get().getString("General.unpaid")), "unpaid", new CurrencyChoiceRenderer()), //$NON-NLS-1$ //$NON-NLS-2$
				new PropertyColumn(new Model(TeachUsSession.get().getString("IncomePerPeriodPage.unPaidBookings")), "unpaidLessonCount"), //$NON-NLS-1$ //$NON-NLS-2$
				new RendererPropertyColumn(new Model(TeachUsSession.get().getString("IncomePerPeriodPage.future")), "future", new CurrencyChoiceRenderer()), //$NON-NLS-1$ //$NON-NLS-2$
				new PropertyColumn(new Model(TeachUsSession.get().getString("IncomePerPeriodPage.futureLessons")), "futureLessonCount"), //$NON-NLS-1$ //$NON-NLS-2$
				new RendererPropertyColumn(new Model(TeachUsSession.get().getString("General.total")), "total", new CurrencyChoiceRenderer()), //$NON-NLS-1$ //$NON-NLS-2$
				new PropertyColumn(new Model(TeachUsSession.get().getString("IncomePerPeriodPage.totalLessons")), "totalLessonCount"), //$NON-NLS-1$ //$NON-NLS-2$
		};
		
		List<MonthIncome> data = new ArrayList<MonthIncome>();
		
		for (PupilBooking booking : paidBookings) {
			MonthIncome monthIncome = getMonthIncome(data, booking);
			
			monthIncome.addPaid(booking.getPeriod().getPrice());
			monthIncome.addPaidLesson();
		}
		
		for (PupilBooking booking : unPaidBookings) {
			MonthIncome monthIncome = getMonthIncome(data, booking);
			
			monthIncome.addUnpaid(booking.getPeriod().getPrice());
			monthIncome.addUnpaidLesson();
		}
		
		for (PupilBooking booking : futureBookings) {
			MonthIncome monthIncome = getMonthIncome(data, booking);
			
			monthIncome.addFuture(booking.getPeriod().getPrice());
			monthIncome.addFutureLesson();
		}
		
		add(new ListPanel("list", columns, data)); //$NON-NLS-1$
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

	private MonthIncome getMonthIncome(List<MonthIncome> data, PupilBooking booking) {
		MonthIncome monthIncome = null;
		for (MonthIncome income : data) {
			if (income.getMonth() == new DateMidnight(booking.getDate()).getMonthOfYear()) {
				monthIncome = income;
				break;
			}
		}
		
		if (monthIncome == null) {
			monthIncome = new MonthIncome();
			monthIncome.setMonth(new DateMidnight(booking.getDate()).getMonthOfYear());
			data.add(monthIncome);
		}
		return monthIncome;
	}
	
	@Override
	protected AuthenticatedPageCategory getPageCategory() {
		return AuthenticatedPageCategory.STATISTICS;
	}

	@Override
	protected String getPageLabel() {
		return TeachUsSession.get().getString("General.incomePerPeriod"); //$NON-NLS-1$
	}

}
