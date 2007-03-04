package dk.teachus.frontend.pages.persons;

import dk.teachus.domain.Person;
import dk.teachus.frontend.UserLevel;
import dk.teachus.frontend.components.person.PersonPanel;
import dk.teachus.frontend.pages.AuthenticatedBasePage;

public abstract class PersonPage<P extends Person> extends AuthenticatedBasePage {	
	private P person;
	
	public PersonPage(UserLevel userLevel, final P person) {
		super(userLevel);
		
		this.person = person;
		
		add(createPersonPanel("personPanel", person));
	}
	
	protected abstract PersonPanel createPersonPanel(String wicketId, P person);
	
	@Override
	protected String getPageLabel() {
		return person.getName();
	}
}
