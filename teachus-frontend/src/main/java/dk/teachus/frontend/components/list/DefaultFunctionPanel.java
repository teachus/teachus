package dk.teachus.frontend.components.list;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;

import dk.teachus.frontend.components.ConfirmClickBehavior;

public class DefaultFunctionPanel<T> extends Panel {
	private static final long serialVersionUID = 1L;

	public DefaultFunctionPanel(String id, final DefaultFunctionItem<T> functionItem, final IModel<T> rowModel) {
		super(id);
		
		Link<T> link = new Link<T>("link", rowModel) {
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick() {
				functionItem.onEvent(getModelObject());
			}
			
			@Override
			public boolean isEnabled() {
				return functionItem.isEnabled(getModelObject());
			}
		};
		
		String clickConfirmText = functionItem.getClickConfirmText(rowModel.getObject());
		link.add(new ConfirmClickBehavior(clickConfirmText));
		link.add(new AttributeModifier("title", true, new AbstractReadOnlyModel<String>() {
			private static final long serialVersionUID = 1L;

			@Override
			public String getObject() {
				return functionItem.getTitle(rowModel.getObject());
			}
		}) {
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isEnabled(Component component) {
				return functionItem.isEnabled(rowModel.getObject());
			}
		});
		functionItem.modifyLink(link);

		link.add(functionItem.createLabelComponent("label", rowModel.getObject()));
		
		add(link);
	}
	
}
