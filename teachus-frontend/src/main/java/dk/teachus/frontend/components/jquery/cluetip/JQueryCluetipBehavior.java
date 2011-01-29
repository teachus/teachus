package dk.teachus.frontend.components.jquery.cluetip;

import java.util.HashMap;
import java.util.Map;

import org.apache.wicket.Component;
import org.apache.wicket.RequestCycle;
import org.apache.wicket.ResourceReference;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.resources.JavascriptResourceReference;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.util.template.TextTemplateHeaderContributor;

import dk.teachus.frontend.components.jquery.JQueryBehavior;

public class JQueryCluetipBehavior extends JQueryBehavior {
	private static final long serialVersionUID = 1L;
	
	public static final ResourceReference JS_CLUETIP_JQUERY = new JavascriptResourceReference(JQueryCluetipBehavior.class, "jquery.cluetip-1.1pre.min.js"); //$NON-NLS-1$
	public static final ResourceReference IMG_CLUETIP_ARROWDOWN_GIF = new ResourceReference(JQueryCluetipBehavior.class, "arrowdown.gif"); //$NON-NLS-1$
	public static final ResourceReference IMG_CLUETIP_ARROWLEFT_GIF = new ResourceReference(JQueryCluetipBehavior.class, "arrowleft.gif"); //$NON-NLS-1$
	public static final ResourceReference IMG_CLUETIP_ARROWRIGHT_GIF = new ResourceReference(JQueryCluetipBehavior.class, "arrowright.gif"); //$NON-NLS-1$
	public static final ResourceReference IMG_CLUETIP_ARROWUP_GIF = new ResourceReference(JQueryCluetipBehavior.class, "arrowup.gif"); //$NON-NLS-1$
	public static final ResourceReference IMG_CLUETIP_DARROWDOWN_GIF = new ResourceReference(JQueryCluetipBehavior.class, "darrowdown.gif"); //$NON-NLS-1$
	public static final ResourceReference IMG_CLUETIP_DARROWLEFT_GIF = new ResourceReference(JQueryCluetipBehavior.class, "darrowleft.gif"); //$NON-NLS-1$
	public static final ResourceReference IMG_CLUETIP_DARROWRIGHT_GIF = new ResourceReference(JQueryCluetipBehavior.class, "darrowright.gif"); //$NON-NLS-1$
	public static final ResourceReference IMG_CLUETIP_DARROWUP_GIF = new ResourceReference(JQueryCluetipBehavior.class, "darrowup.gif"); //$NON-NLS-1$
	public static final ResourceReference IMG_CLUETIP_WAIT_GIF = new ResourceReference(JQueryCluetipBehavior.class, "wait.gif"); //$NON-NLS-1$
	public static final TextTemplateHeaderContributor CSS_CLUETIP_JQUERY = TextTemplateHeaderContributor.forCss(JQueryCluetipBehavior.class, "jquery.cluetip-1.1pre.css", new StyleSheetModel()); //$NON-NLS-1$

	private static class StyleSheetModel extends AbstractReadOnlyModel<Map<String, Object>> {
		private static final long serialVersionUID = 1L;

		@Override
		public Map<String, Object> getObject() {
			RequestCycle requestCycle = RequestCycle.get();
			
			Map<String, Object> variables = new HashMap<String, Object>();
			variables.put("arrowdown.gif", requestCycle.urlFor(IMG_CLUETIP_ARROWDOWN_GIF));
			variables.put("arrowleft.gif", requestCycle.urlFor(IMG_CLUETIP_ARROWLEFT_GIF));
			variables.put("arrowright.gif", requestCycle.urlFor(IMG_CLUETIP_ARROWRIGHT_GIF));
			variables.put("arrowup.gif", requestCycle.urlFor(IMG_CLUETIP_ARROWUP_GIF));
			variables.put("darrowdown.gif", requestCycle.urlFor(IMG_CLUETIP_DARROWDOWN_GIF));
			variables.put("darrowleft.gif", requestCycle.urlFor(IMG_CLUETIP_DARROWLEFT_GIF));
			variables.put("darrowright.gif", requestCycle.urlFor(IMG_CLUETIP_DARROWRIGHT_GIF));
			variables.put("darrowup.gif", requestCycle.urlFor(IMG_CLUETIP_DARROWUP_GIF));
			variables.put("wait.gif", requestCycle.urlFor(IMG_CLUETIP_WAIT_GIF));
			
			return variables;
		}
		
	}

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
		component.add(JQueryCluetipBehavior.CSS_CLUETIP_JQUERY);
	}
	
}
