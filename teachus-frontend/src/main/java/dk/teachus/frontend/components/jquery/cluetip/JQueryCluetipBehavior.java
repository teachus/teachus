package dk.teachus.frontend.components.jquery.cluetip;

import org.apache.wicket.ResourceReference;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.resources.JavascriptResourceReference;

import dk.teachus.frontend.components.jquery.dimensions.JQueryDimensionsBehavior;

public class JQueryCluetipBehavior extends JQueryDimensionsBehavior {
	private static final long serialVersionUID = 1L;
	
	public static final ResourceReference JS_CLUETIP_JQUERY = new JavascriptResourceReference(JQueryCluetipBehavior.class, "jquery.cluetip-0.9.6.min.js"); //$NON-NLS-1$
	
	@Override
	public void onRenderHead(IHeaderResponse response) {
		super.onRenderHead(response);
		
		response.renderJavascriptReference(JS_CLUETIP_JQUERY);
		
		StringBuilder tipConf = new StringBuilder();
		tipConf.append("$('a.tooltip').cluetip({splitTitle: '|'})");
		
		response.renderOnDomReadyJavascript(tipConf.toString());
	}
	
}
