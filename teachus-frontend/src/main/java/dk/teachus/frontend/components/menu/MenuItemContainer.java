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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import dk.teachus.frontend.pages.BasePage.PageCategory;

public class MenuItemContainer implements MenuItem, Serializable {
	private static final long serialVersionUID = 1L;
	
	private PageCategory menuItemType;
	private String label;
	private List<MenuItem> subMenuItems = new ArrayList<MenuItem>();
	private String icon;
	
	public MenuItemContainer(PageCategory menuItemType, String label) {
		this.menuItemType = menuItemType;
		this.label = label;
	}
	
	@Override
	public String getLabel() {
		return label;
	}
	
	@Override
	public PageCategory getMenuItemType() {
		return menuItemType;
	}
	
	public void addSubMenuItem(MenuItem subMenuItem) {
		subMenuItems.add(subMenuItem);
	}
	
	public List<MenuItem> getSubMenuItems() {
		return subMenuItems;
	}
	
	public String getIconName() {
		return icon;
	}
	
	public void setIcon(String icon) {
		this.icon = icon;
	}
	
}
