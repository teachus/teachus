package dk.teachus.frontend.components.jquery.cluetip;

import org.apache.wicket.Component;
import org.apache.wicket.ResourceReference;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.resources.JavascriptResourceReference;
import org.apache.wicket.model.Model;

import dk.teachus.frontend.components.jquery.JQueryBehavior;

public class JQueryCluetipBehavior extends JQueryBehavior {
	private static final long serialVersionUID = 1L;
	
	public static final ResourceReference JS_CLUETIP_JQUERY = new JavascriptResourceReference(JQueryCluetipBehavior.class, "jquery.cluetip-1.1pre.min.js"); //$NON-NLS-1$

	public static enum Style {
		STANDARD,
		NO_HEADER
	}
	
	private final Style style;
	
	public JQueryCluetipBehavior() {
		this(null);
	}
	
	public JQueryCluetipBehavior(Style style) {
		if (style == null) {
			style = Style.STANDARD;
		}
		
		this.style = style;		
	}
	
	@Override
	public void onRenderHead(IHeaderResponse response) {
		super.onRenderHead(response);
		
		response.renderJavascriptReference(JS_CLUETIP_JQUERY);
		
		StringBuilder tipConf = new StringBuilder();
		tipConf.append("$('.tooltip").append(style.name()).append("').cluetip({");
		if (style == Style.STANDARD) {
			tipConf.append("splitTitle: '|'");
		} else {
			tipConf.append("splitTitle: '|',");
			tipConf.append("showTitle: false");
		}
		tipConf.append("})");
		
		response.renderOnDomReadyJavascript(tipConf.toString());
	}
	
	@Override
	public void bind(Component component) {
		component.add(new AttributeAppender("class", true, new Model<String>("tooltip"+style.name()), " "));
	}
	
}
