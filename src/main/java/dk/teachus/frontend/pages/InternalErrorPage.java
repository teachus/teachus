package dk.teachus.frontend.pages;

import wicket.markup.html.basic.Label;
import wicket.markup.html.basic.MultiLineLabel;
import wicket.markup.html.link.BookmarkablePageLink;
import wicket.markup.html.link.Link;
import dk.teachus.frontend.TeachUsApplication;
import dk.teachus.frontend.TeachUsSession;

public class InternalErrorPage extends SystemBasePage {
	private static final long serialVersionUID = 1L;

	public InternalErrorPage() {
		add(new Label("header", TeachUsSession.get().getString("InternalErrorPage.header")));
		
		add(new MultiLineLabel("errorDescription", TeachUsSession.get().getString("InternalErrorPage.discription")));
		
		Link link = new BookmarkablePageLink("link", TeachUsApplication.get().getHomePage());
		add(link);
		link.add(new Label("label", TeachUsSession.get().getString("InternalErrorPage.homePageLink")));
	}
	
}
