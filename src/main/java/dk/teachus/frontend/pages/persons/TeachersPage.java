package dk.teachus.frontend.pages.persons;

import java.util.ArrayList;
import java.util.List;

import dk.teachus.dao.PersonDAO;
import dk.teachus.domain.Teacher;
import dk.teachus.frontend.TeachUsApplication;
import dk.teachus.frontend.TeachUsSession;
import dk.teachus.frontend.UserLevel;
import dk.teachus.frontend.components.FunctionsColumn.FunctionItem;
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
	protected String getNewPersonLabel() {
		return TeachUsSession.get().getString("TeachersPage.newTeacher"); //$NON-NLS-1$
	}

	@Override
	protected boolean showNewPersonLink() {
		return true;
	}
	
	@Override
	protected PersonPage getPersonPage(Long personId) {
		return new TeacherPage(new TeacherModel(personId));
	}

	@Override
	protected AuthenticatedPageCategory getPageCategory() {
		return AuthenticatedPageCategory.TEACHERS;
	}
	
	@Override
	protected List<FunctionItem> getFunctions() {
		List<FunctionItem> functions = new ArrayList<FunctionItem>();
		
		functions.add(new FunctionItem("Delete") {
			private static final long serialVersionUID = 1L;

			@Override
			public void onEvent(Object object) {
				Teacher teacher = (Teacher) object;
				
				PersonDAO personDAO = TeachUsApplication.get().getPersonDAO();
				personDAO.deleteTeacher(teacher);
				
				getRequestCycle().setResponsePage(TeachersPage.class);
			}
			
			@Override
			public String getClickConfirmText(Object object) {
				Teacher teacher = (Teacher) object;
				String deleteText = TeachUsSession.get().getString("TeachersPage.deleteConfirm"); //$NON-NLS-1$
				deleteText = deleteText.replace("{personname}", teacher.getName()); //$NON-NLS-1$
				return deleteText;
			}
		});
		
		return functions;
	}

}
