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

import java.util.List;

import org.apache.wicket.Component;
import org.apache.wicket.behavior.SimpleAttributeModifier;
import org.apache.wicket.extensions.markup.html.repeater.data.table.AbstractColumn;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;

public class FunctionsColumn extends AbstractColumn {
	private static final long serialVersionUID = 1L;

	private List<FunctionItem> functions;
	
	public FunctionsColumn(IModel displayModel, List<FunctionItem> functions) {
		super(displayModel);
		this.functions = functions;
	}
	
	@Override
	public Component getHeader(String componentId) {
		Component component = super.getHeader(componentId);
		component.add(new SimpleAttributeModifier("class", "functions"));
		return component;
	}

	public void populateItem(Item cellItem, String componentId, IModel rowModel) {		
		cellItem.add(new SimpleAttributeModifier("class", "functions"));
		
		cellItem.add(new FunctionColumnPanel(componentId, functions, rowModel));
		
//		LinkPropertyColumnPanel linkPropertyColumnPanel = new LinkPropertyColumnPanel(componentId);
//		linkPropertyColumnPanel.setRenderBodyOnly(true);
//		cellItem.add(linkPropertyColumnPanel);
//		
//		RepeatingView links = new RepeatingView("link");
//		linkPropertyColumnPanel.add(links);
//		
//		for (final FunctionItem function : functions) {
//			Link link = new Link(links.newChildId()) {
//				private static final long serialVersionUID = 1L;
//
//				@Override
//				public void onClick() {
//					function.onEvent(object);
//				}
//				
//				@Override
//				public boolean isEnabled() {
//					return function.isEnabled(object);
//				}
//			};
//			
//			String clickConfirmText = function.getClickConfirmText(object);
//			link.add(new ConfirmClickBehavior(clickConfirmText));
//			link.add(new AttributeModifier("title", true, new AbstractReadOnlyModel() {
//				private static final long serialVersionUID = 1L;
//
//				@Override
//				public Object getObject() {
//					return function.getTitle();
//				}
//			}));
//			function.modifyLink(link);
//
//			link.add(function.createLabelComponent("label", object));
//			
//			links.add(link);
//		}
	}

}
