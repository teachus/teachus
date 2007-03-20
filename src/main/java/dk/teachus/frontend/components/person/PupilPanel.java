package dk.teachus.frontend.components.person;

import dk.teachus.domain.Admin;
import dk.teachus.domain.Person;
import dk.teachus.domain.Pupil;
import dk.teachus.domain.Teacher;
import dk.teachus.frontend.TeachUsSession;
import dk.teachus.frontend.UserLevel;
import dk.teachus.frontend.models.PupilModel;
import dk.teachus.frontend.pages.persons.PersonsPage;
import dk.teachus.frontend.pages.persons.PupilsPage;

public class PupilPanel extends PersonPanel {
	private static final long serialVersionUID = 1L;

	public PupilPanel(String id, PupilModel pupilModel) {
		super(id, pupilModel);
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
	protected boolean isTeacherVisible() {
		return true;
	}

}
