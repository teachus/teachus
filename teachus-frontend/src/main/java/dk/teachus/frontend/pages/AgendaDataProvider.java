package dk.teachus.frontend.pages;

import org.apache.wicket.model.IModel;

import dk.teachus.backend.domain.PupilBooking;
import dk.teachus.frontend.components.DateComparator;
import dk.teachus.frontend.components.DoubleComparator;
import dk.teachus.frontend.components.StringComparator;
import dk.teachus.frontend.components.TeachUsSortableDataProvider;

public class AgendaDataProvider extends TeachUsSortableDataProvider<PupilBooking> {
	private static final long serialVersionUID = 1L;

	public AgendaDataProvider(IModel listModel) {
		super(listModel);
		
		addComparator("pupil.name", new StringComparator());
		addComparator("date", new DateComparator());
		addComparator("pupil.phoneNumber", new StringComparator());
		addComparator("period.price", new DoubleComparator());
		
		setSort("date", true);
	}
	
}
