package dk.teachus.frontend.pages.periods;

import org.apache.wicket.model.IModel;

import dk.teachus.backend.domain.Period;
import dk.teachus.frontend.components.list.DateComparator;
import dk.teachus.frontend.components.list.DoubleComparator;
import dk.teachus.frontend.components.list.StringComparator;
import dk.teachus.frontend.components.list.TeachUsSortableDataProvider;
import dk.teachus.frontend.components.list.TimeComparator;
import dk.teachus.frontend.components.list.WeekDayComparator;

public class PeriodsDataProvider extends TeachUsSortableDataProvider<Period> {
	private static final long serialVersionUID = 1L;

	public PeriodsDataProvider(IModel listModel) {
		super(listModel);
		
		addComparator("name", new StringComparator());
		addComparator("beginDate", new DateComparator());
		addComparator("endDate", new DateComparator());
		addComparator("startTime", new TimeComparator());
		addComparator("endTime", new TimeComparator());
		addComparator("weekDays", new WeekDayComparator());
		addComparator("price", new DoubleComparator());
		
		setSort("weekDays", true);
	}
	
}