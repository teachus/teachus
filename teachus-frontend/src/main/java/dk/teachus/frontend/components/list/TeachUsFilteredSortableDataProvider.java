package dk.teachus.frontend.components.list;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.wicket.model.IModel;
import org.apache.wicket.util.lang.PropertyResolver;

public abstract class TeachUsFilteredSortableDataProvider<O> extends TeachUsSortableDataProvider<O> implements TeachUsFilter<O> {
	private static final long serialVersionUID = 1L;
	
	private O stateObject;
	private Map<String, IFilter<?>> filters;
	
	public TeachUsFilteredSortableDataProvider(IModel<List<O>> listModel) {
		super(listModel);
		
		stateObject = createStateObject();
		filters = new HashMap<String, IFilter<?>>();
	}
	
	@Override
	protected List<O> getList() {
		List<O> list = super.getList();
		
		return filter(list, stateObject);
	}
		
	public void onSubmit() {
	}
	
	public void reset() {
		setFilterState(null);
	}

	public O getFilterState() {
		return stateObject;
	}
	
	public void setFilterState(O state) {
		if (state == null) {
			state = createStateObject();
		}
		
		this.stateObject = state;
	}
	
	protected void addFilter(String filterProperty, IFilter<?> filter) {
		filters.put(filterProperty, filter);
	}
	
	protected abstract O createStateObject();
	
	@SuppressWarnings("unchecked")
	protected List<O> filter(List<O> list, O stateObject) {
		List<O> filteredList = new ArrayList<O>();
		
		for (O object : list) {
			boolean include = true;
			
			for (String filterProperty : filters.keySet()) {
				@SuppressWarnings("rawtypes")
				IFilter filter = filters.get(filterProperty);
				
				Object objectProperty = PropertyResolver.getValue(filterProperty, object);
				Object stateProperty = PropertyResolver.getValue(filterProperty, stateObject);
				
				if (filter.include(objectProperty, stateProperty) == false) {
					include = false;
					break;
				}
			}
			
			if (include) {
				filteredList.add(object);
			}
		}
		
		return filteredList;
	}
	
}
