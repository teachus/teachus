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
package dk.teachus.frontend.components.list;

import java.util.Iterator;
import java.util.List;

import org.apache.wicket.MarkupContainer;
import org.apache.wicket.extensions.ajax.markup.html.repeater.data.table.AjaxFallbackHeadersToolbar;
import org.apache.wicket.extensions.ajax.markup.html.repeater.data.table.AjaxNavigationToolbar;
import org.apache.wicket.extensions.markup.html.repeater.data.table.DataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.ISortableDataProvider;
import org.apache.wicket.extensions.markup.html.repeater.data.table.filter.FilterForm;
import org.apache.wicket.extensions.markup.html.repeater.data.table.filter.FilterToolbar;
import org.apache.wicket.extensions.markup.html.repeater.util.SortableDataProvider;
import org.apache.wicket.markup.html.WebComponent;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.markup.repeater.data.ListDataProvider;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;

import dk.teachus.frontend.TeachUsSession;

public class ListPanel extends Panel {
	private static final long serialVersionUID = 1L;

	public ListPanel(String id, IColumn[] columns, ISortableDataProvider dataProvider) {
		this(id, columns, dataProvider, null);
	}
	
	public ListPanel(String id, IColumn[] columns, ISortableDataProvider dataProvider, TeachUsFilter filterStateLocator) {
		super(id);
		
		createList(columns, dataProvider, filterStateLocator);
	}
	
	public ListPanel(String id, IColumn[] columns, List<?> data) {
		super(id);
		
		final IDataProvider listDataProvider = new ListDataProvider(data);
			
		ISortableDataProvider dataProvider = new SortableDataProvider() {
			private static final long serialVersionUID = 1L;

			public Iterator<?> iterator(int first, int count) {
				return listDataProvider.iterator(first, count);
			}

			public IModel model(Object object) {
				return listDataProvider.model(object);
			}

			public int size() {
				return listDataProvider.size();
			}			
		};
		
		createList(columns, dataProvider, null);
	}

	private void createList(IColumn[] columns, ISortableDataProvider dataProvider, final TeachUsFilter filterStateLocator) {
		FilterForm form = null;
		MarkupContainer parent = null;
		if (filterStateLocator != null) {
			form = new FilterForm("filterForm", filterStateLocator) {
				private static final long serialVersionUID = 1L;

				@Override
				protected void onSubmit() {
					filterStateLocator.onSubmit();
				}
			};
			parent = form;
		} else {
			parent = new WebMarkupContainer("filterForm");
			parent.setRenderBodyOnly(true);
			parent.add(new WebComponent("focus-tracker").setVisible(false));
			parent.add(new WebComponent("focus-restore").setVisible(false));
		}
		add(parent);
		
		parent.add(createDataTable(columns, dataProvider, form, filterStateLocator));
	}

	private DataTable createDataTable(IColumn[] columns, ISortableDataProvider dataProvider, FilterForm form, TeachUsFilter filterStateLocator) {
		DataTable dataTable = new DataTable("table", columns, dataProvider, 40);

		if (form != null && filterStateLocator != null) {
			dataTable.addTopToolbar(new FilterSubmitToolbar(dataTable, filterStateLocator));
			dataTable.addTopToolbar(new FilterToolbar(dataTable, form, filterStateLocator));
		}
		
		dataTable.addTopToolbar(new AjaxFallbackHeadersToolbar(dataTable, dataProvider));
		dataTable.addTopToolbar(createNavigationToolbar(dataTable));
		dataTable.addBottomToolbar(createNavigationToolbar(dataTable));
		return dataTable;
	}

	private AjaxNavigationToolbar createNavigationToolbar(DataTable dataTable) {
		return new AjaxNavigationToolbar(dataTable) {
			private static final long serialVersionUID = 1L;

			@Override
			protected WebComponent newNavigatorLabel(String navigatorId, final DataTable table) {
				Label label = new Label(navigatorId, new AbstractReadOnlyModel() {
					private static final long serialVersionUID = 1L;

					@Override
					public Object getObject() {
						int of = table.getRowCount();
						int from = table.getCurrentPage() * table.getRowsPerPage();
						int to = Math.min(of, from + table.getRowsPerPage());

						from++;

						if (of == 0)
						{
							from = 0;
							to = 0;
						}

						String label = TeachUsSession.get().getString("ListPanel.navigatorLabel");
						label = label.replace("${from}", ""+from);
						label = label.replace("${to}", ""+to);
						label = label.replace("${of}", ""+of);
						
						return label;
					}					
				});
				label.setRenderBodyOnly(true);
				return label;
			}
		};
	}

}
