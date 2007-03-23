package dk.teachus.frontend.pages.persons;

import dk.teachus.frontend.UserLevel;
import dk.teachus.frontend.components.person.PersonPanel;
import dk.teachus.frontend.components.person.TeacherPanel;
import dk.teachus.frontend.models.TeacherModel;

public class TeacherPage extends PersonPage<TeacherModel> {
	private static final long serialVersionUID = 1L;

	public TeacherPage(TeacherModel teacherModel) {
		super(UserLevel.TEACHER, teacherModel);
	}

	@Override
	protected AuthenticatedPageCategory getPageCategory() {
		return AuthenticatedPageCategory.TEACHERS;
	}

	@Override
	protected PersonPanel createPersonPanel(String wicketId, TeacherModel teacherModel) {
		return new TeacherPanel(wicketId, teacherModel);
	}

}
