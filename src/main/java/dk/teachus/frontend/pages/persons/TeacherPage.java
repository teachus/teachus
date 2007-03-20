package dk.teachus.frontend.pages.persons;

import dk.teachus.domain.Teacher;
import dk.teachus.frontend.UserLevel;
import dk.teachus.frontend.components.person.PersonPanel;
import dk.teachus.frontend.components.person.TeacherPanel;
import dk.teachus.frontend.models.TeacherModel;

public class TeacherPage extends PersonPage<Teacher> {
	private static final long serialVersionUID = 1L;

	public TeacherPage(Teacher teacher) {
		super(UserLevel.TEACHER, teacher);
	}

	@Override
	protected AuthenticatedPageCategory getPageCategory() {
		return AuthenticatedPageCategory.TEACHERS;
	}

	@Override
	protected PersonPanel createPersonPanel(String wicketId, Teacher teacher) {
		return new TeacherPanel(wicketId, new TeacherModel(teacher.getId()));
	}

}
