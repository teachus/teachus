package dk.teachus.frontend.components.list;

import java.util.List;

import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

public class FunctionColumnPanel<T> extends Panel {
	private static final long serialVersionUID = 1L;

	public FunctionColumnPanel(String id, List<FunctionItem<T>> items, final IModel<T> rowModel) {
		super(id);
		
		add(new ListView<FunctionItem<T>>("items", items) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(ListItem<FunctionItem<T>> item) {
				FunctionItem<T> functionItem = item.getModelObject();
				
				item.setRenderBodyOnly(true);
				
				item.add(functionItem.createComponent("item", rowModel));
			}
		});
	}
	
}
