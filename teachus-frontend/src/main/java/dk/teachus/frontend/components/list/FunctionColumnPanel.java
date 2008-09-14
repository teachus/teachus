package dk.teachus.frontend.components.list;

import java.util.List;

import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

public class FunctionColumnPanel extends Panel {
	private static final long serialVersionUID = 1L;

	public FunctionColumnPanel(String id, List<FunctionItem> items, final IModel rowModel) {
		super(id);
		
		add(new ListView("items", items) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(ListItem item) {
				FunctionItem functionItem = (FunctionItem) item.getModelObject();
				
				item.setRenderBodyOnly(true);
				
				item.add(functionItem.createComponent("item", rowModel));
			}
		});
	}
	
}
