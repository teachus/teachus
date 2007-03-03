package dk.teachus.frontend.pages;

import java.util.List;

import dk.teachus.frontend.TeachUsSession;

import wicket.Application;
import wicket.behavior.SimpleAttributeModifier;
import wicket.markup.html.WebComponent;
import wicket.markup.html.basic.Label;
import wicket.markup.html.link.BookmarkablePageLink;
import wicket.markup.html.link.Link;
import wicket.request.target.component.BookmarkablePageRequestTarget;

public class SignedOutPage extends UnAuthenticatedBasePage {
	private static final long serialVersionUID = 1L;

	public SignedOutPage() {
		WebComponent refresh = new WebComponent("refresh"); //$NON-NLS-1$
		StringBuilder content = new StringBuilder();
		content.append("1; url="); //$NON-NLS-1$
		content.append(getRequestCycle().urlFor(new BookmarkablePageRequestTarget(Application.get().getHomePage())));
		refresh.add(new SimpleAttributeModifier("content", content)); //$NON-NLS-1$
		add(refresh);
		
		add(new Label("signedOutText", TeachUsSession.get().getString("SignedOutPage.youAreNowLoggedOutOfTheSystem"))); //$NON-NLS-1$ //$NON-NLS-2$
		
		Link homePageLink= new BookmarkablePageLink("homePageLink", Application.get().getHomePage()); //$NON-NLS-1$
		add(homePageLink);
		homePageLink.add(new Label("homePageLabel", TeachUsSession.get().getString("SignedOutPage.clickToGoToFrontPage"))); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Override
	protected List<MenuItem> createMenuItems() {
		return null;
	}
	
	protected UnAuthenticatedPageCategory getPageCategory() {
		return null;
	}

	protected String getPageLabel() {
		return null;
	}
	
}
