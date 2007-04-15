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
package dk.teachus.frontend.components;

import java.util.List;

import wicket.Component;
import wicket.extensions.markup.html.repeater.data.table.DataTable;
import wicket.extensions.markup.html.repeater.data.table.HeadersToolbar;
import wicket.extensions.markup.html.repeater.data.table.IColumn;
import wicket.extensions.markup.html.repeater.data.table.NavigationToolbar;
import wicket.markup.html.WebComponent;
import wicket.markup.html.basic.Label;
import wicket.markup.html.panel.Panel;
import wicket.markup.repeater.data.ListDataProvider;
import wicket.model.AbstractReadOnlyModel;
import dk.teachus.frontend.TeachUsSession;

public class ListPanel extends Panel {
	private static final long serialVersionUID = 1L;

	public ListPanel(String id, IColumn[] columns, List data) {
		super(id);
		
		DataTable dataTable = new DataTable("table", columns, new ListDataProvider(data), 40);
		dataTable.addTopToolbar(new HeadersToolbar(dataTable, null));
		dataTable.addBottomToolbar(new NavigationToolbar(dataTable) {
			private static final long serialVersionUID = 1L;

			@Override
			protected WebComponent newNavigatorLabel(String navigatorId, final DataTable table) {
				Label label = new Label(navigatorId, new AbstractReadOnlyModel() {
					private static final long serialVersionUID = 1L;

					@Override
					public Object getObject(Component component) {
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
		});
		add(dataTable);
	}

}
