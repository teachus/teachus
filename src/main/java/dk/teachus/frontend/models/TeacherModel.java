package dk.teachus.frontend.models;

import dk.teachus.domain.Teacher;
import dk.teachus.domain.impl.TeacherImpl;

public class TeacherModel extends PersonModel<Teacher> {
	private static final long serialVersionUID = 1L;

	public TeacherModel(Long personId) {
		super(personId);
	}

	@Override
	protected Teacher createNewPerson() {
		return new TeacherImpl();
	}

}
