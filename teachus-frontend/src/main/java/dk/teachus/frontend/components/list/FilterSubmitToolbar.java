package dk.teachus.frontend.components.list;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.behavior.SimpleAttributeModifier;
import org.apache.wicket.extensions.markup.html.repeater.data.table.AbstractToolbar;
import org.apache.wicket.extensions.markup.html.repeater.data.table.DataTable;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.model.Model;

public class FilterSubmitToolbar extends AbstractToolbar {
	private static final long serialVersionUID = 1L;

	public FilterSubmitToolbar(DataTable table, final TeachUsFilter filter) {
		super(table);
		
		WebMarkupContainer td = new WebMarkupContainer("td");
		add(td);
		
		td.add(new AttributeModifier("colspan", true, new Model(String
				.valueOf(table.getColumns().length))));
		
		Button filterSubmit = new Button("filterSubmit");
		filterSubmit.add(new SimpleAttributeModifier("value", "Filter"));
		td.add(filterSubmit);
		
		Button filterReset = new Button("filterReset") {
			private static final long serialVersionUID = 1L;

			@Override
			public void onSubmit() {
				filter.reset();
			}
		};
		filterReset.add(new SimpleAttributeModifier("value", "Show all"));
		td.add(filterReset);
	}
	
}
