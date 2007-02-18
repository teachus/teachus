package dk.frankbille.teachus.frontend.pages;

import java.util.List;

import wicket.ResourceReference;
import dk.frankbille.teachus.dao.PersonDAO;
import dk.frankbille.teachus.domain.Person;
import dk.frankbille.teachus.domain.Teacher;
import dk.frankbille.teachus.domain.impl.TeacherImpl;
import dk.frankbille.teachus.frontend.TeachUsApplication;
import dk.frankbille.teachus.frontend.TeachUsSession;
import dk.frankbille.teachus.frontend.UserLevel;
import dk.frankbille.teachus.frontend.utils.Icons;

public class TeachersPage extends PersonsPage {
	private static final long serialVersionUID = 1L;

	public TeachersPage() {
		super(UserLevel.ADMIN);
	}
	
	@Override
	protected ResourceReference getPageIcon() {
		return Icons.TEACHER;
	}

	@Override
	protected String getPageLabel() {
		return TeachUsSession.get().getString("General.teachers"); //$NON-NLS-1$
	}

	@Override
	protected List<Person> getPersons() {
		PersonDAO personDAO = TeachUsApplication.get().getPersonDAO();
		return personDAO.getPersons(Teacher.class);
	}

	@Override
	protected Person getNewPerson() {
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

}
