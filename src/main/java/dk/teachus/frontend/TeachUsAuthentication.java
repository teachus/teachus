package dk.teachus.frontend;

import dk.teachus.frontend.pages.AuthenticatedBasePage;
import dk.teachus.frontend.pages.SignInPage;
import wicket.authorization.strategies.page.SimplePageAuthorizationStrategy;

public class TeachUsAuthentication extends SimplePageAuthorizationStrategy {

	public TeachUsAuthentication() {
		super(AuthenticatedBasePage.class, SignInPage.class);
	}

	@Override
	protected boolean isAuthorized() {
		return TeachUsSession.get().isAuthenticated();
	}

}
