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

import java.util.ArrayList;
import java.util.List;

import wicket.extensions.markup.html.repeater.data.table.AbstractColumn;
import wicket.extensions.markup.html.repeater.data.table.DataTable;
import wicket.extensions.markup.html.repeater.data.table.IColumn;
import wicket.markup.html.basic.Label;
import wicket.markup.html.panel.Panel;
import wicket.markup.repeater.Item;
import wicket.model.IModel;
import wicket.model.Model;
import wicket.util.tester.TestPanelSource;
import dk.teachus.test.WicketSpringTestCase;

public class TestListPanel extends WicketSpringTestCase {
	private static final long serialVersionUID = 1L;

	public void testRender() {
		tester.startPanel(new TestPanelSource() {
			private static final long serialVersionUID = 1L;

			public Panel getTestPanel(String panelId) {
				List<String> data = new ArrayList<String>(); 
				
				IColumn[] columns = new IColumn[] {
						new AbstractColumn(new Model("Header")) {
							private static final long serialVersionUID = 1L;

							public void populateItem(Item cellItem, String componentId, IModel rowModel) {
								cellItem.add(new Label(componentId, rowModel));
							}
						}
				};
				
				return new ListPanel(panelId, columns, data);
			}
		});
		
		tester.assertComponent("panel", ListPanel.class);
		tester.assertComponent("panel:table", DataTable.class);
	}
	
}
