package dk.teachus.frontend.components.fancybox;

import java.util.HashMap;
import java.util.Map;

import org.apache.wicket.RequestCycle;
import org.apache.wicket.ResourceReference;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.resources.JavascriptResourceReference;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.util.template.TextTemplateHeaderContributor;

import dk.teachus.frontend.components.jquery.JQueryBehavior;

public class JQueryFancyboxBehavior extends JQueryBehavior {
	private static final long serialVersionUID = 1L;
	
	public static final ResourceReference JS_FANCYBOX = new JavascriptResourceReference(JQueryFancyboxBehavior.class, "jquery.fancybox-1.3.4.js"); //$NON-NLS-1$
	public static final ResourceReference IMG_FANCYBOX_FANCYBOX_PNG = new ResourceReference(JQueryFancyboxBehavior.class, "fancybox.png"); //$NON-NLS-1$;
	public static final ResourceReference IMG_FANCYBOX_BLANK_GIF = new ResourceReference(JQueryFancyboxBehavior.class, "blank.gif"); //$NON-NLS-1$;
	public static final ResourceReference IMG_FANCYBOX_FANCYBOX_X_PNG = new ResourceReference(JQueryFancyboxBehavior.class, "fancybox-x.png"); //$NON-NLS-1$;
	public static final ResourceReference IMG_FANCYBOX_FANCYBOX_Y_PNG = new ResourceReference(JQueryFancyboxBehavior.class, "fancybox-y.png"); //$NON-NLS-1$;
	public static final ResourceReference IMG_FANCYBOX_FANCY_TITLE_MAIN_PNG = new ResourceReference(JQueryFancyboxBehavior.class, "fancy_title_main.png"); //$NON-NLS-1$;
	public static final ResourceReference IMG_FANCYBOX_FANCY_TITLE_OVER_PNG = new ResourceReference(JQueryFancyboxBehavior.class, "fancy_title_over.png"); //$NON-NLS-1$;
	public static final ResourceReference IMG_FANCYBOX_FANCY_TITLE_LEFT_PNG = new ResourceReference(JQueryFancyboxBehavior.class, "fancy_title_left.png"); //$NON-NLS-1$;
	public static final ResourceReference IMG_FANCYBOX_FANCY_TITLE_RIGHT_PNG = new ResourceReference(JQueryFancyboxBehavior.class, "fancy_title_right.png"); //$NON-NLS-1$;
	public static final ResourceReference IMG_FANCYBOX_FANCY_CLOSE_PNG = new ResourceReference(JQueryFancyboxBehavior.class, "fancy_close.png"); //$NON-NLS-1$;
	public static final ResourceReference IMG_FANCYBOX_FANCY_LOADING_PNG = new ResourceReference(JQueryFancyboxBehavior.class, "fancy_loading.png"); //$NON-NLS-1$;
	public static final ResourceReference IMG_FANCYBOX_FANCY_NAV_LEFT_PNG = new ResourceReference(JQueryFancyboxBehavior.class, "fancy_nav_left.png"); //$NON-NLS-1$;
	public static final ResourceReference IMG_FANCYBOX_FANCY_NAV_RIGHT_PNG = new ResourceReference(JQueryFancyboxBehavior.class, "fancy_nav_right.png"); //$NON-NLS-1$;
	public static final ResourceReference IMG_FANCYBOX_FANCY_SHADOW_E_PNG = new ResourceReference(JQueryFancyboxBehavior.class, "fancy_shadow_e.png"); //$NON-NLS-1$;
	public static final ResourceReference IMG_FANCYBOX_FANCY_SHADOW_N_PNG = new ResourceReference(JQueryFancyboxBehavior.class, "fancy_shadow_n.png"); //$NON-NLS-1$;
	public static final ResourceReference IMG_FANCYBOX_FANCY_SHADOW_NE_PNG = new ResourceReference(JQueryFancyboxBehavior.class, "fancy_shadow_ne.png"); //$NON-NLS-1$;
	public static final ResourceReference IMG_FANCYBOX_FANCY_SHADOW_NW_PNG = new ResourceReference(JQueryFancyboxBehavior.class, "fancy_shadow_nw.png"); //$NON-NLS-1$;
	public static final ResourceReference IMG_FANCYBOX_FANCY_SHADOW_S_PNG = new ResourceReference(JQueryFancyboxBehavior.class, "fancy_shadow_s.png"); //$NON-NLS-1$;
	public static final ResourceReference IMG_FANCYBOX_FANCY_SHADOW_SE_PNG = new ResourceReference(JQueryFancyboxBehavior.class, "fancy_shadow_se.png"); //$NON-NLS-1$;
	public static final ResourceReference IMG_FANCYBOX_FANCY_SHADOW_SW_PNG = new ResourceReference(JQueryFancyboxBehavior.class, "fancy_shadow_sw.png"); //$NON-NLS-1$;
	public static final ResourceReference IMG_FANCYBOX_FANCY_SHADOW_W_PNG = new ResourceReference(JQueryFancyboxBehavior.class, "fancy_shadow_w.png"); //$NON-NLS-1$;
	public static final TextTemplateHeaderContributor CSS_FANCYBOX = TextTemplateHeaderContributor.forCss(JQueryFancyboxBehavior.class, "jquery.fancybox-1.3.4.css", new StyleSheetModel()); //$NON-NLS-1$

	private static class StyleSheetModel extends AbstractReadOnlyModel<Map<String, Object>> {
		private static final long serialVersionUID = 1L;

		@Override
		public Map<String, Object> getObject() {
			RequestCycle requestCycle = RequestCycle.get();
			
			Map<String, Object> variables = new HashMap<String, Object>();
			variables.put("fancybox.png", requestCycle.urlFor(IMG_FANCYBOX_FANCYBOX_PNG));
			variables.put("blank.gif", requestCycle.urlFor(IMG_FANCYBOX_BLANK_GIF));
			variables.put("fancybox-x.png", requestCycle.urlFor(IMG_FANCYBOX_FANCYBOX_X_PNG));
			variables.put("fancybox-y.png", requestCycle.urlFor(IMG_FANCYBOX_FANCYBOX_Y_PNG));
			variables.put("fancy_title_main.png", requestCycle.urlFor(IMG_FANCYBOX_FANCY_TITLE_MAIN_PNG));
			variables.put("fancy_title_over.png", requestCycle.urlFor(IMG_FANCYBOX_FANCY_TITLE_OVER_PNG));
			variables.put("fancy_title_left.png", requestCycle.urlFor(IMG_FANCYBOX_FANCY_TITLE_LEFT_PNG));
			variables.put("fancy_title_right.png", requestCycle.urlFor(IMG_FANCYBOX_FANCY_TITLE_RIGHT_PNG));
			
			variables.put("fancy_close.png", requestCycle.urlFor(IMG_FANCYBOX_FANCY_CLOSE_PNG));
			variables.put("fancy_loading.png", requestCycle.urlFor(IMG_FANCYBOX_FANCY_LOADING_PNG));
			variables.put("fancy_nav_left.png", requestCycle.urlFor(IMG_FANCYBOX_FANCY_NAV_LEFT_PNG));
			variables.put("fancy_nav_right.png", requestCycle.urlFor(IMG_FANCYBOX_FANCY_NAV_RIGHT_PNG));
			variables.put("fancy_shadow_e.png", requestCycle.urlFor(IMG_FANCYBOX_FANCY_SHADOW_E_PNG));
			variables.put("fancy_shadow_n.png", requestCycle.urlFor(IMG_FANCYBOX_FANCY_SHADOW_N_PNG));
			variables.put("fancy_shadow_ne.png", requestCycle.urlFor(IMG_FANCYBOX_FANCY_SHADOW_NE_PNG));
			variables.put("fancy_shadow_nw.png", requestCycle.urlFor(IMG_FANCYBOX_FANCY_SHADOW_NW_PNG));
			variables.put("fancy_shadow_s.png", requestCycle.urlFor(IMG_FANCYBOX_FANCY_SHADOW_S_PNG));
			variables.put("fancy_shadow_se.png", requestCycle.urlFor(IMG_FANCYBOX_FANCY_SHADOW_SE_PNG));
			variables.put("fancy_shadow_sw.png", requestCycle.urlFor(IMG_FANCYBOX_FANCY_SHADOW_SW_PNG));
			variables.put("fancy_shadow_w.png", requestCycle.urlFor(IMG_FANCYBOX_FANCY_SHADOW_W_PNG));
			
			return variables;
		}
		
	}
	
	public JQueryFancyboxBehavior() {
	}
	
	@Override
	public void onRenderHead(IHeaderResponse response) {
		response.renderJavascriptReference(JS_FANCYBOX);

	}
	
}
