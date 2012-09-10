package dk.teachus.frontend.components.list;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.extensions.markup.html.repeater.data.table.AbstractToolbar;
import org.apache.wicket.extensions.markup.html.repeater.data.table.DataTable;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Button;

import dk.teachus.frontend.TeachUsSession;

public class FilterSubmitToolbar extends AbstractToolbar {
	private static final long serialVersionUID = 1L;

	public FilterSubmitToolbar(DataTable table, final TeachUsFilter filter) {
		super(table);
		
		WebMarkupContainer td = new WebMarkupContainer("td"); //$NON-NLS-1$
		add(td);
		
		td.add(AttributeModifier.replace("colspan", String //$NON-NLS-1$
				.valueOf(table.getColumns().size())));
		
		Button filterSubmit = new Button("filterSubmit"); //$NON-NLS-1$
		filterSubmit.add(AttributeModifier.replace("value", TeachUsSession.get().getString("FilterSubmitToolbar.filter"))); //$NON-NLS-1$ //$NON-NLS-2$
		td.add(filterSubmit);
		
		Button filterReset = new Button("filterReset") { //$NON-NLS-1$
			private static final long serialVersionUID = 1L;

			@Override
			public void onSubmit() {
				filter.reset();
			}
		};
		filterReset.add(AttributeModifier.replace("value", TeachUsSession.get().getString("FilterSubmitToolbar.showAll"))); //$NON-NLS-1$ //$NON-NLS-2$
		td.add(filterReset);
	}
	
}
