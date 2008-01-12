package dk.teachus.frontend.pages.persons;

import org.apache.wicket.extensions.markup.html.repeater.data.table.filter.IFilterStateLocator;
import org.apache.wicket.model.IModel;

import dk.teachus.backend.domain.Person;
import dk.teachus.frontend.components.StringComparator;
import dk.teachus.frontend.components.TeachUsSortableDataProvider;

public class PersonsDataProvider extends TeachUsSortableDataProvider<Person> implements IFilterStateLocator {
	private static final long serialVersionUID = 1L;
	
	public PersonsDataProvider(IModel personsModel) {
		super(personsModel);
		
		StringComparator stringComparator = new StringComparator();
		addComparator("name", stringComparator);
		addComparator("username", stringComparator);
		addComparator("email", stringComparator);
		addComparator("phoneNumber", stringComparator);
		
		setSort("name", true);
	}

	public Object getFilterState() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public void setFilterState(Object state) {
		// TODO Auto-generated method stub
		
	}
	
}
