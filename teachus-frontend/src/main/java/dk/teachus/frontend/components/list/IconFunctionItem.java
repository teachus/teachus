package dk.teachus.frontend.components.list;

import org.apache.wicket.Component;
import org.apache.wicket.model.IModel;

public abstract class IconFunctionItem<T> implements FunctionItem<T> {
	private static final long serialVersionUID = 1L;

	private final String title;
	private final String icon;
	private final String buttonStyle;
	
	public IconFunctionItem(final String title, final String icon) {
		this(title, icon, null);
	}
	
	public IconFunctionItem(final String title, final String icon, final String buttonStyle) {
		this.title = title;
		this.icon = icon;
		this.buttonStyle = buttonStyle;
	}

	@Override
	public Component createComponent(final String wicketId, final IModel<T> rowModel) {
		return new IconFunctionItemPanel(wicketId, title, icon, buttonStyle) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void onEvent() {
				IconFunctionItem.this.onEvent(rowModel.getObject());
			}
			
			@Override
			protected String getClickConfirmText() {
				return IconFunctionItem.this.getClickConfirmText(rowModel.getObject());
			}
		};
	}

	public String getClickConfirmText(T object) {
		return null;
	}
	
	protected abstract void onEvent(T object);
	
}