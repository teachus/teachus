package dk.teachus.frontend.components.list;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;

import dk.teachus.frontend.components.ConfirmClickBehavior;

public class DefaultFunctionPanel extends Panel {
	private static final long serialVersionUID = 1L;

	public DefaultFunctionPanel(String id, final DefaultFunctionItem functionItem, IModel rowModel) {
		super(id);
		
		final Object object = rowModel.getObject();
		
		Link link = new Link("link") {
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick() {
				functionItem.onEvent(object);
			}
			
			@Override
			public boolean isEnabled() {
				return functionItem.isEnabled(object);
			}
		};
		
		String clickConfirmText = functionItem.getClickConfirmText(object);
		link.add(new ConfirmClickBehavior(clickConfirmText));
		link.add(new AttributeModifier("title", true, new AbstractReadOnlyModel() {
			private static final long serialVersionUID = 1L;

			@Override
			public Object getObject() {
				return functionItem.getTitle(object);
			}
		}) {
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isEnabled(Component component) {
				return functionItem.isEnabled(object);
			}
		});
		functionItem.modifyLink(link);

		link.add(functionItem.createLabelComponent("label", object));
		
		add(link);
	}
	
}
