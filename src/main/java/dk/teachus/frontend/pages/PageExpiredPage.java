package dk.teachus.frontend.pages;

import wicket.markup.html.basic.Label;
import wicket.markup.html.basic.MultiLineLabel;
import wicket.markup.html.link.BookmarkablePageLink;
import wicket.markup.html.link.Link;
import dk.teachus.frontend.TeachUsApplication;
import dk.teachus.frontend.TeachUsSession;

public class PageExpiredPage extends SystemBasePage {
	private static final long serialVersionUID = 1L;

	public PageExpiredPage() {
		add(new Label("pageExpiredHeader", TeachUsSession.get().getString("PageExpiredPage.header")));
		
		add(new MultiLineLabel("pageExpired", TeachUsSession.get().getString("PageExpiredPage.discription")));
		
		Link link = new BookmarkablePageLink("link", TeachUsApplication.get().getHomePage());
		add(link);
		link.add(new Label("label", TeachUsSession.get().getString("PageExpiredPage.homePageLink")));
	}
	
}
