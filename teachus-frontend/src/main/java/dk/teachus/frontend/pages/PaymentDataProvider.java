package dk.teachus.frontend.pages;

import org.apache.wicket.extensions.markup.html.repeater.data.sort.SortOrder;
import org.apache.wicket.model.IModel;

import dk.teachus.backend.domain.PupilBooking;
import dk.teachus.frontend.components.list.DateTimeComparator;
import dk.teachus.frontend.components.list.DoubleComparator;
import dk.teachus.frontend.components.list.StringComparator;
import dk.teachus.frontend.components.list.TeachUsSortableDataProvider;

public class PaymentDataProvider extends TeachUsSortableDataProvider<PupilBooking> {
	private static final long serialVersionUID = 1L;

	public PaymentDataProvider(IModel listModel) {
		super(listModel);
		
		addComparator("pupil.name", new StringComparator());
		addComparator("date", new DateTimeComparator());
		addComparator("period.price", new DoubleComparator());
		
		setSort("date", SortOrder.DESCENDING);
	}
	
}
