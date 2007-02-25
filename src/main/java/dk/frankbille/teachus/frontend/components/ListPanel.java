package dk.frankbille.teachus.frontend.components;

import java.util.List;

import dk.frankbille.teachus.frontend.TeachUsSession;

import wicket.Component;
import wicket.extensions.markup.html.repeater.data.table.DataTable;
import wicket.extensions.markup.html.repeater.data.table.HeadersToolbar;
import wicket.extensions.markup.html.repeater.data.table.IColumn;
import wicket.extensions.markup.html.repeater.data.table.NavigationToolbar;
import wicket.markup.html.WebComponent;
import wicket.markup.html.basic.Label;
import wicket.markup.html.panel.Panel;
import wicket.markup.repeater.data.ListDataProvider;
import wicket.model.AbstractReadOnlyModel;

public class ListPanel extends Panel {
	private static final long serialVersionUID = 1L;

	public ListPanel(String id, IColumn[] columns, List data) {
		super(id);
		
		DataTable dataTable = new DataTable("table", columns, new ListDataProvider(data), 20);
		dataTable.addTopToolbar(new HeadersToolbar(dataTable, null));
		dataTable.addBottomToolbar(new NavigationToolbar(dataTable) {
			private static final long serialVersionUID = 1L;

			@Override
			protected WebComponent newNavigatorLabel(String navigatorId, final DataTable table) {
				return new Label(navigatorId, new AbstractReadOnlyModel() {
					private static final long serialVersionUID = 1L;

					@Override
					public Object getObject(Component component) {
						int of = table.getRowCount();
						int from = table.getCurrentPage() * table.getRowsPerPage();
						int to = Math.min(of, from + table.getRowsPerPage());

						from++;

						if (of == 0)
						{
							from = 0;
							to = 0;
						}

						String label = TeachUsSession.get().getString("ListPanel.navigatorLabel");
						label = label.replace("${from}", ""+from);
						label = label.replace("${to}", ""+to);
						label = label.replace("${of}", ""+of);
						
						return label;
					}					
				});
			}
		});
		add(dataTable);
	}

}
