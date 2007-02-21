package dk.frankbille.teachus.frontend.pages;

import java.util.List;

import wicket.ResourceReference;
import wicket.markup.html.WebMarkupContainer;
import wicket.markup.html.basic.Label;
import wicket.markup.repeater.RepeatingView;
import wicket.model.Model;
import dk.frankbille.teachus.dao.BookingDAO;
import dk.frankbille.teachus.domain.PupilBooking;
import dk.frankbille.teachus.domain.Teacher;
import dk.frankbille.teachus.frontend.TeachUsApplication;
import dk.frankbille.teachus.frontend.TeachUsSession;
import dk.frankbille.teachus.frontend.UserLevel;
import dk.frankbille.teachus.frontend.components.RenderingLabel;
import dk.frankbille.teachus.frontend.utils.CurrencyChoiceRenderer;
import dk.frankbille.teachus.frontend.utils.DateChoiceRenderer;
import dk.frankbille.teachus.frontend.utils.Resources;
import dk.frankbille.teachus.frontend.utils.TimeChoiceRenderer;

public class AgendaPage extends AuthenticatedBasePage {
	private static final long serialVersionUID = 1L;

	public AgendaPage() {
		super(UserLevel.TEACHER);
		
		Teacher teacher = (Teacher) TeachUsSession.get().getPerson();
		
		BookingDAO bookingDAO = TeachUsApplication.get().getBookingDAO();
		List<PupilBooking> bookings = bookingDAO.getFutureBookingsForTeacher(teacher);
		
		// HEADER
		add(new Label("pupil", TeachUsSession.get().getString("General.pupil"))); //$NON-NLS-1$ //$NON-NLS-2$
		add(new Label("date", TeachUsSession.get().getString("General.date"))); //$NON-NLS-1$ //$NON-NLS-2$
		add(new Label("time", TeachUsSession.get().getString("General.time"))); //$NON-NLS-1$ //$NON-NLS-2$
		add(new Label("phoneNumber", TeachUsSession.get().getString("General.phoneNumber"))); //$NON-NLS-1$ //$NON-NLS-2$
		add(new Label("price", TeachUsSession.get().getString("General.price"))); //$NON-NLS-1$ //$NON-NLS-2$
		
		// BODY
		RepeatingView rows = new RepeatingView("rows"); //$NON-NLS-1$
		add(rows);
		
		for (PupilBooking booking : bookings) {
			WebMarkupContainer row = new WebMarkupContainer(rows.newChildId());
			rows.add(row);
			
			row.add(new Label("name", booking.getPupil().getName())); //$NON-NLS-1$
			row.add(new RenderingLabel("date", new Model(booking.getDate()), new DateChoiceRenderer())); //$NON-NLS-1$
			row.add(new RenderingLabel("time", new Model(booking.getDate()), new TimeChoiceRenderer())); //$NON-NLS-1$
			row.add(new Label("phoneNumber", booking.getPupil().getPhoneNumber())); //$NON-NLS-1$
			row.add(new RenderingLabel("price", new Model(booking.getPeriod().getPrice()), new CurrencyChoiceRenderer())); //$NON-NLS-1$
		}
	}

	@Override
	protected ResourceReference getPageIcon() {
		return Resources.AGENDA;
	}

	@Override
	protected String getPageLabel() {
		return TeachUsSession.get().getString("General.agenda"); //$NON-NLS-1$
	}

}
