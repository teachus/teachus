package dk.frankbille.teachus.frontend.pages;

import wicket.ResourceReference;
import wicket.RestartResponseAtInterceptPageException;
import dk.frankbille.teachus.frontend.TeachUsApplication;
import dk.frankbille.teachus.frontend.TeachUsSession;
import dk.frankbille.teachus.frontend.UserLevel;

public class SignOutPage extends AuthenticatedBasePage {
	private static final long serialVersionUID = 1L;

	public SignOutPage() {
		super(UserLevel.PUPIL);
		
		TeachUsSession.get().signOut();
		
		throw new RestartResponseAtInterceptPageException(TeachUsApplication.get().getHomePage());
	}
	
	@Override
	protected ResourceReference getPageIcon() {
		return null;
	}

	@Override
	protected String getPageLabel() {
		return null;
	}

}
