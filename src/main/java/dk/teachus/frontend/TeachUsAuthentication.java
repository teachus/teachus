package dk.teachus.frontend;

import wicket.authorization.strategies.page.SimplePageAuthorizationStrategy;
import dk.teachus.frontend.pages.AuthenticatedBasePage;
import dk.teachus.frontend.pages.HomePage;

public class TeachUsAuthentication extends SimplePageAuthorizationStrategy {

	public TeachUsAuthentication() {
		super(AuthenticatedBasePage.class, HomePage.class);
	}

	@Override
	protected boolean isAuthorized() {
		return TeachUsSession.get().isAuthenticated();
	}

}
