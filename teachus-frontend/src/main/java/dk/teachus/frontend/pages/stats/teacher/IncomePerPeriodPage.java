/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package dk.teachus.frontend.pages.stats.teacher;

import java.awt.Paint;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joda.time.DateMidnight;
import org.joda.time.DateTime;

import wicket.PageParameters;
import wicket.ajax.AjaxRequestTarget;
import wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import wicket.extensions.markup.html.repeater.data.table.IColumn;
import wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
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
import dk.teachus.frontend.components.ListPanel;
import dk.teachus.frontend.components.RendererPropertyColumn;
import dk.teachus.frontend.components.jfreechart.BarChartResource;
import dk.teachus.frontend.components.jfreechart.JFreeChartImage;
import dk.teachus.frontend.components.jfreechart.PaintedDefaultCategoryDataset;
import dk.teachus.frontend.utils.CurrencyChoiceRenderer;
import dk.teachus.frontend.utils.Formatters;
import dk.teachus.frontend.utils.MonthChoiceRenderer;

public class IncomePerPeriodPage extends AbstractTeacherStatisticsPage {
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
	
	public IncomePerPeriodPage(PageParameters pageParameters) {
		int year = 0;
		if (Strings.isEmpty(pageParameters.getString("0"))) {
			year = new DateMidnight().getYear(); 
		} else {
			year = pageParameters.getInt("0");
		}
		
		add(new Label("perMonth", TeachUsSession.get().getString("IncomePerPeriodPage.perMonth"))); //$NON-NLS-1$ //$NON-NLS-2$

		BookingDAO bookingDAO = TeachUsApplication.get().getBookingDAO();
		
		List<Integer> yearsWithPaidBookings = bookingDAO.getYearsWithBookings(getPerson());
		
		Form form = new Form("form"); //$NON-NLS-1$
		add(form);
		final IModel yearModel = new Model(year);
		final DropDownChoice years = new DropDownChoice("years", yearModel, yearsWithPaidBookings); //$NON-NLS-1$
		years.add(new AjaxFormComponentUpdatingBehavior("onchange") { //$NON-NLS-1$
			private static final long serialVersionUID = 1L;

			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				int year = (Integer) yearModel.getObject(years);
				PageParameters pageParameters = new PageParameters();
				pageParameters.add("0", ""+year);
				getRequestCycle().setResponsePage(IncomePerPeriodPage.class, pageParameters);
			}			
		});
		form.add(years);
		
			
		Date fromDate = new DateMidnight().withYear(year).withMonthOfYear(1).withDayOfMonth(1).toDate();
		Date toDate = new DateMidnight().withYear(year).withMonthOfYear(12).withDayOfMonth(31).toDate();
		
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

		// Paid bookings
		PaintedDefaultCategoryDataset paidCategoryDataset = createCategoryDataset(paidBookings, TeachUsSession.get().getString("General.paidBookings"), COLOR_GREEN); //$NON-NLS-1$
		
		// Unpaid bookings
		PaintedDefaultCategoryDataset unPaidCategoryDataset = createCategoryDataset(unPaidBookings, TeachUsSession.get().getString("General.unPaidBookings"), COLOR_RED); //$NON-NLS-1$
		appendDataset(paidCategoryDataset, unPaidCategoryDataset);
		
		// Future bookings
		PaintedDefaultCategoryDataset futureCategoryDataset = createCategoryDataset(futureBookings, TeachUsSession.get().getString("General.futureLessons"), COLOR_BLUE); //$NON-NLS-1$
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
	protected String getPageLabel() {
		return TeachUsSession.get().getString("General.incomePerPeriod"); //$NON-NLS-1$
	}

}
