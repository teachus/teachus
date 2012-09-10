package dk.teachus.frontend.components.jquery.cluetip;

import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.request.resource.JavaScriptResourceReference;
import org.apache.wicket.request.resource.ResourceReference;
import org.apache.wicket.request.resource.SharedResourceReference;

import dk.teachus.frontend.components.jquery.JQueryBehavior;

public class AbstractJQueryCluetipBehavior extends JQueryBehavior {
	private static final long serialVersionUID = 1L;
	
	public static final ResourceReference JS_CLUETIP_JQUERY = new JavaScriptResourceReference(AbstractJQueryCluetipBehavior.class, "jquery.cluetip-1.1pre.min.js"); //$NON-NLS-1$
	public static final ResourceReference IMG_CLUETIP_ARROWDOWN_GIF = new SharedResourceReference(AbstractJQueryCluetipBehavior.class, "arrowdown.gif"); //$NON-NLS-1$
	public static final ResourceReference IMG_CLUETIP_ARROWLEFT_GIF = new SharedResourceReference(AbstractJQueryCluetipBehavior.class, "arrowleft.gif"); //$NON-NLS-1$
	public static final ResourceReference IMG_CLUETIP_ARROWRIGHT_GIF = new SharedResourceReference(AbstractJQueryCluetipBehavior.class, "arrowright.gif"); //$NON-NLS-1$
	public static final ResourceReference IMG_CLUETIP_ARROWUP_GIF = new SharedResourceReference(AbstractJQueryCluetipBehavior.class, "arrowup.gif"); //$NON-NLS-1$
	public static final ResourceReference IMG_CLUETIP_DARROWDOWN_GIF = new SharedResourceReference(AbstractJQueryCluetipBehavior.class, "darrowdown.gif"); //$NON-NLS-1$
	public static final ResourceReference IMG_CLUETIP_DARROWLEFT_GIF = new SharedResourceReference(AbstractJQueryCluetipBehavior.class, "darrowleft.gif"); //$NON-NLS-1$
	public static final ResourceReference IMG_CLUETIP_DARROWRIGHT_GIF = new SharedResourceReference(AbstractJQueryCluetipBehavior.class, "darrowright.gif"); //$NON-NLS-1$
	public static final ResourceReference IMG_CLUETIP_DARROWUP_GIF = new SharedResourceReference(AbstractJQueryCluetipBehavior.class, "darrowup.gif"); //$NON-NLS-1$
	public static final ResourceReference IMG_CLUETIP_WAIT_GIF = new SharedResourceReference(AbstractJQueryCluetipBehavior.class, "wait.gif"); //$NON-NLS-1$
	public static final ResourceReference CSS_CLUETIP_JQUERY = new SharedResourceReference(AbstractJQueryCluetipBehavior.class, "jquery.cluetip-1.1pre.css"); //$NON-NLS-1$
	
	public AbstractJQueryCluetipBehavior() {
	}
	
	@Override
	public void onRenderHead(IHeaderResponse response) {
		super.onRenderHead(response);
		
		response.renderJavaScriptReference(JS_CLUETIP_JQUERY);		
		response.renderCSSReference(CSS_CLUETIP_JQUERY);
	}
	
}
