package dk.teachus.frontend.components.list;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.WebComponent;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.AbstractReadOnlyModel;

import dk.teachus.frontend.components.ConfirmClickBehavior;

abstract class IconFunctionItemPanel extends Panel {
	private static final long serialVersionUID = 1L;

	public IconFunctionItemPanel(final String id, final String title, final String icon, final String buttonStyle) {
		super(id);
		
		Link<Void> link = new Link<Void>("link") {
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick() {
				IconFunctionItemPanel.this.onEvent();
			}
		};
		link.add(new ConfirmClickBehavior(getClickConfirmText()));
		link.add(AttributeModifier.replace("title", title));
		link.add(AttributeModifier.append("class", new AbstractReadOnlyModel<String>() {
			private static final long serialVersionUID = 1L;

			@Override
			public String getObject() {
				return buttonStyle != null ? "btn-"+buttonStyle : null;
			}
		}));
		add(link);
		
		WebComponent iconComponent = new WebComponent("icon");
		iconComponent.add(AttributeModifier.replace("class", "icon-"+icon));
		link.add(iconComponent);
	}
	
	protected abstract void onEvent();

	protected abstract String getClickConfirmText();
	
}
