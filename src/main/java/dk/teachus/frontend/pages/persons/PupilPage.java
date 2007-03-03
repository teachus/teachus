package dk.teachus.frontend.pages.persons;

import dk.teachus.domain.Admin;
import dk.teachus.domain.Person;
import dk.teachus.domain.Pupil;
import dk.teachus.domain.Teacher;
import dk.teachus.frontend.TeachUsSession;
import dk.teachus.frontend.UserLevel;

public class PupilPage extends PersonPage {
	private static final long serialVersionUID = 1L;

	public PupilPage(Pupil pupil) {
		super(UserLevel.PUPIL, pupil);
	}

	@Override
	protected Class<? extends PersonsPage> getPersonsPageClass() {
		return PupilsPage.class;
	}
	
	@Override
	protected boolean allowUserEditing(Person loggedInPerson, Person editPerson) {
		boolean allow = false;
		
		Pupil pupil = (Pupil) editPerson;
		
		if (loggedInPerson instanceof Admin) {
			allow = true;
		} else if (loggedInPerson instanceof Teacher) {
			allow = pupil.getTeacher().getId().equals(loggedInPerson.getId());
		} else if (loggedInPerson instanceof Pupil) {
			allow = pupil.getId().equals(loggedInPerson.getId());
		}
		
		return allow;
	}
	
	@Override
	protected boolean isUsernameEnabled() {
		return UserLevel.TEACHER.authorized(TeachUsSession.get().getUserLevel());
	}
	
	@Override
	protected boolean isLocaleVisible() {
		return false;
	}

	@Override
	protected PageCategory getPageCategory() {
		return AuthenticatedPageCategory.PUPILS;
	}

	protected String getPageLabel() {
		return null;
	}

}
