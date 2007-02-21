package dk.frankbille.teachus.frontend.pages;

import wicket.ResourceReference;
import dk.frankbille.teachus.domain.Admin;
import dk.frankbille.teachus.domain.Person;
import dk.frankbille.teachus.domain.Teacher;
import dk.frankbille.teachus.frontend.UserLevel;
import dk.frankbille.teachus.frontend.utils.Resources;

public class TeacherPage extends PersonPage {
	private static final long serialVersionUID = 1L;

	public TeacherPage(Teacher teacher) {
		super(UserLevel.TEACHER, teacher);
	}

	@Override
	protected Class<? extends PersonsPage> getPersonsPageClass() {
		return TeachersPage.class;
	}

	@Override
	protected ResourceReference getPageIcon() {
		return Resources.TEACHER;
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

}
