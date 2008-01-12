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

import org.apache.wicket.extensions.markup.html.repeater.data.table.AbstractColumn;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

import dk.teachus.frontend.components.RenderingLabel;

public class RendererPropertyColumn extends AbstractColumn {
	private static final long serialVersionUID = 1L;
	
	private IChoiceRenderer renderer;
	private String propertyExpressions;

	public RendererPropertyColumn(IModel displayModel, String propertyExpressions) {
		this(displayModel, null, propertyExpressions, null);
	}

	public RendererPropertyColumn(IModel displayModel, String sortProperty, String propertyExpressions) {
		this(displayModel, sortProperty, propertyExpressions, null);
	}
	
	public RendererPropertyColumn(IModel displayModel, String propertyExpressions, IChoiceRenderer renderer) {
		this(displayModel, null, propertyExpressions, renderer);
	}
	
	public RendererPropertyColumn(IModel displayModel, String sortProperty, String propertyExpressions, IChoiceRenderer renderer) {
		super(displayModel, sortProperty);
		this.renderer = renderer;
		this.propertyExpressions = propertyExpressions;
	}

	public void populateItem(Item cellItem, String componentId, IModel rowModel) {
		RenderingLabel renderingLabel = new RenderingLabel(componentId, new PropertyModel(rowModel, propertyExpressions), renderer);
		renderingLabel.setRenderBodyOnly(true);
		cellItem.add(renderingLabel);
	}
}
