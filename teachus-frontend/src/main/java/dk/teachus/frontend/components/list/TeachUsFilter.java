package dk.teachus.frontend.components.list;

import org.apache.wicket.extensions.markup.html.repeater.data.table.filter.IFilterStateLocator;

public interface TeachUsFilter<T> extends IFilterStateLocator<T> {
	
	void onSubmit();

	void reset();
	
}
