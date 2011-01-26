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
package dk.teachus.frontend.components.fancybox;

import java.io.Serializable;
import java.util.List;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ResourceReference;
import org.apache.wicket.behavior.HeaderContributor;
import org.apache.wicket.markup.html.IHeaderContributor;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.AbstractReadOnlyModel;

public class FancyBox extends Panel {
	private static final long serialVersionUID = 1L;

	public static class ImageResource implements Serializable {
		private static final long serialVersionUID = 1L;

		private ResourceReference image;
		
		private ResourceReference imageThumb;
		
		private String title;

		public ImageResource(ResourceReference image, ResourceReference imageThumb) {
			this.image = image;
			this.imageThumb = imageThumb;
		}

		public ImageResource(ResourceReference image, ResourceReference imageThumb, String title) {
			this.image = image;
			this.imageThumb = imageThumb;
			this.title = title;
		}

		public ResourceReference getImage() {
			return image;
		}

		public void setImage(ResourceReference image) {
			this.image = image;
		}

		public ResourceReference getImageThumb() {
			return imageThumb;
		}

		public void setImageThumb(ResourceReference imageThumb) {
			this.imageThumb = imageThumb;
		}

		public String getTitle() {
			return title;
		}

		public void setTitle(String title) {
			this.title = title;
		}
		
	}
	
	private class ImageBoxConfiguration implements IHeaderContributor {
		private static final long serialVersionUID = 1L;
		
		public void renderHead(IHeaderResponse response) {			
			response.renderOnDomReadyJavascript("$('a.fancybox').fancybox({titlePosition:'over'})");
		}
	}
	
	public FancyBox(String id, List<ImageResource> images) {
		super(id);
		
		setOutputMarkupId(true);
		
		add(new JQueryFancyboxBehavior());
		add(new HeaderContributor(new ImageBoxConfiguration()));
		add(JQueryFancyboxBehavior.CSS_FANCYBOX);
	
		add(new ListView<ImageResource>("images", images) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(final ListItem<ImageResource> item) {
				item.add(new AttributeModifier("rel", true, new AbstractReadOnlyModel<String>() {
					private static final long serialVersionUID = 1L;

					@Override
					public String getObject() {
						return FancyBox.this.getMarkupId()+"-imageGroup";
					}
				}));

				item.add(new AttributeModifier("href", true, new AbstractReadOnlyModel<CharSequence>() {
					private static final long serialVersionUID = 1L;

					@Override
					public CharSequence getObject() {
						return getRequestCycle().urlFor(item.getModelObject().getImage());
					}
				}));

				item.add(new AttributeModifier("title", true, new AbstractReadOnlyModel<CharSequence>() {
					private static final long serialVersionUID = 1L;

					@Override
					public CharSequence getObject() {
						return item.getModelObject().getTitle();
					}
				}));
				
				item.add(new Image("image", item.getModelObject().getImageThumb()));
			}
		});
	}
	
}
