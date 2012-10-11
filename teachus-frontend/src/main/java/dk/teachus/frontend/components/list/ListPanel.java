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

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.navigation.paging.AjaxPagingNavigation;
import org.apache.wicket.ajax.markup.html.navigation.paging.AjaxPagingNavigator;
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
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.navigation.paging.IPageable;
import org.apache.wicket.markup.html.navigation.paging.IPagingLabelProvider;
import org.apache.wicket.markup.html.navigation.paging.PagingNavigation;
import org.apache.wicket.markup.html.navigation.paging.PagingNavigator;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.markup.repeater.data.ListDataProvider;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import dk.teachus.frontend.TeachUsSession;

public class ListPanel<T extends Serializable> extends Panel {
	private static final long serialVersionUID = 1L;

	public ListPanel(String id, List<IColumn<T>> columns, ISortableDataProvider<T> dataProvider) {
		this(id, columns, dataProvider, null);
	}
	
	public ListPanel(String id, List<IColumn<T>> columns, ISortableDataProvider<T> dataProvider, TeachUsFilter<T> filterStateLocator) {
		super(id);
		
		createList(columns, dataProvider, filterStateLocator);
	}
	
	public ListPanel(String id, List<IColumn<T>> columns, List<T> data) {
		super(id);
		
		final IDataProvider<T> listDataProvider = new ListDataProvider<T>(data);
			
		ISortableDataProvider<T> dataProvider = new SortableDataProvider<T>() {
			private static final long serialVersionUID = 1L;

			public Iterator<? extends T> iterator(int first, int count) {
				return listDataProvider.iterator(first, count);
			}

			public IModel<T> model(T object) {
				return listDataProvider.model(object);
			}

			public int size() {
				return listDataProvider.size();
			}			
		};
		
		createList(columns, dataProvider, null);
	}

	private void createList(List<IColumn<T>> columns, ISortableDataProvider<T> dataProvider, final TeachUsFilter<T> filterStateLocator) {
		FilterForm<T> form = null;
		MarkupContainer parent = null;
		if (filterStateLocator != null) {
			form = new FilterForm<T>("filterForm", filterStateLocator) {
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
		}
		add(parent);
		
		parent.add(createDataTable(columns, dataProvider, form, filterStateLocator));
	}

	private DataTable<T> createDataTable(List<IColumn<T>> columns, ISortableDataProvider<T> dataProvider, FilterForm<T> form, TeachUsFilter<T> filterStateLocator) {
		DataTable<T> dataTable = new DataTable<T>("table", columns, dataProvider, 40);

		if (form != null && filterStateLocator != null) {
			dataTable.addTopToolbar(new FilterSubmitToolbar(dataTable, filterStateLocator));
			dataTable.addTopToolbar(new FilterToolbar(dataTable, form, filterStateLocator));
		}
		
		dataTable.addTopToolbar(new AjaxFallbackHeadersToolbar(dataTable, dataProvider));
		dataTable.addTopToolbar(createNavigationToolbar(dataTable));
		dataTable.addBottomToolbar(createNavigationToolbar(dataTable));
		return dataTable;
	}

	private AjaxNavigationToolbar createNavigationToolbar(DataTable<T> dataTable) {
		return new AjaxNavigationToolbar(dataTable) {
			private static final long serialVersionUID = 1L;

			@Override
			protected WebComponent newNavigatorLabel(String navigatorId, final DataTable<?> table) {
				Label label = new Label(navigatorId, new AbstractReadOnlyModel<String>() {
					private static final long serialVersionUID = 1L;

					@Override
					public String getObject() {
						int of = table.getRowCount();
						int from = table.getCurrentPage() * table.getItemsPerPage();
						int to = Math.min(of, from + table.getItemsPerPage());

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
			
			@Override
			protected PagingNavigator newPagingNavigator(String navigatorId, DataTable<?> table) {
				return new AjaxPagingNavigator(navigatorId, table) {
					private static final long serialVersionUID = 1L;
					
					@Override
					protected void onAjaxEvent(final AjaxRequestTarget target) {
						target.add(getTable());
					}
					
					@Override
					protected Link<?> newPagingNavigationLink(String id, IPageable pageable, final int pageNumber) {
						final Link<?> pagingNavigationLink = super.newPagingNavigationLink(id, pageable, pageNumber);
						pagingNavigationLink.setBody(Model.of(""));
						pagingNavigationLink.add(AttributeModifier.append("class", new AbstractReadOnlyModel<String>() {
							private static final long serialVersionUID = 1L;

							@Override
							public String getObject() {
								String cls = "btn btn-mini";
								if (pageNumber == 0) {
									cls += " icon-fast-backward";
								} else {
									cls += " icon-fast-forward";
								}
								if (false == pagingNavigationLink.isEnabled()) {
									cls += " disabled";
								}
								return cls;
							}
						}));
						return pagingNavigationLink;
					}
					
					@Override
					protected Link<?> newPagingNavigationIncrementLink(String id, IPageable pageable, final int increment) {
						final Link<?> pagingNavigationIncrementLink = super.newPagingNavigationIncrementLink(id, pageable, increment);
						pagingNavigationIncrementLink.setBody(Model.of(""));
						pagingNavigationIncrementLink.add(AttributeModifier.append("class", new AbstractReadOnlyModel<String>() {
							private static final long serialVersionUID = 1L;

							@Override
							public String getObject() {
								String cls = "btn btn-mini";
								if (increment < 0) {
									cls += " icon-backward";
								} else {
									cls += " icon-forward";
								}
								if (false == pagingNavigationIncrementLink.isEnabled()) {
									cls += " disabled";
								}
								return cls;
							}
						}));
						return pagingNavigationIncrementLink;
					}
					
					@Override
					protected PagingNavigation newNavigation(String id, IPageable pageable, IPagingLabelProvider labelProvider) {
						return new AjaxPagingNavigation(id, pageable, labelProvider) {
							private static final long serialVersionUID = 1L;
							
							@Override
							protected Link<?> newPagingNavigationLink(String id, IPageable pageable, int pageIndex) {
								final Link<?> pagingNavigationLink = super.newPagingNavigationLink(id, pageable, pageIndex);
								pagingNavigationLink.add(AttributeModifier.append("class", new AbstractReadOnlyModel<String>() {
									private static final long serialVersionUID = 1L;

									@Override
									public String getObject() {
										StringBuilder cls = new StringBuilder();
										cls.append("btn btn-mini");
										if (false == pagingNavigationLink.isEnabled()) {
											cls.append(" btn-primary disabled");
										}
										return cls.toString();
									}
								}));
								return pagingNavigationLink;
							}
						};
					}
				};
			}
		};
	}

}
