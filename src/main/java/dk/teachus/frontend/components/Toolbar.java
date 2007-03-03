package dk.teachus.frontend.components;

import java.io.Serializable;
import java.util.List;

import wicket.markup.html.WebMarkupContainer;
import wicket.markup.html.basic.Label;
import wicket.markup.html.link.Link;
import wicket.markup.html.panel.Panel;
import wicket.markup.repeater.RepeatingView;

public class Toolbar extends Panel {
	private static final long serialVersionUID = 1L;

	public Toolbar(String id, List<ToolbarItem> items) {
		super(id);
		
		RepeatingView itemsContainer = new RepeatingView("items"); //$NON-NLS-1$
		add(itemsContainer);
		
		for (final ToolbarItem item : items) {
			WebMarkupContainer itemContainer = new WebMarkupContainer(itemsContainer.newChildId());
			itemsContainer.add(itemContainer);
			
			Link link = new Link("link") {
				private static final long serialVersionUID = 1L;

				@Override
				public void onClick() {
					item.onEvent();
				}				
			};
			itemContainer.add(link);
			link.add(new Label("label", item.getLabel()).setRenderBodyOnly(true)); //$NON-NLS-1$
		}
	}
	
	public abstract static class ToolbarItem implements Serializable {		
		private String label;
		
		public ToolbarItem(String label) {
			this.label = label;
		}

		public String getLabel() {
			return label;
		}

		public void setLabel(String label) {
			this.label = label;
		}

		public abstract void onEvent();
	}

}
