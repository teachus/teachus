package dk.teachus.frontend.pages.persons;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import wicket.ajax.AjaxRequestTarget;
import wicket.extensions.markup.html.repeater.data.table.IColumn;
import wicket.model.Model;
import dk.teachus.domain.Person;
import dk.teachus.frontend.TeachUsSession;
import dk.teachus.frontend.UserLevel;
import dk.teachus.frontend.components.FunctionsColumn;
import dk.teachus.frontend.components.LinkPropertyColumn;
import dk.teachus.frontend.components.ListPanel;
import dk.teachus.frontend.components.RendererPropertyColumn;
import dk.teachus.frontend.components.Toolbar;
import dk.teachus.frontend.components.Toolbar.ToolbarItem;
import dk.teachus.frontend.pages.AuthenticatedBasePage;

public abstract class PersonsPage<P extends Person> extends AuthenticatedBasePage {
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

		List<P> persons = getPersons();
		
		final List<FunctionItem> functions = getFunctions();
		
		List<IColumn> columns = new ArrayList<IColumn>();
		columns.add(new LinkPropertyColumn(new Model(TeachUsSession.get().getString("General.name")), "name") {
			private static final long serialVersionUID = 1L;

			@SuppressWarnings("unchecked")
			@Override
			protected void onClick(Object rowModelObject) {
				P person = (P) rowModelObject;
				getRequestCycle().setResponsePage(getPersonPage(person));
			}					
		});
		columns.add(new RendererPropertyColumn(new Model(TeachUsSession.get().getString("General.username")), "username"));
		columns.add(new RendererPropertyColumn(new Model(TeachUsSession.get().getString("General.email")), "email"));
		columns.add(new RendererPropertyColumn(new Model(TeachUsSession.get().getString("General.phoneNumber")), "phoneNumber"));
		if (functions != null) {
			columns.add(new FunctionsColumn<P>(new Model(TeachUsSession.get().getString("General.functions")), functions));
		}
		
		add(new ListPanel("list", columns.toArray(new IColumn[columns.size()]), persons));
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

		public abstract void onEvent(P person);
		
		public String getClickConfirmText(P person) {
			return null;
		}
	}
}
