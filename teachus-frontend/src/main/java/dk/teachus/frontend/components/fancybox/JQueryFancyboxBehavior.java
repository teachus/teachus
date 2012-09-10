package dk.teachus.frontend.components.fancybox;

import java.util.HashMap;
import java.util.Map;

import org.apache.wicket.Component;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.resource.JavaScriptResourceReference;
import org.apache.wicket.request.resource.ResourceReference;
import org.apache.wicket.request.resource.SharedResourceReference;
import org.apache.wicket.util.template.CssTemplate;
import org.apache.wicket.util.template.PackageTextTemplate;
import org.apache.wicket.util.template.TextTemplate;

import dk.teachus.frontend.components.jquery.JQueryBehavior;

public class JQueryFancyboxBehavior extends JQueryBehavior {
	private static final long serialVersionUID = 1L;
	
	public static final ResourceReference JS_FANCYBOX = new JavaScriptResourceReference(JQueryFancyboxBehavior.class, "jquery.fancybox-1.3.4.js"); //$NON-NLS-1$
	public static final ResourceReference IMG_FANCYBOX_FANCYBOX_PNG = new SharedResourceReference(JQueryFancyboxBehavior.class, "fancybox.png"); //$NON-NLS-1$;
	public static final ResourceReference IMG_FANCYBOX_BLANK_GIF = new SharedResourceReference(JQueryFancyboxBehavior.class, "blank.gif"); //$NON-NLS-1$;
	public static final ResourceReference IMG_FANCYBOX_FANCYBOX_X_PNG = new SharedResourceReference(JQueryFancyboxBehavior.class, "fancybox-x.png"); //$NON-NLS-1$;
	public static final ResourceReference IMG_FANCYBOX_FANCYBOX_Y_PNG = new SharedResourceReference(JQueryFancyboxBehavior.class, "fancybox-y.png"); //$NON-NLS-1$;
	public static final ResourceReference IMG_FANCYBOX_FANCY_TITLE_MAIN_PNG = new SharedResourceReference(JQueryFancyboxBehavior.class, "fancy_title_main.png"); //$NON-NLS-1$;
	public static final ResourceReference IMG_FANCYBOX_FANCY_TITLE_OVER_PNG = new SharedResourceReference(JQueryFancyboxBehavior.class, "fancy_title_over.png"); //$NON-NLS-1$;
	public static final ResourceReference IMG_FANCYBOX_FANCY_TITLE_LEFT_PNG = new SharedResourceReference(JQueryFancyboxBehavior.class, "fancy_title_left.png"); //$NON-NLS-1$;
	public static final ResourceReference IMG_FANCYBOX_FANCY_TITLE_RIGHT_PNG = new SharedResourceReference(JQueryFancyboxBehavior.class, "fancy_title_right.png"); //$NON-NLS-1$;
	public static final ResourceReference IMG_FANCYBOX_FANCY_CLOSE_PNG = new SharedResourceReference(JQueryFancyboxBehavior.class, "fancy_close.png"); //$NON-NLS-1$;
	public static final ResourceReference IMG_FANCYBOX_FANCY_LOADING_PNG = new SharedResourceReference(JQueryFancyboxBehavior.class, "fancy_loading.png"); //$NON-NLS-1$;
	public static final ResourceReference IMG_FANCYBOX_FANCY_NAV_LEFT_PNG = new SharedResourceReference(JQueryFancyboxBehavior.class, "fancy_nav_left.png"); //$NON-NLS-1$;
	public static final ResourceReference IMG_FANCYBOX_FANCY_NAV_RIGHT_PNG = new SharedResourceReference(JQueryFancyboxBehavior.class, "fancy_nav_right.png"); //$NON-NLS-1$;
	public static final ResourceReference IMG_FANCYBOX_FANCY_SHADOW_E_PNG = new SharedResourceReference(JQueryFancyboxBehavior.class, "fancy_shadow_e.png"); //$NON-NLS-1$;
	public static final ResourceReference IMG_FANCYBOX_FANCY_SHADOW_N_PNG = new SharedResourceReference(JQueryFancyboxBehavior.class, "fancy_shadow_n.png"); //$NON-NLS-1$;
	public static final ResourceReference IMG_FANCYBOX_FANCY_SHADOW_NE_PNG = new SharedResourceReference(JQueryFancyboxBehavior.class, "fancy_shadow_ne.png"); //$NON-NLS-1$;
	public static final ResourceReference IMG_FANCYBOX_FANCY_SHADOW_NW_PNG = new SharedResourceReference(JQueryFancyboxBehavior.class, "fancy_shadow_nw.png"); //$NON-NLS-1$;
	public static final ResourceReference IMG_FANCYBOX_FANCY_SHADOW_S_PNG = new SharedResourceReference(JQueryFancyboxBehavior.class, "fancy_shadow_s.png"); //$NON-NLS-1$;
	public static final ResourceReference IMG_FANCYBOX_FANCY_SHADOW_SE_PNG = new SharedResourceReference(JQueryFancyboxBehavior.class, "fancy_shadow_se.png"); //$NON-NLS-1$;
	public static final ResourceReference IMG_FANCYBOX_FANCY_SHADOW_SW_PNG = new SharedResourceReference(JQueryFancyboxBehavior.class, "fancy_shadow_sw.png"); //$NON-NLS-1$;
	public static final ResourceReference IMG_FANCYBOX_FANCY_SHADOW_W_PNG = new SharedResourceReference(JQueryFancyboxBehavior.class, "fancy_shadow_w.png"); //$NON-NLS-1$;
	public static final Behavior CSS_FANCYBOX = new Behavior() {
		private static final long serialVersionUID = 1L;
		
		private TextTemplate template = new CssTemplate(new PackageTextTemplate(JQueryFancyboxBehavior.class, "jquery.fancybox-1.3.4.css"));
		
		public void renderHead(Component component, IHeaderResponse response) {
			RequestCycle requestCycle = RequestCycle.get();
			
			Map<String, Object> variables = new HashMap<String, Object>();
			variables.put("fancybox.png", requestCycle.urlFor(IMG_FANCYBOX_FANCYBOX_PNG, null));
			variables.put("blank.gif", requestCycle.urlFor(IMG_FANCYBOX_BLANK_GIF, null));
			variables.put("fancybox-x.png", requestCycle.urlFor(IMG_FANCYBOX_FANCYBOX_X_PNG, null));
			variables.put("fancybox-y.png", requestCycle.urlFor(IMG_FANCYBOX_FANCYBOX_Y_PNG, null));
			variables.put("fancy_title_main.png", requestCycle.urlFor(IMG_FANCYBOX_FANCY_TITLE_MAIN_PNG, null));
			variables.put("fancy_title_over.png", requestCycle.urlFor(IMG_FANCYBOX_FANCY_TITLE_OVER_PNG, null));
			variables.put("fancy_title_left.png", requestCycle.urlFor(IMG_FANCYBOX_FANCY_TITLE_LEFT_PNG, null));
			variables.put("fancy_title_right.png", requestCycle.urlFor(IMG_FANCYBOX_FANCY_TITLE_RIGHT_PNG, null));
			
			variables.put("fancy_close.png", requestCycle.urlFor(IMG_FANCYBOX_FANCY_CLOSE_PNG, null));
			variables.put("fancy_loading.png", requestCycle.urlFor(IMG_FANCYBOX_FANCY_LOADING_PNG, null));
			variables.put("fancy_nav_left.png", requestCycle.urlFor(IMG_FANCYBOX_FANCY_NAV_LEFT_PNG, null));
			variables.put("fancy_nav_right.png", requestCycle.urlFor(IMG_FANCYBOX_FANCY_NAV_RIGHT_PNG, null));
			variables.put("fancy_shadow_e.png", requestCycle.urlFor(IMG_FANCYBOX_FANCY_SHADOW_E_PNG, null));
			variables.put("fancy_shadow_n.png", requestCycle.urlFor(IMG_FANCYBOX_FANCY_SHADOW_N_PNG, null));
			variables.put("fancy_shadow_ne.png", requestCycle.urlFor(IMG_FANCYBOX_FANCY_SHADOW_NE_PNG, null));
			variables.put("fancy_shadow_nw.png", requestCycle.urlFor(IMG_FANCYBOX_FANCY_SHADOW_NW_PNG, null));
			variables.put("fancy_shadow_s.png", requestCycle.urlFor(IMG_FANCYBOX_FANCY_SHADOW_S_PNG, null));
			variables.put("fancy_shadow_se.png", requestCycle.urlFor(IMG_FANCYBOX_FANCY_SHADOW_SE_PNG, null));
			variables.put("fancy_shadow_sw.png", requestCycle.urlFor(IMG_FANCYBOX_FANCY_SHADOW_SW_PNG, null));
			variables.put("fancy_shadow_w.png", requestCycle.urlFor(IMG_FANCYBOX_FANCY_SHADOW_W_PNG, null));
			
			response.renderString(template.asString(variables));
		}
		
	};
	
	public JQueryFancyboxBehavior() {
	}
	
	@Override
	public void onRenderHead(IHeaderResponse response) {
		response.renderJavaScriptReference(JS_FANCYBOX);
	}
	
}
