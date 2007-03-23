package dk.teachus.frontend.pages.persons;

import dk.teachus.frontend.UserLevel;
import dk.teachus.frontend.components.person.AdminPanel;
import dk.teachus.frontend.components.person.PersonPanel;
import dk.teachus.frontend.models.AdminModel;

public class AdminPage extends PersonPage<AdminModel> {
	private static final long serialVersionUID = 1L;
	
	public AdminPage(AdminModel adminModel) {
		super(UserLevel.ADMIN, adminModel);
	}

	@Override
	protected AuthenticatedPageCategory getPageCategory() {
		return AuthenticatedPageCategory.ADMINS;
	}

	@Override
	protected PersonPanel createPersonPanel(String wicketId, AdminModel adminModel) {
		return new AdminPanel(wicketId, adminModel);
	}
	
}
