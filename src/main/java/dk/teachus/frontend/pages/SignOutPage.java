package dk.teachus.frontend.pages;

import wicket.RestartResponseAtInterceptPageException;
import dk.teachus.frontend.TeachUsSession;
import dk.teachus.frontend.UserLevel;

public class SignOutPage extends AuthenticatedBasePage {
	private static final long serialVersionUID = 1L;

	public SignOutPage() {
		super(UserLevel.PUPIL);
		
		TeachUsSession.get().signOut();
		
		throw new RestartResponseAtInterceptPageException(SignedOutPage.class);
	}

	@Override
	protected String getPageLabel() {
		return null;
	}

	@Override
	protected PageCategory getPageCategory() {
		return AuthenticatedPageCategory.SIGNOUT;
	}

}
