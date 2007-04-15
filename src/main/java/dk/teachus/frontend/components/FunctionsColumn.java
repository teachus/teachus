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

import java.io.Serializable;
import java.util.List;

import wicket.behavior.SimpleAttributeModifier;
import wicket.extensions.markup.html.repeater.data.table.AbstractColumn;
import wicket.markup.html.basic.Label;
import wicket.markup.html.link.Link;
import wicket.markup.repeater.Item;
import wicket.markup.repeater.RepeatingView;
import wicket.model.IModel;
import wicket.util.string.JavascriptUtils;
import wicket.util.string.Strings;

public class FunctionsColumn extends AbstractColumn {
	private static final long serialVersionUID = 1L;

	public static abstract class FunctionItem implements Serializable {
		private String label;
		
		public FunctionItem(String label) {
			this.label = label;
		}

		public String getLabel() {
			return label;
		}

		public abstract void onEvent(Object object);
		
		public String getClickConfirmText(Object object) {
			return null;
		}
	}
	
	private List<FunctionItem> functions;
	
	public FunctionsColumn(IModel displayModel, List<FunctionItem> functions) {
		super(displayModel);
		this.functions = functions;
	}

	@SuppressWarnings("unchecked")
	public void populateItem(Item cellItem, String componentId, IModel rowModel) {
		final Object object = rowModel.getObject(cellItem);
		
		cellItem.add(new SimpleAttributeModifier("class", "functions"));
		
		LinkPropertyColumnPanel linkPropertyColumnPanel = new LinkPropertyColumnPanel(componentId);
		linkPropertyColumnPanel.setRenderBodyOnly(true);
		cellItem.add(linkPropertyColumnPanel);
		
		RepeatingView links = new RepeatingView("link");
		linkPropertyColumnPanel.add(links);
		
		for (final FunctionItem function : functions) {
			Link link = new Link(links.newChildId()) {
				private static final long serialVersionUID = 1L;

				@Override
				public void onClick() {
					function.onEvent(object);
				}
			};
			
			String clickConfirmText = function.getClickConfirmText(object);
			if (Strings.isEmpty(clickConfirmText) == false) {
				StringBuilder confirmJavascript = new StringBuilder();
				confirmJavascript.append("return confirm('");
				confirmJavascript.append(JavascriptUtils.escapeQuotes(clickConfirmText));
				confirmJavascript.append("');");
				SimpleAttributeModifier onClickModifier = new SimpleAttributeModifier("onclick", confirmJavascript.toString());
				link.add(onClickModifier);
			}
			
			link.add(new Label("label", function.getLabel()).setRenderBodyOnly(true));
			
			links.add(link);
		}
	}

}
