package dk.frankbille.teachus.frontend.pages;

import java.util.ArrayList;
import java.util.List;

import wicket.ajax.AjaxRequestTarget;
import wicket.ajax.markup.html.AjaxLink;
import wicket.markup.html.WebComponent;
import wicket.markup.html.WebMarkupContainer;
import wicket.markup.html.basic.Label;
import wicket.markup.repeater.RepeatingView;
import wicket.model.CompoundPropertyModel;
import dk.frankbille.teachus.domain.Person;
import dk.frankbille.teachus.frontend.TeachUsSession;
import dk.frankbille.teachus.frontend.UserLevel;
import dk.frankbille.teachus.frontend.components.PersonPanel;
import dk.frankbille.teachus.frontend.components.Toolbar;
import dk.frankbille.teachus.frontend.components.Toolbar.ToolbarItem;

public abstract class PersonsPage extends AuthenticatedBasePage {

	private static final String PLACEHOLDER = "placeholder"; //$NON-NLS-1$

	protected PersonsPage(UserLevel userLevel) {
		super(userLevel);
		
		// Toolbar
		List<ToolbarItem> items = new ArrayList<ToolbarItem>();
		items.add(new ToolbarItem(getNewPersonLabel()) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void onEvent(AjaxRequestTarget target) {
				target.addComponent(createPersonPanel(getNewPerson()));
			}			
		});
		add(new Toolbar("toolbar", items) { //$NON-NLS-1$
			private static final long serialVersionUID = 1L;
			
			@Override
			public boolean isVisible() {
				return showNewPersonLink();
			}
		});
		
		// Placeholder
		add(new WebComponent(PLACEHOLDER).setOutputMarkupId(true));
		
		// HEADERS
		add(new Label("name", TeachUsSession.get().getString("General.name"))); //$NON-NLS-1$ //$NON-NLS-2$
		add(new Label("username", TeachUsSession.get().getString("General.username"))); //$NON-NLS-1$ //$NON-NLS-2$
		add(new Label("email", TeachUsSession.get().getString("General.email"))); //$NON-NLS-1$ //$NON-NLS-2$
		
		// DATA
		RepeatingView rows = new RepeatingView("rows"); //$NON-NLS-1$
		add(rows);
		
		List<Person> persons = getPersons();
		if (persons != null) {
			for (final Person person : persons) {
				WebMarkupContainer row = new WebMarkupContainer(rows.newChildId(), new CompoundPropertyModel(person));
				rows.add(row);
				
				AjaxLink link = new AjaxLink("link") { //$NON-NLS-1$
					private static final long serialVersionUID = 1L;
	
					@Override
					public void onClick(AjaxRequestTarget target) {
						target.addComponent(createPersonPanel(person));
					}					
				};
				link.add(new Label("name")); //$NON-NLS-1$
				row.add(link);
				row.add(new Label("username")); //$NON-NLS-1$
				row.add(new Label("email")); //$NON-NLS-1$
			}
		}
	}
	
	private PersonPanel createPersonPanel(Person person) {
		PersonPanel personPanel = new PersonPanel(PLACEHOLDER, person) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void personSaved(AjaxRequestTarget target) {
				getRequestCycle().setResponsePage(PersonsPage.this.getClass());
			}			
		};
		personPanel.setOutputMarkupId(true);
		PersonsPage.this.replace(personPanel);
		
		return personPanel;
	}

	protected abstract List<Person> getPersons();
	
	protected abstract Person getNewPerson();
	
	protected abstract String getNewPersonLabel();
	
	protected abstract boolean showNewPersonLink();
}
