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

import org.apache.wicket.Page;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import dk.teachus.frontend.pages.BasePage.PageCategory;

public class MenuItemPageLink implements Serializable, MenuItem {
	private static final long serialVersionUID = 1L;
	
	private PageCategory menuItemType;
	private String label;
	private Class<? extends Page> pageClass;
	private PageParameters pageParameters;
	private String icon;
	
	public MenuItemPageLink(PageCategory menuItemType, String label, Class<? extends Page> pageClass) {
		this(menuItemType, label, pageClass, null, null);
	}
	
	public MenuItemPageLink(PageCategory menuItemType, String label, Class<? extends Page> pageClass, String icon) {
		this(menuItemType, label, pageClass, null, icon);
	}
	
	public MenuItemPageLink(PageCategory menuItemType, String label, Class<? extends Page> pageClass, PageParameters pageParameters) {
		this(menuItemType, label, pageClass, pageParameters, null);
	}
	
	public MenuItemPageLink(PageCategory menuItemType, String label, Class<? extends Page> pageClass, PageParameters pageParameters, String icon) {
		this.menuItemType = menuItemType;
		this.label = label;
		this.pageClass = pageClass;
		this.pageParameters = pageParameters;
		this.icon = icon;
	}
	
	@Override
	public String getLabel() {
		return label;
	}
	
	@Override
	public PageCategory getMenuItemType() {
		return menuItemType;
	}
	
	public Class<? extends Page> getPageClass() {
		return pageClass;
	}
	
	public PageParameters getPageParameters() {
		return pageParameters;
	}
	
	public String getIconName() {
		return icon;
	}
	
	public void setIcon(String icon) {
		this.icon = icon;
	}
	
}