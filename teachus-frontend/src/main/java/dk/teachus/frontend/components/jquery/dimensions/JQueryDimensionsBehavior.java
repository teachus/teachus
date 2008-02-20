package dk.teachus.frontend.components.jquery.dimensions;

import org.apache.wicket.ResourceReference;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.resources.JavascriptResourceReference;

import dk.teachus.frontend.components.jquery.JQueryBehavior;

public class JQueryDimensionsBehavior extends JQueryBehavior {
	private static final long serialVersionUID = 1L;
	
	public static final ResourceReference JS_DIM_JQUERY = new JavascriptResourceReference(JQueryDimensionsBehavior.class, "jquery.dimensions-1.2.0.min.js"); //$NON-NLS-1$
	
	@Override
	public void onRenderHead(IHeaderResponse response) {
		response.renderJavascriptReference(JS_DIM_JQUERY);
	}
	
}
