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

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.MarkupStream;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.WebComponent;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.joda.time.DateMidnight;

import com.newrelic.api.agent.NewRelic;

import dk.teachus.backend.domain.ApplicationConfiguration;
import dk.teachus.backend.domain.Theme;
import dk.teachus.frontend.TeachUsApplication;
import dk.teachus.frontend.TeachUsSession;
import dk.teachus.frontend.components.jquery.cluetip.JQueryCluetipBehavior;

public abstract class BasePage extends WebPage {
	private static final long serialVersionUID = 1L;

	public static interface PageCategory {
	}

	boolean attached = false;
	private WebMarkupContainer ajaxLoader;
	private Theme theme;
	
	public BasePage() {
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
		
		ajaxLoader = new WebMarkupContainer("ajaxLoader");
		ajaxLoader.setOutputMarkupId(true);
		add(ajaxLoader);
		
		/*
		 * Google Analytics
		 */
		WebMarkupContainer googleAnalytics = new WebMarkupContainer("googleAnalytics") {
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isVisible() {
				return TeachUsApplication.get().getConfiguration().hasConfiguration(ApplicationConfiguration.GOOGLE_ANALYTICS_WEB_PROPERTY_ID);
			}
			
			@Override
			public void onComponentTagBody(MarkupStream markupStream, ComponentTag openTag) {
				String content = markupStream.get().toCharSequence().toString();
				
				content = content.replace("UA-XXXXX-X", TeachUsApplication.get().getConfiguration().getConfiguration(ApplicationConfiguration.GOOGLE_ANALYTICS_WEB_PROPERTY_ID));
				
				replaceComponentTagBody(markupStream, openTag, content);
			}
		};
		add(googleAnalytics);

		/*
		 * New Relic
		 */
		add(new Label("newRelicTimingHeader", NewRelic.getBrowserTimingHeader()).setEscapeModelStrings(false).setRenderBodyOnly(true));
		add(new Label("newRelicTimingFooter", new AbstractReadOnlyModel<String>() {
			private static final long serialVersionUID = 1L;

			@Override
			public String getObject() {
				return NewRelic.getBrowserTimingFooter();
			}
		}).setEscapeModelStrings(false).setRenderBodyOnly(true));
	}
	
	@Override
	public void renderHead(IHeaderResponse response) {
		StringBuilder b = new StringBuilder();
		
		b.append("wicketGlobalPreCallHandler = function() {").append("\n");
		b.append("\t").append("wicketShow('"+ajaxLoader.getMarkupId()+"');").append("\n");
		b.append("}").append("\n");
		
		b.append("wicketGlobalPostCallHandler = function() {").append("\n");
		b.append("\t").append("wicketHide('"+ajaxLoader.getMarkupId()+"');").append("\n");
		b.append("}").append("\n");
		
		response.renderJavaScript(b, "ajaxLoadingIndicator");
		
		response.renderCSSReference(JQueryCluetipBehavior.CSS_CLUETIP_JQUERY);
	}
	
	private void setTheme(Theme theme) {
		this.theme = theme;
	}

	private void createMenu() {
		add(new BookmarkablePageLink<Void>("brandLink", TeachUsApplication.get().getHomePage()));
		
		addMenu("menuItems", new PropertyModel<List<MenuItem>>(this, "menuItems"));
		addMenu("rightMenuItems", new PropertyModel<List<MenuItem>>(this, "rightMenuItems"));
	}
	
	private void addMenu(String wicketId, IModel<List<MenuItem>> menuItemsModel) {
		add(new ListView<MenuItem>(wicketId, menuItemsModel) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(ListItem<MenuItem> item) {
				MenuItem menuItem = item.getModelObject();
				Link<Void> menuLink = new BookmarkablePageLink<Void>("menuLink", menuItem.getBookmarkablePage());
				item.add(menuLink);
				
				if (menuItem.getPageCategory().equals(getPageCategory())) {
					item.add(AttributeModifier.replace("class", "active"));
				}
				
				if (menuItem.getIcon() == null) {
					menuLink.add(new Label("menuLabel", menuItem.getHelpText()));
				} else {
					WebComponent icon = new WebComponent("menuLabel");
					menuLink.add(AttributeModifier.replace("title", menuItem.getHelpText()));
					menuLink.add(AttributeModifier.replace("rel", "tooltip"));
					menuLink.add(AttributeModifier.replace("data-placement", "bottom"));
					icon.add(AttributeModifier.replace("class", "icon-"+menuItem.getIcon()));
					menuLink.add(icon);
				}
			}
		});
	}

	@Override
	protected void onBeforeRender() {
		super.onBeforeRender();
		
		if (attached == false) {
			NewRelic.setTransactionName(null, getPagePath());

			attached = true;
		}
	}
	
	protected Theme getTheme() {
		return TeachUsApplication.get().getDefaultTheme();
	}

	protected abstract String getPagePath();

	protected abstract PageCategory getPageCategory();
	
	public abstract List<MenuItem> getMenuItems();
	
	public abstract List<MenuItem> getRightMenuItems();
}
