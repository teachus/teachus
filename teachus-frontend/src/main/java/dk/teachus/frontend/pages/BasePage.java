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

import java.util.List;

import org.apache.wicket.behavior.HeaderContributor;
import org.apache.wicket.behavior.SimpleAttributeModifier;
import org.apache.wicket.markup.html.IHeaderContributor;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.joda.time.DateMidnight;

import dk.teachus.backend.domain.Theme;
import dk.teachus.frontend.TeachUsApplication;
import dk.teachus.frontend.TeachUsSession;
import dk.teachus.frontend.utils.Resources;

public abstract class BasePage extends WebPage {
	private static final long serialVersionUID = 1L;

	public static interface PageCategory {
	}

	boolean attached = false;
	
	public BasePage() {
		add(HeaderContributor.forCss(Resources.CSS_ANDREAS09));
		add(HeaderContributor.forCss(Resources.CSS_SCREEN));
		add(HeaderContributor.forCss(Resources.CSS_PRINT, "print"));
		
		Theme theme = getTheme();
		
		if (theme == null) {
			theme = Theme.BLUE;
		}
		
		setTheme(theme);
		
		StringBuilder sb = new StringBuilder(TeachUsSession.get().getString("General.teachUsTitle")); //$NON-NLS-1$
		sb.append(" "); //$NON-NLS-1$
		sb.append(TeachUsApplication.get().getVersion());
		add(new Label("title", sb.toString())); //$NON-NLS-1$
		
		createMenu();		
		
		add(new Label("copyright", "2006-"+new DateMidnight().getYear()+" TeachUs Booking Systems"));
		
		final WebMarkupContainer ajaxLoader = new WebMarkupContainer("ajaxLoader");
		ajaxLoader.setOutputMarkupId(true);
		add(ajaxLoader);
		
		ajaxLoader.add(new Image("loadingImage", Resources.DOT_INDICATOR));
		
		add(new HeaderContributor(new IHeaderContributor() {
			private static final long serialVersionUID = 1L;

			public void renderHead(IHeaderResponse response) {
				StringBuilder b = new StringBuilder();
				
				b.append("wicketGlobalPreCallHandler = function() {").append("\n");
				b.append("\t").append("wicketShow('"+ajaxLoader.getMarkupId()+"');").append("\n");
				b.append("}").append("\n");
				
				b.append("wicketGlobalPostCallHandler = function() {").append("\n");
				b.append("\t").append("wicketHide('"+ajaxLoader.getMarkupId()+"');").append("\n");
				b.append("}").append("\n");
				
				response.renderJavascript(b, "ajaxLoadingIndicator");
			}
			
		}));
	}
	
	private void setTheme(Theme theme) {
		switch (theme) {
			case BLUE:
				break;
			case RED:
				add(HeaderContributor.forCss(Resources.CSS_ANDREAS09_RED));
				break;
			case ORANGE:
				add(HeaderContributor.forCss(Resources.CSS_ANDREAS09_ORANGE));
				break;
			case BLACK:
				add(HeaderContributor.forCss(Resources.CSS_ANDREAS09_BLACK));
				break;
			case GREEN:
				add(HeaderContributor.forCss(Resources.CSS_ANDREAS09_GREEN));
				break;
			case PURPLE:
				add(HeaderContributor.forCss(Resources.CSS_ANDREAS09_PURPLE));
				break;
		}
	}

	private void createMenu() {
		RepeatingView menuItems = new RepeatingView("menuItems"); //$NON-NLS-1$
		add(menuItems);
		
		List<MenuItem> menuItemsList = createMenuItems();
		if (menuItemsList != null) {
			for (MenuItem menuItem : menuItemsList) {
				WebMarkupContainer menuItemContainer = new WebMarkupContainer(menuItems.newChildId());
				menuItems.add(menuItemContainer);
				
				Link menuLink = new BookmarkablePageLink("menuLink", menuItem.getBookmarkablePage());
				menuItemContainer.add(menuLink);
				
				if (menuItem.getPageCategory().equals(getPageCategory())) {
					menuLink.add(new SimpleAttributeModifier("class", "current"));
				}
				
				menuLink.add(new Label("menuLabel", menuItem.getHelpText()));
			}
		}
	}

	@Override
	protected final void onBeforeRender() {
		super.onBeforeRender();
		
		if (attached == false) {
			add(new Label("pageLabel", getPageLabel())); //$NON-NLS-1$
			attached = true;
		}
	}
	
	protected Theme getTheme() {
		return TeachUsApplication.get().getDefaultTheme();
	}

	protected abstract String getPageLabel();

	protected abstract PageCategory getPageCategory();
	
	protected abstract List<MenuItem> createMenuItems();
}
