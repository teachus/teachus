package dk.frankbille.teachus.frontend.pages;

import java.util.List;

import wicket.ResourceReference;
import dk.frankbille.teachus.dao.PersonDAO;
import dk.frankbille.teachus.domain.Admin;
import dk.frankbille.teachus.domain.Person;
import dk.frankbille.teachus.domain.impl.AdminImpl;
import dk.frankbille.teachus.frontend.TeachUsApplication;
import dk.frankbille.teachus.frontend.TeachUsSession;
import dk.frankbille.teachus.frontend.UserLevel;
import dk.frankbille.teachus.frontend.utils.Icons;

public class AdminsPage extends PersonsPage {
	private static final long serialVersionUID = 1L;
	
	public AdminsPage() {
		super(UserLevel.ADMIN);
	}
	
	@Override
	protected ResourceReference getPageIcon() {
		return Icons.ADMIN;
	}

	@Override
	protected String getPageLabel() {
		return TeachUsSession.get().getString("General.administrators"); //$NON-NLS-1$
	}

	@Override
	protected Person getNewPerson() {
		return new AdminImpl();
	}

	@Override
	protected List<Person> getPersons() {
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
	protected PersonPage getPersonPage(Person person) {
		return new AdminPage((Admin) person);
	}

}
