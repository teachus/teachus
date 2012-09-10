package dk.teachus.frontend.pages.settings;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.extensions.markup.html.repeater.data.sort.SortOrder;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;

import dk.teachus.backend.dao.BookingDAO;
import dk.teachus.backend.domain.Booking;
import dk.teachus.backend.domain.Bookings;
import dk.teachus.backend.domain.Teacher;
import dk.teachus.frontend.TeachUsApplication;
import dk.teachus.frontend.TeachUsSession;
import dk.teachus.frontend.components.list.BookingTypeComparator;
import dk.teachus.frontend.components.list.DateTimeComparator;
import dk.teachus.frontend.components.list.ListPanel;
import dk.teachus.frontend.components.list.RendererPropertyColumn;
import dk.teachus.frontend.components.list.TeachUsSortableDataProvider;
import dk.teachus.frontend.utils.BookingTypeRenderer;
import dk.teachus.frontend.utils.DateChoiceRenderer;
import dk.teachus.frontend.utils.TimeChoiceRenderer;

public class InvalidBookingsPage extends AbstractSettingsPage {
	
	private static class InvalidBookingsModel extends LoadableDetachableModel<List<Booking>> {
		private static final long serialVersionUID = 1L;

		@Override
		protected List<Booking> load() {
			List<Booking> invalidBookings = new ArrayList<Booking>();
			
			Teacher teacher = TeachUsSession.get().getTeacher();
			
			BookingDAO bookingDAO = TeachUsApplication.get().getBookingDAO();
			Bookings bookings = bookingDAO.getAllBookings(teacher);
			List<Booking> bookingList = bookings.getBookingList();
			for (Booking booking : bookingList) {
				if (booking.getPeriod().mayBook(booking.getDate()) == false) {
					invalidBookings.add(booking);
				}
			}
			
			return invalidBookings;
		}
	}
	
	private static class InvalidBookingsDataProvider extends TeachUsSortableDataProvider<Booking> {
		private static final long serialVersionUID = 1L;

		public InvalidBookingsDataProvider(IModel<List<Booking>> listModel) {
			super(listModel);
		
			addComparator("date", new DateTimeComparator());
			addComparator("class", new BookingTypeComparator());

			setSort("date", SortOrder.DESCENDING);
		}		
	}
	
	public InvalidBookingsPage() {
		List<IColumn> columns = new ArrayList<IColumn>();
		columns.add(new RendererPropertyColumn(new Model<String>(TeachUsSession.get().getString("General.date")), "date", "date", new DateChoiceRenderer()));
		columns.add(new RendererPropertyColumn(new Model<String>(TeachUsSession.get().getString("General.time")), "date", "date", new TimeChoiceRenderer()));
		columns.add(new RendererPropertyColumn(new Model<String>(TeachUsSession.get().getString("General.bookingType")), "class", "class", new BookingTypeRenderer()));
		
		add(new ListPanel("list", columns, new InvalidBookingsDataProvider(new InvalidBookingsModel())));
	}
	
	@Override
	protected String getPageLabel() {
		return TeachUsSession.get().getString("Settings.invalidBookings");
	}
	
}
