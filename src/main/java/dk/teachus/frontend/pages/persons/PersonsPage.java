package dk.teachus.frontend.pages.persons;

import java.util.ArrayList;
import java.util.List;

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
import dk.teachus.frontend.components.FunctionsColumn.FunctionItem;
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
			public void onEvent() {
				getRequestCycle().setResponsePage(getPersonPage(null));
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
				getRequestCycle().setResponsePage(getPersonPage(person.getId()));
			}					
		});
		columns.add(new RendererPropertyColumn(new Model(TeachUsSession.get().getString("General.username")), "username"));
		columns.add(new RendererPropertyColumn(new Model(TeachUsSession.get().getString("General.email")), "email"));
		columns.add(new RendererPropertyColumn(new Model(TeachUsSession.get().getString("General.phoneNumber")), "phoneNumber"));
		if (functions != null) {
			columns.add(new FunctionsColumn(new Model(TeachUsSession.get().getString("General.functions")), functions));
		}
		
		add(new ListPanel("list", columns.toArray(new IColumn[columns.size()]), persons));
	}

	protected abstract List<P> getPersons();
	
	protected abstract String getNewPersonLabel();
	
	protected abstract boolean showNewPersonLink();
	
	protected abstract PersonPage getPersonPage(Long personId);
	
	protected List<FunctionItem> getFunctions() {
		return null;
	}
	
	public abstract class PersonFunctionItem extends FunctionItem {
		public PersonFunctionItem(String label) {
			super(label);
		}

		@SuppressWarnings("unchecked")
		@Override
		public final void onEvent(Object object) {
			onEvent((P) object);
		}
		
		@SuppressWarnings("unchecked")
		@Override
		public final String getClickConfirmText(Object object) {
			return getClickConfirmText((P) object);
		}
		
		public abstract void onEvent(P person);
		
		public String getClickConfirmText(P person) {
			return null;
		}
	}
}
