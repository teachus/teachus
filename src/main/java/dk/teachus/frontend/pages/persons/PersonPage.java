package dk.teachus.frontend.pages.persons;

import dk.teachus.frontend.UserLevel;
import dk.teachus.frontend.components.person.PersonPanel;
import dk.teachus.frontend.models.PersonModel;
import dk.teachus.frontend.pages.AuthenticatedBasePage;

public abstract class PersonPage<P extends PersonModel> extends AuthenticatedBasePage {	
	private P personModel;
	
	public PersonPage(UserLevel userLevel, final P personModel) {
		super(userLevel);
		
		this.personModel = personModel;
		
		add(createPersonPanel("personPanel", personModel));
	}
	
	protected abstract PersonPanel createPersonPanel(String wicketId, P personModel);
	
	@Override
	protected String getPageLabel() {
		return personModel.getObject(this).getName();
	}
}
