package dk.teachus.frontend.components.list;

import org.apache.wicket.extensions.markup.html.repeater.data.table.filter.IFilterStateLocator;

public interface TeachUsFilter extends IFilterStateLocator {
	
	void onSubmit();

	void reset();
	
}
