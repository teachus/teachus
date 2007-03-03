package dk.teachus.frontend.pages;

import java.util.List;

import wicket.extensions.markup.html.repeater.data.table.IColumn;
import wicket.model.Model;
import dk.teachus.dao.BookingDAO;
import dk.teachus.domain.PupilBooking;
import dk.teachus.domain.Teacher;
import dk.teachus.frontend.TeachUsApplication;
import dk.teachus.frontend.TeachUsSession;
import dk.teachus.frontend.UserLevel;
import dk.teachus.frontend.components.LinkPropertyColumn;
import dk.teachus.frontend.components.ListPanel;
import dk.teachus.frontend.components.RendererPropertyColumn;
import dk.teachus.frontend.pages.persons.PupilPage;
import dk.teachus.frontend.utils.CurrencyChoiceRenderer;
import dk.teachus.frontend.utils.DateChoiceRenderer;
import dk.teachus.frontend.utils.TimeChoiceRenderer;

public class AgendaPage extends AuthenticatedBasePage {
	private static final long serialVersionUID = 1L;

	public AgendaPage() {
		super(UserLevel.TEACHER, true);
		
		Teacher teacher = (Teacher) TeachUsSession.get().getPerson();
		
		BookingDAO bookingDAO = TeachUsApplication.get().getBookingDAO();
		List<PupilBooking> bookings = bookingDAO.getFutureBookingsForTeacher(teacher);
		
		IColumn[] columns = new IColumn[] {
				new LinkPropertyColumn(new Model(TeachUsSession.get().getString("General.pupil")), "pupil.name") {
					private static final long serialVersionUID = 1L;

					@Override
					protected void onClick(Object rowModelObject) {
						PupilBooking booking = (PupilBooking) rowModelObject;
						getRequestCycle().setResponsePage(new PupilPage(booking.getPupil()));
					}
				},
				new RendererPropertyColumn(new Model(TeachUsSession.get().getString("General.date")), "date", new DateChoiceRenderer()),
				new RendererPropertyColumn(new Model(TeachUsSession.get().getString("General.time")), "date", new TimeChoiceRenderer()),
				new RendererPropertyColumn(new Model(TeachUsSession.get().getString("General.phoneNumber")), "pupil.phoneNumber"),
				new RendererPropertyColumn(new Model(TeachUsSession.get().getString("General.price")), "period.price", new CurrencyChoiceRenderer())
		};
		
		add(new ListPanel("list", columns, bookings));
	}

	@Override
	protected String getPageLabel() {
		return TeachUsSession.get().getString("General.agenda"); //$NON-NLS-1$
	}

	@Override
	protected PageCategory getPageCategory() {
		return PageCategory.AGENDA;
	}

}
