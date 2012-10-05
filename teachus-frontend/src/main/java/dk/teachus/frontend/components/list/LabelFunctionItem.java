package dk.teachus.frontend.components.list;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.basic.Label;

public abstract class LabelFunctionItem<T> extends DefaultFunctionItem<T> {
	private static final long serialVersionUID = 1L;
	
	private String label; 
	
	public LabelFunctionItem() {
	}
	
	public LabelFunctionItem(String label) {
		this.label = label;
	}

	@Override
	public Component createLabelComponent(String wicketId, T object) {
		return new Label(wicketId, getLabel(object)).setRenderBodyOnly(true);
	}
	
	public String getLabel(T object) {
		return label;
	}
	
}
