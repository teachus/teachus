package dk.frankbille.teachus.frontend.components;

import java.io.Serializable;
import java.util.List;

import wicket.ajax.AjaxRequestTarget;
import wicket.ajax.markup.html.AjaxLink;
import wicket.markup.html.basic.Label;
import wicket.markup.html.panel.Panel;
import wicket.markup.repeater.RepeatingView;

public class Toolbar extends Panel {
	private static final long serialVersionUID = 1L;

	public Toolbar(String id, List<ToolbarItem> items) {
		super(id);
		
		RepeatingView links = new RepeatingView("links"); //$NON-NLS-1$
		add(links);
		
		for (final ToolbarItem item : items) {
			AjaxLink link = new AjaxLink(links.newChildId()) {
				private static final long serialVersionUID = 1L;

				@Override
				public void onClick(AjaxRequestTarget target) {
					item.onEvent(target);
				}				
			};
			links.add(link);
			link.add(new Label("label", item.getLabel())); //$NON-NLS-1$
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

		protected abstract void onEvent(AjaxRequestTarget target);
	}

}
