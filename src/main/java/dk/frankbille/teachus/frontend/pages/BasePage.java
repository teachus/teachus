package dk.frankbille.teachus.frontend.pages;

import wicket.ResourceReference;
import wicket.behavior.HeaderContributor;
import wicket.markup.html.WebPage;
import wicket.markup.html.resources.CompressedResourceReference;
import wicket.markup.html.resources.JavascriptResourceReference;

public abstract class BasePage extends WebPage {
	private static final ResourceReference CSS_MAIN = new CompressedResourceReference(BasePage.class, "resources/main.css"); //$NON-NLS-1$
	private static final ResourceReference JS_PROTOTYPE = new JavascriptResourceReference(BasePage.class, "resources/prototype.js"); //$NON-NLS-1$
	
	public BasePage() {
		add(HeaderContributor.forCss(CSS_MAIN));
		add(HeaderContributor.forJavaScript(JS_PROTOTYPE));
	}
	
}
