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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.WebComponent;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.joda.time.DateMidnight;
import org.joda.time.DateTime;

import dk.teachus.backend.dao.BookingDAO;
import dk.teachus.backend.domain.PupilBooking;
import dk.teachus.frontend.TeachUsApplication;
import dk.teachus.frontend.TeachUsSession;
import dk.teachus.frontend.components.list.ListPanel;
import dk.teachus.frontend.components.list.RendererPropertyColumn;
import dk.teachus.frontend.utils.CurrencyChoiceRenderer;
import dk.teachus.frontend.utils.MonthChoiceRenderer;

public class IncomePerMonthPage extends AbstractTeacherStatisticsPage {
	private static final long serialVersionUID = 1L;
	
	static class MonthIncome implements Serializable {
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
	
	public IncomePerMonthPage(PageParameters pageParameters) {
		int year = pageParameters.get(0).toInt(new DateMidnight().getYear());
		
		add(new Label("perMonth", TeachUsSession.get().getString("IncomePerMonthPage.perMonth"))); //$NON-NLS-1$ //$NON-NLS-2$

		BookingDAO bookingDAO = TeachUsApplication.get().getBookingDAO();
		
		List<Integer> yearsWithPaidBookings = bookingDAO.getYearsWithBookings(getPerson());
		
		// If the latest booking is not in the selected year, then select the latest year with bookings
		if (yearsWithPaidBookings.size() > 0 && yearsWithPaidBookings.contains(year) == false) {
			year = 0;
			for (Integer y : yearsWithPaidBookings) {
				if (y > year) {
					year = y;
				}
			}
		}
		
		Form<Void> form = new Form<Void>("form"); //$NON-NLS-1$
		add(form);
		final IModel<Integer> yearModel = new Model<Integer>(year);
		final DropDownChoice<Integer> years = new DropDownChoice<Integer>("years", yearModel, yearsWithPaidBookings); //$NON-NLS-1$
		years.add(new AjaxFormComponentUpdatingBehavior("onchange") { //$NON-NLS-1$
			private static final long serialVersionUID = 1L;

			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				int year = (Integer) yearModel.getObject();
				PageParameters pageParameters = new PageParameters();
				pageParameters.set(0, year);
				getRequestCycle().setResponsePage(IncomePerMonthPage.class, pageParameters);
			}			
		});
		form.add(years);
		
			
		DateMidnight fromDate = new DateMidnight(year, 1, 1);
		DateMidnight toDate = new DateMidnight(year, 12, 31);
		
		List<PupilBooking> paidBookings = bookingDAO.getPaidBookings(getPerson(), fromDate, toDate);
		
		// Get the unpaid bookings
		List<PupilBooking> allUnPaidBookings = bookingDAO.getUnPaidBookings(getPerson(), fromDate, toDate);
		
		// Extract the unpaid bookings which is before now
		List<PupilBooking> unPaidBookings = new ArrayList<PupilBooking>();
		List<PupilBooking> futureBookings = new ArrayList<PupilBooking>();
		DateTime now = new DateTime();
		for (PupilBooking booking : allUnPaidBookings) {
			if (now.isAfter(booking.getDate())) {
				unPaidBookings.add(booking);
			} else {
				futureBookings.add(booking);
			}
		}
		
		final List<MonthIncome> data = new ArrayList<MonthIncome>();
		
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
		
		WebComponent chart = new WebComponent("chart") {
			private static final long serialVersionUID = 1L;
			
			@Override
			public void renderHead(IHeaderResponse response) {
				response.renderJavaScriptReference("https://www.google.com/jsapi");
				StringBuilder b = new StringBuilder();
				b.append("google.load('visualization', '1', {packages:['corechart']});");
				b.append("google.setOnLoadCallback(drawChart);");
				b.append("function drawChart() {");
				b.append("var data = google.visualization.arrayToDataTable([");
				b.append("['").append(TeachUsSession.get().getString("General.month")).append("', ");
				b.append("'").append(TeachUsSession.get().getString("General.paid")).append("', ");
				b.append("'").append(TeachUsSession.get().getString("General.unpaid")).append("', ");
				b.append("'").append(TeachUsSession.get().getString("IncomePerMonthPage.future")).append("'],");
				StringBuilder d = new StringBuilder();
				for (MonthIncome monthIncome : data) {
					if (d.length() > 0) {
						d.append(",");
					}
					d.append("[");
					d.append("'").append(new MonthChoiceRenderer().getDisplayValue(monthIncome.getMonth())).append("', ");
					d.append(monthIncome.getPaid()).append(", ");
					d.append(monthIncome.getUnpaid()).append(", ");
					d.append(monthIncome.getFuture());
					d.append("]");
				}
				b.append(d);
				b.append("]);");

				b.append("var options = {");
				b.append("isStacked: true,");
				b.append("series: {0:{color:'#46a546'},1:{color:'#9d261d'},2:{color:'#049cdb'}},");
				b.append("vAxis: {format:'#,##0.00'},");
				b.append("};");

				b.append("var chart = new google.visualization.ColumnChart(document.getElementById('").append(getMarkupId()).append("'));");
				b.append("chart.draw(data, options);");
				b.append("}");
				response.renderJavaScript(b, "chart");
			}
		};
		chart.setOutputMarkupId(true);
		add(chart);
		
		// Add list of data in static form
		List<IColumn<MonthIncome>> columns = new ArrayList<IColumn<MonthIncome>>();
		columns.add(new RendererPropertyColumn<MonthIncome,Integer>(new Model<String>(TeachUsSession.get().getString("General.month")), "month", new MonthChoiceRenderer())); //$NON-NLS-1$ //$NON-NLS-2$
		columns.add(new RendererPropertyColumn<MonthIncome,Object>(new Model<String>(TeachUsSession.get().getString("General.paid")), "paid", new CurrencyChoiceRenderer())); //$NON-NLS-1$ //$NON-NLS-2$
		columns.add(new PropertyColumn<MonthIncome>(new Model<String>(TeachUsSession.get().getString("IncomePerMonthPage.paidBookings")), "paidLessonCount")); //$NON-NLS-1$ //$NON-NLS-2$
		columns.add(new RendererPropertyColumn<MonthIncome,Object>(new Model<String>(TeachUsSession.get().getString("General.unpaid")), "unpaid", new CurrencyChoiceRenderer())); //$NON-NLS-1$ //$NON-NLS-2$
		columns.add(new PropertyColumn<MonthIncome>(new Model<String>(TeachUsSession.get().getString("IncomePerMonthPage.unPaidBookings")), "unpaidLessonCount")); //$NON-NLS-1$ //$NON-NLS-2$
		columns.add(new RendererPropertyColumn<MonthIncome,Object>(new Model<String>(TeachUsSession.get().getString("IncomePerMonthPage.future")), "future", new CurrencyChoiceRenderer())); //$NON-NLS-1$ //$NON-NLS-2$
		columns.add(new PropertyColumn<MonthIncome>(new Model<String>(TeachUsSession.get().getString("IncomePerMonthPage.futureLessons")), "futureLessonCount")); //$NON-NLS-1$ //$NON-NLS-2$
		columns.add(new RendererPropertyColumn<MonthIncome,Object>(new Model<String>(TeachUsSession.get().getString("General.total")), "total", new CurrencyChoiceRenderer())); //$NON-NLS-1$ //$NON-NLS-2$
		columns.add(new PropertyColumn<MonthIncome>(new Model<String>(TeachUsSession.get().getString("IncomePerMonthPage.totalLessons")), "totalLessonCount")); //$NON-NLS-1$ //$NON-NLS-2$
		
		add(new ListPanel<MonthIncome>("list", columns, data)); //$NON-NLS-1$
	}

	private MonthIncome getMonthIncome(List<MonthIncome> data, PupilBooking booking) {
		MonthIncome monthIncome = null;
		for (MonthIncome income : data) {
			if (income.getMonth() == booking.getDate().getMonthOfYear()) {
				monthIncome = income;
				break;
			}
		}
		
		if (monthIncome == null) {
			monthIncome = new MonthIncome();
			monthIncome.setMonth(booking.getDate().getMonthOfYear());
			data.add(monthIncome);
		}
		return monthIncome;
	}

}
