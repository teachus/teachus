package dk.teachus.frontend.components;

import wicket.extensions.markup.html.repeater.data.table.AbstractColumn;
import wicket.markup.html.basic.Label;
import wicket.markup.html.link.Link;
import wicket.markup.repeater.Item;
import wicket.model.IModel;
import wicket.model.PropertyModel;

public abstract class LinkPropertyColumn extends AbstractColumn {
	private String propertyExpression;
	
	public LinkPropertyColumn(IModel displayModel, String propertyExpression) {
		super(displayModel);
		
		this.propertyExpression = propertyExpression;
	}

	public void populateItem(final Item cellItem, String componentId, final IModel rowModel) {
		LinkPropertyColumnPanel linkPropertyColumnPanel = new LinkPropertyColumnPanel(componentId);
		linkPropertyColumnPanel.setRenderBodyOnly(true);
		Link link = new Link("link") {
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick() {
				LinkPropertyColumn.this.onClick(rowModel.getObject(cellItem));
			}			
		};
		Label label = new Label("label", new PropertyModel(rowModel, propertyExpression));
		label.setRenderBodyOnly(true);
		link.add(label);
		linkPropertyColumnPanel.add(link);
		cellItem.add(linkPropertyColumnPanel);
	}

	protected abstract void onClick(Object rowModelObject);
}
