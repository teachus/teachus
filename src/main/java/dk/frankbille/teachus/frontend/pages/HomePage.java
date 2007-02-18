package dk.frankbille.teachus.frontend.pages;

import wicket.RestartResponseAtInterceptPageException;
import dk.frankbille.teachus.domain.Admin;
import dk.frankbille.teachus.domain.Person;
import dk.frankbille.teachus.domain.Pupil;
import dk.frankbille.teachus.domain.Teacher;
import dk.frankbille.teachus.frontend.TeachUsSession;

public class HomePage extends BasePage {
	private static final long serialVersionUID = 1L;

	public HomePage() {
		Person person = TeachUsSession.get().getPerson();
		
		Class page = null;
		
		if (person != null) {
			if (person instanceof Admin) {
				page = TeachersPage.class;
			} else if (person instanceof Teacher) {
				page = PupilsPage.class;
			} else if (person instanceof Pupil) {
				page = PupilCalendarPage.class;
			}
		} else {
			page = SignInPage.class;
		}
		
		throw new RestartResponseAtInterceptPageException(page);
	}

}
