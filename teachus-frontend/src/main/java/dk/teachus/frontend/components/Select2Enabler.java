package dk.teachus.frontend.components;

import org.apache.wicket.Component;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.markup.html.IHeaderResponse;

public class Select2Enabler extends Behavior {
	private static final long serialVersionUID = 1L;

	public void bind(Component component) {
		component.setOutputMarkupId(true);
	}

	@Override
	public void renderHead(Component component, IHeaderResponse response) {
		super.renderHead(component, response);

		response.renderOnDomReadyJavaScript("$(\"#"+component.getMarkupId()+"\").select2({allowClear:true});");
	}
}
