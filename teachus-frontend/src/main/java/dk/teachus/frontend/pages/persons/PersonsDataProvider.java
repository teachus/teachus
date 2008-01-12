package dk.teachus.frontend.pages.persons;

import org.apache.wicket.model.IModel;

import dk.teachus.backend.domain.Person;
import dk.teachus.backend.domain.impl.PersonImpl;
import dk.teachus.frontend.components.list.StringComparator;
import dk.teachus.frontend.components.list.StringFilter;
import dk.teachus.frontend.components.list.TeachUsFilteredSortableDataProvider;

public class PersonsDataProvider extends TeachUsFilteredSortableDataProvider<Person> {
	private static final long serialVersionUID = 1L;
	
	public PersonsDataProvider(IModel personsModel) {
		super(personsModel);
		
		StringComparator stringComparator = new StringComparator();
		addComparator("name", stringComparator);
		addComparator("username", stringComparator);
		addComparator("email", stringComparator);
		addComparator("phoneNumber", stringComparator);
		
		setSort("name", true);
		
		StringFilter stringFilter = new StringFilter();
		addFilter("name", stringFilter);
		addFilter("username", stringFilter);
		addFilter("email", stringFilter);
		addFilter("phoneNumber", stringFilter);
	}
	
	@Override
	protected Person createStateObject() {
		return new PersonImpl() {
			private static final long serialVersionUID = 1L;
		};
	}
	
}
