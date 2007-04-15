package dk.teachus.frontend.pages.persons;

import java.util.List;

import dk.teachus.dao.PersonDAO;
import dk.teachus.domain.Admin;
import dk.teachus.frontend.TeachUsApplication;
import dk.teachus.frontend.TeachUsSession;
import dk.teachus.frontend.UserLevel;
import dk.teachus.frontend.models.AdminModel;

public class AdminsPage extends PersonsPage<Admin> {
	private static final long serialVersionUID = 1L;
	
	public AdminsPage() {
		super(UserLevel.ADMIN);
	}

	@Override
	protected String getPageLabel() {
		return TeachUsSession.get().getString("General.administrators"); //$NON-NLS-1$
	}

	@Override
	protected List<Admin> getPersons() {
		PersonDAO personDAO = TeachUsApplication.get().getPersonDAO();
		return personDAO.getPersons(Admin.class);
	}

	@Override
	protected String getNewPersonLabel() {
		return TeachUsSession.get().getString("AdminsPage.newAdministrator"); //$NON-NLS-1$
	}

	@Override
	protected boolean showNewPersonLink() {
		return true;
	}
	
	@Override
	protected PersonPage getPersonPage(Long personId) {
		return new AdminPage(new AdminModel(personId));
	}

	@Override
	protected AuthenticatedPageCategory getPageCategory() {
		return AuthenticatedPageCategory.ADMINS;
	}

}
