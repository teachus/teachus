package dk.teachus.frontend.pages.persons;

import java.util.List;

import dk.teachus.dao.PersonDAO;
import dk.teachus.domain.Teacher;
import dk.teachus.domain.impl.TeacherImpl;
import dk.teachus.frontend.TeachUsApplication;
import dk.teachus.frontend.TeachUsSession;
import dk.teachus.frontend.UserLevel;
import dk.teachus.frontend.models.TeacherModel;

public class TeachersPage extends PersonsPage<Teacher> {
	private static final long serialVersionUID = 1L;

	public TeachersPage() {
		super(UserLevel.ADMIN);
	}

	@Override
	protected String getPageLabel() {
		return TeachUsSession.get().getString("General.teachers"); //$NON-NLS-1$
	}

	@Override
	protected List<Teacher> getPersons() {
		PersonDAO personDAO = TeachUsApplication.get().getPersonDAO();
		return personDAO.getPersons(Teacher.class);
	}

	@Override
	protected Teacher getNewPerson() {
		return new TeacherImpl();
	}

	@Override
	protected String getNewPersonLabel() {
		return TeachUsSession.get().getString("TeachersPage.newTeacher"); //$NON-NLS-1$
	}

	@Override
	protected boolean showNewPersonLink() {
		return true;
	}
	
	@Override
	protected PersonPage getPersonPage(Teacher person) {
		return new TeacherPage(new TeacherModel(person.getId()));
	}

	@Override
	protected AuthenticatedPageCategory getPageCategory() {
		return AuthenticatedPageCategory.TEACHERS;
	}

}
