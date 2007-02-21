package dk.frankbille.teachus.frontend.pages;

import dk.frankbille.teachus.frontend.TeachUsApplication;
import dk.frankbille.teachus.frontend.TeachUsSession;
import dk.frankbille.teachus.frontend.utils.Resources;
import wicket.behavior.HeaderContributor;
import wicket.markup.html.WebPage;
import wicket.markup.html.basic.Label;

public abstract class BasePage extends WebPage {
	public BasePage() {
		add(HeaderContributor.forCss(Resources.CSS_MAIN));
		add(HeaderContributor.forJavaScript(Resources.JS_PROTOTYPE));

		StringBuilder sb = new StringBuilder(TeachUsSession.get().getString("General.teachUsTitle")); //$NON-NLS-1$
		sb.append(" "); //$NON-NLS-1$
		sb.append(TeachUsApplication.get().getVersion());
		add(new Label("title", sb.toString())); //$NON-NLS-1$
	}
}
