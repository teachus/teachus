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
package dk.teachus.frontend.components.menu;

import java.util.List;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.WebComponent;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

import dk.teachus.frontend.pages.BasePage.PageCategory;

public class MenuItemsPanel extends Panel {
	private static final long serialVersionUID = 1L;
	
	public MenuItemsPanel(String id, IModel<List<MenuItem>> itemsModel, final IModel<PageCategory> activeMenuItemModel) {
		super(id, itemsModel);
		
		add(new ListView<MenuItem>("menuItems", itemsModel) {
			private static final long serialVersionUID = 1L;
			
			@Override
			protected void populateItem(final ListItem<MenuItem> listItem) {
				listItem.add(new AttributeAppender("class", new AbstractReadOnlyModel<String>() {
					private static final long serialVersionUID = 1L;
					
					@Override
					public String getObject() {
						PageCategory activeMenuType = activeMenuItemModel.getObject();
						PageCategory menuItemType = listItem.getModelObject().getMenuItemType();
						
						return activeMenuType == menuItemType ? "active" : null;
					}
				}));
				
				final WebMarkupContainer link;
				
				final MenuItem menuItem = listItem.getModelObject();
				if (menuItem instanceof MenuItemPageLink) {
					final MenuItemPageLink menuItemLink = (MenuItemPageLink) menuItem;
					
					link = new BookmarkablePageLink<Void>("menuLink", menuItemLink.getPageClass(), menuItemLink.getPageParameters());
					listItem.add(link);
					
					link.add(new Label("menuLabel", menuItemLink.getLabel()).setRenderBodyOnly(true));
					link.add(new WebMarkupContainer("downIcon").setVisible(false));
					
					listItem.add(new WebComponent("subMenu").setVisible(false));
				} else if (menuItem instanceof MenuItemContainer) {
					MenuItemContainer menuItemContainer = (MenuItemContainer) menuItem;
					
					listItem.add(AttributeModifier.append("class", "dropdown"));
					
					link = new WebMarkupContainer("menuLink");
					link.setOutputMarkupId(true);
					link.add(AttributeModifier.replace("href", "#"));
					link.add(AttributeModifier.replace("class", "dropdown-toggle"));
					link.add(AttributeModifier.replace("data-toggle", "dropdown"));
					link.add(AttributeModifier.replace("role", "button"));
					listItem.add(link);
					
					link.add(new Label("menuLabel", menuItemContainer.getLabel()).setRenderBodyOnly(true));
					link.add(new WebMarkupContainer("downIcon").setRenderBodyOnly(true));
					
					MenuItemsPanel subMenu = new MenuItemsPanel("subMenu", new PropertyModel<List<MenuItem>>(menuItemContainer, "subMenuItems"),
							new AbstractReadOnlyModel<PageCategory>() {
								private static final long serialVersionUID = 1L;
								
								@Override
								public PageCategory getObject() {
									return null;
								}
							});
					subMenu.add(AttributeModifier.replace("class", "dropdown-menu"));
					subMenu.add(AttributeModifier.replace("role", "menu"));
					subMenu.add(AttributeModifier.replace("aria-labelledby", new AbstractReadOnlyModel<String>() {
						private static final long serialVersionUID = 1L;
						
						@Override
						public String getObject() {
							return link.getMarkupId();
						}
					}));
					listItem.add(subMenu);
				} else {
					throw new IllegalStateException("Unknown menuItem type: " + menuItem);
				}
				
				// Icon
				WebComponent icon = new WebComponent("icon") {
					private static final long serialVersionUID = 1L;
					
					@Override
					public boolean isVisible() {
						return menuItem.getIconName() != null;
					}
				};
				icon.add(AttributeModifier.replace("class", new AbstractReadOnlyModel<String>() {
					private static final long serialVersionUID = 1L;
					
					@Override
					public String getObject() {
						return "icon-" + menuItem.getIconName();
					}
				}));
				link.add(icon);
			}
		});
	}
	
}
