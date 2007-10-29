/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package dk.teachus.frontend.pages.persons;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.model.Model;

import dk.teachus.backend.domain.Person;
import dk.teachus.frontend.TeachUsSession;
import dk.teachus.frontend.UserLevel;
import dk.teachus.frontend.components.FunctionsColumn;
import dk.teachus.frontend.components.LinkPropertyColumn;
import dk.teachus.frontend.components.ListPanel;
import dk.teachus.frontend.components.RendererPropertyColumn;
import dk.teachus.frontend.components.Toolbar;
import dk.teachus.frontend.components.FunctionsColumn.FunctionItem;
import dk.teachus.frontend.components.Toolbar.ToolbarItem;
import dk.teachus.frontend.components.Toolbar.ToolbarItemInterface;
import dk.teachus.frontend.models.PersonModel;
import dk.teachus.frontend.pages.AuthenticatedBasePage;
import dk.teachus.frontend.pages.persons.mail.MailPage;

public abstract class PersonsPage<P extends Person> extends AuthenticatedBasePage {
	protected PersonsPage(UserLevel userLevel) {
		super(userLevel);
		
		// Toolbar
		List<ToolbarItemInterface> items = new ArrayList<ToolbarItemInterface>();
		items.add(new ToolbarItem(getNewPersonLabel()) {
			private static final long serialVersionUID = 1L;

			@Override
			public void onEvent() {
				getRequestCycle().setResponsePage(getPersonPage(null));
			}			
		});		
		items.add(new ToolbarItem(TeachUsSession.get().getString("General.mail")) { //$NON-NLS-1$
			private static final long serialVersionUID = 1L;

			@Override
			public void onEvent() {
				getRequestCycle().setResponsePage(MailPage.class);
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
		columns.add(new LinkPropertyColumn(new Model(TeachUsSession.get().getString("General.name")), "name") { //$NON-NLS-1$ //$NON-NLS-2$
			private static final long serialVersionUID = 1L;

			@SuppressWarnings("unchecked") //$NON-NLS-1$
			@Override
			protected void onClick(Object rowModelObject) {
				P person = (P) rowModelObject;
				getRequestCycle().setResponsePage(getPersonPage(person.getId()));
			}					
		});
		columns.add(new RendererPropertyColumn(new Model(TeachUsSession.get().getString("General.username")), "username")); //$NON-NLS-1$ //$NON-NLS-2$
		columns.add(new RendererPropertyColumn(new Model(TeachUsSession.get().getString("General.email")), "email")); //$NON-NLS-1$ //$NON-NLS-2$
		columns.add(new RendererPropertyColumn(new Model(TeachUsSession.get().getString("General.phoneNumber")), "phoneNumber")); //$NON-NLS-1$ //$NON-NLS-2$
		if (functions != null) {
			columns.add(new FunctionsColumn(new Model(TeachUsSession.get().getString("General.functions")), functions)); //$NON-NLS-1$
		}
		
		add(new ListPanel("list", columns.toArray(new IColumn[columns.size()]), persons)); //$NON-NLS-1$
	}

	protected abstract List<P> getPersons();
	
	protected abstract String getNewPersonLabel();
	
	protected abstract boolean showNewPersonLink();
	
	protected abstract PersonPage<? extends PersonModel<P>> getPersonPage(Long personId);
	
	protected List<FunctionItem> getFunctions() {
		return null;
	}
	
	public abstract class PersonFunctionItem extends FunctionItem {
		public PersonFunctionItem(String label) {
			super(label);
		}

		@SuppressWarnings("unchecked") //$NON-NLS-1$
		@Override
		public final void onEvent(Object object) {
			onEvent((P) object);
		}
		
		@SuppressWarnings("unchecked") //$NON-NLS-1$
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
