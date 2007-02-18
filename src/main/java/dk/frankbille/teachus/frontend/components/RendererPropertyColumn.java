package dk.frankbille.teachus.frontend.components;

import wicket.extensions.markup.html.repeater.data.table.AbstractColumn;
import wicket.markup.html.form.IChoiceRenderer;
import wicket.markup.repeater.Item;
import wicket.model.IModel;
import wicket.model.PropertyModel;

public class RendererPropertyColumn extends AbstractColumn {
	private static final long serialVersionUID = 1L;
	
	private IChoiceRenderer renderer;
	private String propertyExpressions;

	public RendererPropertyColumn(IModel displayModel, String propertyExpressions, IChoiceRenderer renderer) {
		super(displayModel);
		this.renderer = renderer;
		this.propertyExpressions = propertyExpressions;
	}

	public void populateItem(Item cellItem, String componentId, IModel rowModel) {
		cellItem.add(new RenderingLabel(componentId, new PropertyModel(rowModel, propertyExpressions), renderer));
	}
}
