package dk.frankbille.teachus.frontend.components;

import java.util.ArrayList;
import java.util.List;

import wicket.extensions.markup.html.repeater.data.table.AbstractColumn;
import wicket.extensions.markup.html.repeater.data.table.DataTable;
import wicket.extensions.markup.html.repeater.data.table.IColumn;
import wicket.markup.html.basic.Label;
import wicket.markup.html.panel.Panel;
import wicket.markup.repeater.Item;
import wicket.model.IModel;
import wicket.model.Model;
import wicket.util.tester.TestPanelSource;
import dk.frankbille.teachus.frontend.WicketTestCase;

public class TestListPanel extends WicketTestCase {
	private static final long serialVersionUID = 1L;

	public void testRender() {
		tester.startPanel(new TestPanelSource() {
			private static final long serialVersionUID = 1L;

			public Panel getTestPanel(String panelId) {
				List<String> data = new ArrayList<String>(); 
				
				IColumn[] columns = new IColumn[] {
						new AbstractColumn(new Model("Header")) {
							private static final long serialVersionUID = 1L;

							public void populateItem(Item cellItem, String componentId, IModel rowModel) {
								cellItem.add(new Label(componentId, rowModel));
							}
						}
				};
				
				return new ListPanel(panelId, columns, data);
			}
		});
		
		tester.assertComponent("panel", ListPanel.class);
		tester.assertComponent("panel:table", DataTable.class);
	}
	
}
