package dk.frankbille.teachus.frontend.pages;

import java.util.List;

import org.jfree.data.general.DefaultPieDataset;

import dk.frankbille.teachus.dao.BookingDAO;
import dk.frankbille.teachus.domain.PupilBooking;
import dk.frankbille.teachus.domain.Teacher;
import dk.frankbille.teachus.frontend.TeachUsApplication;
import dk.frankbille.teachus.frontend.TeachUsSession;
import dk.frankbille.teachus.frontend.UserLevel;
import dk.frankbille.teachus.frontend.components.JFreeChartImage;
import dk.frankbille.teachus.frontend.resources.PieChartResource;
import dk.frankbille.teachus.frontend.utils.Icons;
import wicket.ResourceReference;
import wicket.RestartResponseAtInterceptPageException;
import wicket.protocol.http.WebApplication;

public class StatsPage extends AuthenticatedBasePage {
	private static final long serialVersionUID = 1L;

	public StatsPage() {
		super(UserLevel.TEACHER);
		
		if (TeachUsSession.get().getUserLevel() != UserLevel.TEACHER) {
			throw new RestartResponseAtInterceptPageException(WebApplication.get().getHomePage());
		}
		
		Teacher teacher = (Teacher) TeachUsSession.get().getPerson();
		
		BookingDAO bookingDAO = TeachUsApplication.get().getBookingDAO();
		List<PupilBooking> bookings = bookingDAO.getPaidBookings(teacher);
		
		DefaultPieDataset pieDataset = new DefaultPieDataset();
		
		for (PupilBooking booking : bookings) {
			double value = booking.getPeriod().getPrice();
			
			String key = booking.getPupil().getName();
			if (pieDataset.getIndex(key) > -1) {
				value += pieDataset.getValue(key).doubleValue();
			}
			
			pieDataset.setValue(key, value);
		}
		
		add(new JFreeChartImage("chart", new PieChartResource(600, 300, pieDataset)));
	}

	@Override
	protected ResourceReference getPageIcon() {
		return Icons.STATS;
	}

	@Override
	protected String getPageLabel() {
		return "Statistik";
	}

}
