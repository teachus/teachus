package dk.teachus.frontend.functions;

import org.apache.wicket.Component;
import org.apache.wicket.model.IModel;

import dk.teachus.frontend.components.PaidPanel;
import dk.teachus.frontend.components.list.FunctionItem;

public class PaidFunction implements FunctionItem {
	private static final long serialVersionUID = 1L;

	public Component createComponent(String wicketId, IModel rowModel) {
		return new PaidPanel(wicketId, rowModel);
	}
	
}
