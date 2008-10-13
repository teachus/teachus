package dk.teachus.frontend.components.list;

import java.io.Serializable;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.wicket.extensions.markup.html.repeater.util.SortParam;
import org.apache.wicket.extensions.markup.html.repeater.util.SortableDataProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.util.lang.Objects;
import org.apache.wicket.util.lang.PropertyResolver;

public abstract class TeachUsSortableDataProvider<O> extends SortableDataProvider {
	private static final long serialVersionUID = 1L;

	private class DelegatedComparator implements Comparator<O> {
		private final SortParam sortParam;
		
		public DelegatedComparator(SortParam sortParam) {
			this.sortParam = sortParam;
		}

		@SuppressWarnings("unchecked")
		public int compare(O o1, O o2) {
			int compare = 0;
			
			if (o1 != null && o2 != null) {
				Comparator foundComparator = null;
				for (String sortProperty : comparators.keySet()) {
					if (Objects.equal(sortParam.getProperty(), sortProperty)) {
						foundComparator = comparators.get(sortProperty);
						break;
					}
				}
				
				if (foundComparator != null) {
					Object property1 = PropertyResolver.getValue(sortParam.getProperty(), o1);
					Object property2 = PropertyResolver.getValue(sortParam.getProperty(), o2);
					compare = foundComparator.compare(property1, property2);
					
					if (sortParam.isAscending() == false) {
						compare *= -1;
					}
				}
			} else if (o1 != null) {
				compare = -1;
			} else if (o2 != null) {
				compare = 1;
			}
			
			return compare;
		}
		
	}
	
	private IModel listModel;
	private Map<String, Comparator<?>> comparators; 
	
	public TeachUsSortableDataProvider(IModel listModel) {
		this.listModel = listModel;
		this.comparators = new HashMap<String, Comparator<?>>();
	}

	public Iterator<?> iterator(int first, int count) {		
		List<O> sortedList = getSortedList();
		
		int toIndex = first + count;
		if (toIndex > sortedList.size())
		{
			toIndex = sortedList.size();
		}
		return sortedList.subList(first, toIndex).listIterator();
	}
	
	public IModel model(Object object) {
		return new Model((Serializable) object);
	}
	
	public int size() {
		return getSortedList().size();
	}
	
	@SuppressWarnings("unchecked")
	protected List<O> getList() {
		return (List<O>) listModel.getObject();
	}
	
	protected List<O> getSortedList() {
		SortParam sortParam = getSort();
		List<O> list = getList();
		Collections.sort(list, new DelegatedComparator(sortParam));
		return list;
		
	}
	
	protected void addComparator(String sortProperty, Comparator<?> comparator) {
		comparators.put(sortProperty, comparator);
	}
	
}
