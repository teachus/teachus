package dk.frankbille.teachus.frontend.pages.persons;

import java.util.List;

import wicket.ResourceReference;
import dk.frankbille.teachus.dao.PersonDAO;
import dk.frankbille.teachus.domain.Admin;
import dk.frankbille.teachus.domain.impl.AdminImpl;
import dk.frankbille.teachus.frontend.TeachUsApplication;
import dk.frankbille.teachus.frontend.TeachUsSession;
import dk.frankbille.teachus.frontend.UserLevel;
import dk.frankbille.teachus.frontend.utils.Resources;

public class AdminsPage extends PersonsPage<Admin> {
	private static final long serialVersionUID = 1L;
	
	public AdminsPage() {
		super(UserLevel.ADMIN);
	}
	
	@Override
	protected ResourceReference getPageIcon() {
		return Resources.ADMIN;
	}

	@Override
	protected String getPageLabel() {
		return TeachUsSession.get().getString("General.administrators"); //$NON-NLS-1$
	}

	@Override
	protected Admin getNewPerson() {
		return new AdminImpl();
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
	protected PersonPage getPersonPage(Admin person) {
		return new AdminPage(person);
	}

}
