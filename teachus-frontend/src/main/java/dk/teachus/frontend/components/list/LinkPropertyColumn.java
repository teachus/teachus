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

import org.apache.wicket.Component;
import org.apache.wicket.extensions.markup.html.repeater.data.table.filter.FilterForm;
import org.apache.wicket.extensions.markup.html.repeater.data.table.filter.FilteredAbstractColumn;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

public abstract class LinkPropertyColumn extends FilteredAbstractColumn {
	private String propertyExpression;
	
	public LinkPropertyColumn(IModel displayModel, String propertyExpression) {
		this(displayModel, null, propertyExpression);
	}
	
	public LinkPropertyColumn(IModel displayModel, String sortProperty, String propertyExpression) {
		super(displayModel, sortProperty);
		
		this.propertyExpression = propertyExpression;
	}

	public void populateItem(final Item cellItem, String componentId, final IModel rowModel) {
		LinkPropertyColumnPanel linkPropertyColumnPanel = new LinkPropertyColumnPanel(componentId);
		linkPropertyColumnPanel.setRenderBodyOnly(true);
		Link link = new Link("link") {
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick() {
				LinkPropertyColumn.this.onClick(rowModel.getObject());
			}			
		};
		Label label = new Label("label", new PropertyModel(rowModel, propertyExpression));
		label.setRenderBodyOnly(true);
		link.add(label);
		linkPropertyColumnPanel.add(link);
		cellItem.add(linkPropertyColumnPanel);
	}

	protected abstract void onClick(Object rowModelObject);
	
	public Component getFilter(String componentId, FilterForm form) {
		return null;
	}
}
