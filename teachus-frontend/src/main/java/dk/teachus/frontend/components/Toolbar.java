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

import org.apache.wicket.Component;
import org.apache.wicket.Page;
import org.apache.wicket.PageParameters;
import org.apache.wicket.behavior.SimpleAttributeModifier;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.RepeatingView;

public class Toolbar extends Panel {
	private static final long serialVersionUID = 1L;

	public Toolbar(String id, List<ToolbarItemInterface> items) {
		super(id);
		
		RepeatingView itemsContainer = new RepeatingView("items"); //$NON-NLS-1$
		add(itemsContainer);
		
		for (final ToolbarItemInterface item : items) {
			WebMarkupContainer itemContainer = new WebMarkupContainer(itemsContainer.newChildId());
			itemsContainer.add(itemContainer);
			
			Link link = null;
			if (item instanceof ToolbarItem) {
				final ToolbarItem toolbarItem = (ToolbarItem) item;
				link = new Link("link") {
					private static final long serialVersionUID = 1L;

					@Override
					public void onClick() {
						toolbarItem.onEvent();
					}				
				};
			} else if (item instanceof BookmarkableToolbarItem) {
				BookmarkableToolbarItem bookmarkableToolbarItem = (BookmarkableToolbarItem) item;
				link = new BookmarkablePageLink("link", bookmarkableToolbarItem.getPageClass(), bookmarkableToolbarItem.getPageParameters());
			} else {
				throw new IllegalArgumentException("Toolbar item not supported: "+item.getClass().getName());
			}
			
			link.add(new SimpleAttributeModifier("class", "current") {
				private static final long serialVersionUID = 1L;
				
				@Override
				public boolean isEnabled(Component component) {
					return item.isCurrent(Toolbar.this.getPage());
				}
			});
			itemContainer.add(link);
			link.add(new Label("label", item.getLabel()).setRenderBodyOnly(true)); //$NON-NLS-1$
		}
	}
	
	public static interface ToolbarItemInterface extends Serializable {
		String getLabel();
		
		boolean isCurrent(Page page);
	}
	
	private abstract static class AbstractToolbarItem implements ToolbarItemInterface {
		private String label;
		
		public AbstractToolbarItem(String label) {
			this.label = label;
		}

		public String getLabel() {
			return label;
		}

		public void setLabel(String label) {
			this.label = label;
		}
		
		public abstract boolean isCurrent(Page page);
	}
	
	public abstract static class ToolbarItem extends AbstractToolbarItem {
		private boolean current;
		
		public ToolbarItem(String label) {
			super(label);
		}

		public ToolbarItem(String label, boolean current) {
			super(label);
			this.current = current;
		}

		public boolean isCurrent(Page page) {
			return current;
		}

		public void setCurrent(boolean current) {
			this.current = current;
		}

		public abstract void onEvent();
	}
	
	public static class BookmarkableToolbarItem extends AbstractToolbarItem {
		private static final long serialVersionUID = 1L;

		private Class<? extends Page> pageClass;
		private PageParameters pageParameters;
		
		public BookmarkableToolbarItem(String label, Class<? extends Page> pageClass) {
			super(label);
			this.pageClass = pageClass;
		}

		public BookmarkableToolbarItem(String label, Class<? extends Page> pageClass, PageParameters pageParameters) {
			super(label);
			this.pageClass = pageClass;
			this.pageParameters = pageParameters;
		}

		public Class<? extends Page> getPageClass() {
			return pageClass;
		}

		public PageParameters getPageParameters() {
			return pageParameters;
		}

		@Override
		public boolean isCurrent(Page page) {
			return pageClass.isInstance(page);
		}
		
	}

}
