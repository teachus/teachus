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
package dk.teachus.frontend.pages;

import java.io.Serializable;

import org.apache.wicket.markup.html.WebPage;

import dk.teachus.frontend.pages.BasePage.PageCategory;

public class MenuItem implements Serializable {
	private static final long serialVersionUID = 1L;
	
	public static MenuItem createWithText(Class<? extends WebPage> bookmarkablePage, String helpText, PageCategory pageCategory) {
		return new MenuItem(bookmarkablePage, helpText, null, pageCategory);
	}
	
	public static MenuItem createWithIcon(Class<? extends WebPage> bookmarkablePage, String helpText, String icon, PageCategory pageCategory) {
		return new MenuItem(bookmarkablePage, helpText, icon, pageCategory);
	}

	private Class<? extends WebPage> bookmarkablePage;

	private String helpText;
	
	private String icon;
	
	private PageCategory pageCategory;

	private MenuItem(Class<? extends WebPage> bookmarkablePage, String helpText, String icon, PageCategory pageCategory) {
		this.bookmarkablePage = bookmarkablePage;
		this.helpText = helpText;
		this.icon = icon;
		this.pageCategory = pageCategory;
	}

	public Class<? extends WebPage> getBookmarkablePage() {
		return bookmarkablePage;
	}

	public String getHelpText() {
		return helpText;
	}
	
	public String getIcon() {
		return icon;
	}

	public PageCategory getPageCategory() {
		return pageCategory;
	}
}
