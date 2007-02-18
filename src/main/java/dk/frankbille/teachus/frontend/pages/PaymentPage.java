package dk.frankbille.teachus.frontend.pages;

import wicket.ResourceReference;
import wicket.RestartResponseAtInterceptPageException;
import wicket.extensions.markup.html.repeater.data.table.DataTable;
import wicket.extensions.markup.html.repeater.data.table.HeadersToolbar;
import wicket.extensions.markup.html.repeater.data.table.IColumn;
import wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import wicket.markup.repeater.data.ListDataProvider;
import wicket.model.Model;
import wicket.protocol.http.WebApplication;
import dk.frankbille.teachus.dao.BookingDAO;
import dk.frankbille.teachus.domain.Pupil;
import dk.frankbille.teachus.domain.PupilBookings;
import dk.frankbille.teachus.domain.Teacher;
import dk.frankbille.teachus.frontend.TeachUsApplication;
import dk.frankbille.teachus.frontend.TeachUsSession;
import dk.frankbille.teachus.frontend.UserLevel;
import dk.frankbille.teachus.frontend.components.RendererPropertyColumn;
import dk.frankbille.teachus.frontend.utils.CurrencyChoiceRenderer;
import dk.frankbille.teachus.frontend.utils.DateChoiceRenderer;
import dk.frankbille.teachus.frontend.utils.Icons;
import dk.frankbille.teachus.frontend.utils.TimeChoiceRenderer;

public class PaymentPage extends AuthenticatedBasePage {
	private static final long serialVersionUID = 1L;

	public PaymentPage() {
		super(UserLevel.PUPIL);
		
		BookingDAO bookingDAO = TeachUsApplication.get().getBookingDAO();
		PupilBookings pupilBookings = null;
		if (TeachUsSession.get().getPerson() instanceof Pupil) {
			pupilBookings = bookingDAO.getUnpaidBookings((Pupil) TeachUsSession.get().getPerson());
		} else if (TeachUsSession.get().getPerson() instanceof Teacher) {
			pupilBookings = bookingDAO.getUnpaidBookings((Teacher) TeachUsSession.get().getPerson());
		} else {
			throw new RestartResponseAtInterceptPageException(WebApplication.get().getHomePage());
		}
		
		init(pupilBookings);
	}
	
	private void init(final PupilBookings pupilBookings) {
		IColumn[] columns = new IColumn[] {
				new PropertyColumn(new Model(TeachUsSession.get().getString("General.pupil")), "pupil.name"), //$NON-NLS-1$ //$NON-NLS-2$
				new RendererPropertyColumn(new Model(TeachUsSession.get().getString("General.date")), "date", new DateChoiceRenderer()), //$NON-NLS-1$ //$NON-NLS-2$
				new RendererPropertyColumn(new Model(TeachUsSession.get().getString("General.time")), "date", new TimeChoiceRenderer()), //$NON-NLS-1$ //$NON-NLS-2$
				new RendererPropertyColumn(new Model(TeachUsSession.get().getString("General.price")), "period.price", new CurrencyChoiceRenderer()) //$NON-NLS-1$ //$NON-NLS-2$
		};

		DataTable dataTable = new DataTable("list", columns, new ListDataProvider(pupilBookings.getBookingList()), 20); //$NON-NLS-1$
		dataTable.addTopToolbar(new HeadersToolbar(dataTable, null));
		add(dataTable);		
	}
	
	@Override
	protected ResourceReference getPageIcon() {
		return Icons.PAYMENT;
	}

	@Override
	protected String getPageLabel() {
		return TeachUsSession.get().getString("General.payment"); //$NON-NLS-1$
	}

}
