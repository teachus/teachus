package dk.teachus.frontend.pages;

import wicket.Application;
import wicket.behavior.SimpleAttributeModifier;
import wicket.markup.html.WebComponent;
import wicket.markup.html.basic.Label;
import wicket.request.target.component.BookmarkablePageRequestTarget;

public class SignedOutPage extends UnAuthenticatedBasePage {
	private static final long serialVersionUID = 1L;

	public SignedOutPage() {
		WebComponent refresh = new WebComponent("refresh");
		StringBuilder content = new StringBuilder();
		content.append("1; url=");
		content.append(getRequestCycle().urlFor(new BookmarkablePageRequestTarget(Application.get().getHomePage())));
		refresh.add(new SimpleAttributeModifier("content", content));
		add(refresh);
		
		add(new Label("signedOutText", "Du er nu logget ud af systemet."));
	}

	protected PageCategory getPageCategory() {
		return null;
	}

	protected String getPageLabel() {
		return null;
	}
	
}
