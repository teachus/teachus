package dk.teachus.frontend.pages.persons;

import java.io.Serializable;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import org.apache.wicket.extensions.markup.html.repeater.data.table.filter.IFilterStateLocator;
import org.apache.wicket.extensions.markup.html.repeater.util.SortParam;
import org.apache.wicket.extensions.markup.html.repeater.util.SortableDataProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.util.lang.Objects;

import dk.teachus.backend.domain.Person;

public class PersonsDataProvider extends SortableDataProvider implements IFilterStateLocator {
	private static final long serialVersionUID = 1L;
	
	private static class StringComparator implements Comparator<String> {
		public int compare(String o1, String o2) {
			int compare = 0;
			
			if (o1 != null && o2 != null) {
				compare = o1.compareTo(o2);
			} else if (o1 != null) {
				compare = -1;
			} else if (o2 != null) {
				compare = 1;
			}
			
			return compare;
		} 
	}
	
	private static class PersonComparator implements Comparator<Person> {

		private final SortParam sortParam;
		
		public PersonComparator(SortParam sortParam) {
			this.sortParam = sortParam;
		}

		public int compare(Person p1, Person p2) {
			int compare = 0;
			
			if (p1 != null && p2 != null) {
				if (Objects.equal(sortParam.getProperty(), "name")) {
					compare = new StringComparator().compare(p1.getName(), p2.getName());
				} else if (Objects.equal(sortParam.getProperty(), "username")) {
					compare = new StringComparator().compare(p1.getUsername(), p2.getUsername());
				} else if (Objects.equal(sortParam.getProperty(), "email")) {
					compare = new StringComparator().compare(p1.getEmail(), p2.getEmail());
				} else if (Objects.equal(sortParam.getProperty(), "phoneNumber")) {
					compare = new StringComparator().compare(p1.getPhoneNumber(), p2.getPhoneNumber());
				}
				
				if (sortParam.isAscending() == false) {
					compare *= -1;
				}
			} else if (p1 != null) {
				compare = -1;
			} else if (p2 != null) {
				compare = 1;
			}
			
			return compare;
		}
		
	}
	
	private IModel personsModel;

	public PersonsDataProvider(IModel personsModel) {
		this.personsModel = personsModel;
		setSort("name", true);
	}

	public Object getFilterState() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public void setFilterState(Object state) {
		// TODO Auto-generated method stub
		
	}
	
	public Iterator<?> iterator(int first, int count) {
		SortParam sort = getSort();
		
		List<Person> personList = getPersonList();
		Collections.sort(personList, new PersonComparator(sort));
		
		int toIndex = first + count;
		if (toIndex > personList.size())
		{
			toIndex = personList.size();
		}
		return personList.subList(first, toIndex).listIterator();
	}
	
	public IModel model(Object object) {
		return new Model((Serializable) object);
	}
	
	public int size() {
		return getPersonList().size();
	}
	
	@SuppressWarnings("unchecked")
	private List<Person> getPersonList() {
		return (List<Person>) personsModel.getObject();
	}
	
}
