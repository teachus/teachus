package dk.frankbille.teachus.frontend.pages.stats;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.jfree.data.general.DefaultPieDataset;

import wicket.extensions.markup.html.repeater.data.table.DataTable;
import wicket.extensions.markup.html.repeater.data.table.HeadersToolbar;
import wicket.extensions.markup.html.repeater.data.table.IColumn;
import wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import wicket.extensions.yui.calendar.DateField;
import wicket.markup.html.basic.Label;
import wicket.markup.html.form.Button;
import wicket.markup.html.form.Form;
import wicket.markup.repeater.data.ListDataProvider;
import wicket.model.CompoundPropertyModel;
import wicket.model.Model;
import dk.frankbille.teachus.dao.BookingDAO;
import dk.frankbille.teachus.domain.Pupil;
import dk.frankbille.teachus.domain.PupilBooking;
import dk.frankbille.teachus.frontend.TeachUsApplication;
import dk.frankbille.teachus.frontend.TeachUsSession;
import dk.frankbille.teachus.frontend.components.JFreeChartImage;
import dk.frankbille.teachus.frontend.components.RendererPropertyColumn;
import dk.frankbille.teachus.frontend.resources.PieChartResource;
import dk.frankbille.teachus.frontend.utils.CurrencyChoiceRenderer;
import dk.frankbille.teachus.frontend.utils.PercentChoiceRenderer;

public class IncomePerPupilPage extends AbstractStatisticsPage {
	private static final long serialVersionUID = 1L;

	private Date startDate;
	
	private Date endDate;
	
	public IncomePerPupilPage() {
		this(null, null);
	}
	
	public IncomePerPupilPage(Date startDate, Date endDate) {
		this.startDate = startDate;
		this.endDate = endDate;
		
		Form form = new Form("form", new CompoundPropertyModel(this));
		add(form);
		
		form.add(new Label("startDateLabel", TeachUsSession.get().getString("General.startDate")));
		form.add(new DateField("startDate"));
		
		form.add(new Label("endDateLabel", TeachUsSession.get().getString("General.endDate")));
		form.add(new DateField("endDate"));
		
		form.add(new Button("execute", new Model("Execute")) {
			private static final long serialVersionUID = 1L;

			@Override
			public void onSubmit() {
				getRequestCycle().setResponsePage(new IncomePerPupilPage(getStartDate(), getEndDate()));
			}
		});
		
		createPercentDistribution();
	}

	private void createPercentDistribution() {
		add(new Label("pctDistribution", "Percentage distribution"));
		
		BookingDAO bookingDAO = TeachUsApplication.get().getBookingDAO();
		List<PupilBooking> bookings = bookingDAO.getPaidBookings(getTeacher(), startDate, endDate);
		
		// Build the dataset
		List<PupilSummary> sumList = new ArrayList<PupilSummary>();
		double total = 0;
		for (PupilBooking booking : bookings) {
			PupilSummary pupilSummary = new PupilSummary(booking.getPupil());
			if (sumList.contains(pupilSummary)) {
				pupilSummary = sumList.get(sumList.indexOf(pupilSummary));
			} else {
				sumList.add(pupilSummary);
			}
			
			total += booking.getPeriod().getPrice();
			pupilSummary.addAmount(booking.getPeriod().getPrice());
		}
		for (PupilSummary summary : sumList) {
			summary.calculatePercent(total);
		}
		
		Collections.sort(sumList, new PupilSummaryComparator());
		
		// Sheet
		IColumn[] columns = new IColumn[] {
				new PropertyColumn(new Model("Sanger"), "pupil.name"),
				new RendererPropertyColumn(new Model("Total"), "total", new CurrencyChoiceRenderer()),
				new RendererPropertyColumn(new Model("Procent"), "percent", new PercentChoiceRenderer())
		};
		
		DataTable pctDistributionSheet = new DataTable("pctDistributionSheet", columns, new ListDataProvider(sumList), 20);
		pctDistributionSheet.addTopToolbar(new HeadersToolbar(pctDistributionSheet, null));
		add(pctDistributionSheet);
		
		// Chart
		DefaultPieDataset pieDataset = new DefaultPieDataset();
		for (PupilSummary summary : sumList) {
			pieDataset.setValue(summary.getPupil().getName(), summary.getTotal());
		}
		
		add(new JFreeChartImage("pctDistributionChart", new PieChartResource(600, 300, pieDataset)));
	}

	@Override
	protected String getPageLabel() {
		return "Income per pupil";
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	private static class PupilSummary implements Serializable {
		private static final long serialVersionUID = 1L;

		private double total;

		private Pupil pupil;
		
		private double percent;

		public PupilSummary(Pupil pupil) {
			this.pupil = pupil;
		}

		public Pupil getPupil() {
			return pupil;
		}

		public double getTotal() {
			return total;
		}

		public void setTotal(double total) {
			this.total = total;
		}

		public void addAmount(double amount) {
			total += amount;
		}

		public double getPercent() {
			return percent;
		}
		
		public void calculatePercent(double overAllTotal) {
			percent = total / overAllTotal;
		}

		public boolean equals(Object o) {
			boolean equals = false;

			if (this == o) {
				equals = true;
			} else if (o != null) {
				if (o instanceof PupilSummary) {
					PupilSummary pupilSummary = (PupilSummary) o;

					if (pupilSummary.getPupil() == getPupil()) {
						equals = true;
					}
				}
			}

			return equals;
		}
	}
	
	private static class PupilSummaryComparator implements Comparator<PupilSummary> {
		public int compare(PupilSummary o1, PupilSummary o2) {
			int compare = 0;
			
			if (o1 != null && o2 != null) {
				compare = new Double(o2.getTotal()).compareTo(o1.getTotal());
				if (compare == 0) {
					if (o1.getPupil() != null && o2.getPupil() != null) {
						compare = o1.getPupil().getName().compareTo(o2.getPupil().getName());
					} else if (o1.getPupil() != null) {
						compare = -1;
					} else if (o2.getPupil() != null) {
						compare = 1;
					}
				}
			} else if (o1 != null) {
				compare = -1;
			} else if (o2 != null) {
				compare = 1;
			}
			
			return compare;
		}
	}
	
}
