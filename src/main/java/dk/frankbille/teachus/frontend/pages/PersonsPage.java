package dk.frankbille.teachus.frontend.pages;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import wicket.ajax.AjaxRequestTarget;
import wicket.markup.html.WebComponent;
import wicket.markup.html.WebMarkupContainer;
import wicket.markup.html.basic.Label;
import wicket.markup.html.link.Link;
import wicket.markup.repeater.RepeatingView;
import wicket.model.CompoundPropertyModel;
import dk.frankbille.teachus.domain.Person;
import dk.frankbille.teachus.frontend.TeachUsSession;
import dk.frankbille.teachus.frontend.UserLevel;
import dk.frankbille.teachus.frontend.components.Toolbar;
import dk.frankbille.teachus.frontend.components.Toolbar.ToolbarItem;

public abstract class PersonsPage<P extends Person> extends AuthenticatedBasePage {

	private static final String PLACEHOLDER = "placeholder"; //$NON-NLS-1$

	protected PersonsPage(UserLevel userLevel) {
		super(userLevel);
		
		// Toolbar
		List<ToolbarItem> items = new ArrayList<ToolbarItem>();
		items.add(new ToolbarItem(getNewPersonLabel()) {
			private static final long serialVersionUID = 1L;

			@Override
			public void onEvent(AjaxRequestTarget target) {
				getRequestCycle().setResponsePage(getPersonPage(getNewPerson()));
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
		
		final List<FunctionItem> functions = getFunctions();
		
		// HEADERS
		add(new Label("name", TeachUsSession.get().getString("General.name"))); //$NON-NLS-1$ //$NON-NLS-2$
		add(new Label("username", TeachUsSession.get().getString("General.username"))); //$NON-NLS-1$ //$NON-NLS-2$
		add(new Label("email", TeachUsSession.get().getString("General.email"))); //$NON-NLS-1$ //$NON-NLS-2$
		add(new Label("phoneNumber", TeachUsSession.get().getString("General.phoneNumber"))); //$NON-NLS-1$ //$NON-NLS-2$
		add(new Label("functions", TeachUsSession.get().getString("General.functions")) {
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isVisible() {
				return functions != null && functions.isEmpty() == false;
			}
		});
		
		// DATA
		RepeatingView rows = new RepeatingView("rows"); //$NON-NLS-1$
		add(rows);
		
		List<P> persons = getPersons();
		if (persons != null) {
			for (final P person : persons) {
				WebMarkupContainer row = new WebMarkupContainer(rows.newChildId(), new CompoundPropertyModel(person));
				rows.add(row);
				
				Link link = new Link("link") {
					private static final long serialVersionUID = 1L;

					@Override
					public void onClick() {
						getRequestCycle().setResponsePage(getPersonPage(person));
					}					
				};
				link.add(new Label("name")); //$NON-NLS-1$
				row.add(link);
				row.add(new Label("username")); //$NON-NLS-1$
				row.add(new Label("email")); //$NON-NLS-1$
				row.add(new Label("phoneNumber")); //$NON-NLS-1$
				
				WebMarkupContainer functionsCell = new WebMarkupContainer("functions") {
					private static final long serialVersionUID = 1L;

					@Override
					public boolean isVisible() {
						return functions != null && functions.isEmpty() == false;
					}
				};
				row.add(functionsCell);
				RepeatingView functionsView = new RepeatingView("functions");
				functionsCell.add(functionsView);
				if (functions != null) {
					for (final FunctionItem function : functions) {
						Link functionLink = new Link(functionsView.newChildId()) {
							private static final long serialVersionUID = 1L;

							@Override
							public void onClick() {
								function.onEvent(person);								
							}
						};
						functionsView.add(functionLink);
						functionLink.add(new Label("label", function.getLabel()));
					}
				}
			}
		}
	}

	protected abstract List<P> getPersons();
	
	protected abstract P getNewPerson();
	
	protected abstract String getNewPersonLabel();
	
	protected abstract boolean showNewPersonLink();
	
	protected abstract PersonPage getPersonPage(P person);
	
	protected List<FunctionItem> getFunctions() {
		return null;
	}
	
	public abstract class FunctionItem implements Serializable {
		private String label;
		
		public FunctionItem(String label) {
			this.label = label;
		}

		public String getLabel() {
			return label;
		}

		protected abstract void onEvent(P person);
	}
}
