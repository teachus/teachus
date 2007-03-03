package dk.teachus.frontend.pages;

import wicket.Page;
import wicket.RestartResponseAtInterceptPageException;
import dk.teachus.domain.Admin;
import dk.teachus.domain.Person;
import dk.teachus.domain.Pupil;
import dk.teachus.domain.Teacher;
import dk.teachus.frontend.TeachUsSession;
import dk.teachus.frontend.pages.calendar.PupilCalendarPage;
import dk.teachus.frontend.pages.persons.PupilsPage;
import dk.teachus.frontend.pages.persons.TeachersPage;

public class HomePage extends Page {
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
			page = InfoPage.class;
		}
		
		throw new RestartResponseAtInterceptPageException(page);
	}

}
