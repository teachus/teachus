package dk.frankbille.teachus.frontend.pages.stats;

import wicket.ResourceReference;
import wicket.RestartResponseAtInterceptPageException;
import wicket.protocol.http.WebApplication;
import dk.frankbille.teachus.domain.Teacher;
import dk.frankbille.teachus.frontend.TeachUsSession;
import dk.frankbille.teachus.frontend.UserLevel;
import dk.frankbille.teachus.frontend.pages.AuthenticatedBasePage;
import dk.frankbille.teachus.frontend.utils.Icons;

public abstract class AbstractStatisticsPage extends AuthenticatedBasePage {
	
	public AbstractStatisticsPage() {
		super(UserLevel.TEACHER);
		
		if (TeachUsSession.get().getUserLevel() != UserLevel.TEACHER) {
			throw new RestartResponseAtInterceptPageException(WebApplication.get().getHomePage());
		}
	}
	
	protected Teacher getTeacher() {
		return (Teacher) TeachUsSession.get().getPerson();
	}
	
	@Override
	protected ResourceReference getPageIcon() {
		return Icons.STATS;
	}

}
