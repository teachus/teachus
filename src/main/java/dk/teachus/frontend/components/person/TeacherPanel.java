package dk.teachus.frontend.components.person;

import dk.teachus.domain.Admin;
import dk.teachus.domain.Person;
import dk.teachus.domain.Teacher;
import dk.teachus.frontend.models.TeacherModel;
import dk.teachus.frontend.pages.persons.PersonsPage;
import dk.teachus.frontend.pages.persons.TeachersPage;

public class TeacherPanel extends PersonPanel {
	private static final long serialVersionUID = 1L;

	public TeacherPanel(String id, TeacherModel teacherModel) {
		super(id, teacherModel);
	}
	
	@Override
	protected Class<? extends PersonsPage> getPersonsPageClass() {
		return TeachersPage.class;
	}

	@Override
	protected boolean allowUserEditing(Person loggedInPerson, Person editPerson) {
		boolean allow = false;
		
		if (loggedInPerson instanceof Admin) {
			allow = true;
		} else if (loggedInPerson instanceof Teacher) {
			allow = loggedInPerson.getId().equals(editPerson.getId());
		}
		
		return allow;
	}
	
	@Override
	protected boolean isThemeVisible() {
		return true;
	}

}
