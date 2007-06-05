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
package dk.teachus.frontend.components.imagebox;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dk.teachus.frontend.TeachUsSession;
import dk.teachus.frontend.components.jquery.JQueryBehavior;

import wicket.AttributeModifier;
import wicket.Component;
import wicket.RequestCycle;
import wicket.ResourceReference;
import wicket.behavior.HeaderContributor;
import wicket.behavior.SimpleAttributeModifier;
import wicket.extensions.util.resource.TextTemplateHeaderContributor;
import wicket.markup.html.IHeaderContributor;
import wicket.markup.html.IHeaderResponse;
import wicket.markup.html.image.Image;
import wicket.markup.html.link.ExternalLink;
import wicket.markup.html.panel.Panel;
import wicket.markup.html.resources.JavascriptResourceReference;
import wicket.markup.repeater.RepeatingView;
import wicket.model.LoadableDetachableModel;
import wicket.model.Model;
import wicket.util.string.Strings;

public class ImageBox extends Panel {
	private static final long serialVersionUID = 1L;
	
	public static final ResourceReference JS_IUTIL_1_2 = new JavascriptResourceReference(ImageBox.class, "resources/iutil-1.2.js"); //$NON-NLS-1$
	public static final ResourceReference JS_IMAGEBOX_1_2 = new JavascriptResourceReference(ImageBox.class, "resources/imagebox-1.2.js"); //$NON-NLS-1$
	public static final ResourceReference LOADING = new ResourceReference(ImageBox.class, "resources/loading.gif"); //$NON-NLS-1$;
	public static final ResourceReference SPACER = new ResourceReference(ImageBox.class, "resources/spacer.gif"); //$NON-NLS-1$;
	public static final ResourceReference PREV = new ResourceReference(ImageBox.class, "resources/prev_image.jpg"); //$NON-NLS-1$;
	public static final ResourceReference NEXT = new ResourceReference(ImageBox.class, "resources/next_image.jpg"); //$NON-NLS-1$;
	public static final TextTemplateHeaderContributor CSS_IMAGEBOX = TextTemplateHeaderContributor.forCss(ImageBox.class, "resources/imagebox-1.2.css", new StyleSheetModel()); //$NON-NLS-1$
	
	private static final String NEWLINE = "\n"; //$NON-NLS-1$
	private static final String TAB = "\t"; //$NON-NLS-1$

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
	
	private static class ImageBoxConfiguration implements IHeaderContributor {
		private static final long serialVersionUID = 1L;

		public void renderHead(IHeaderResponse response) {
			RequestCycle requestCycle = RequestCycle.get();
			
			Map<String, Object> properties = new HashMap<String, Object>();
			properties.put("loaderSRC", requestCycle.urlFor(ImageBox.LOADING)); //$NON-NLS-1$
			properties.put("closeHTML", TeachUsSession.get().getString("ImageBox.close")); //$NON-NLS-1$ //$NON-NLS-2$
			properties.put("textImage", TeachUsSession.get().getString("ImageBox.showingImage")); //$NON-NLS-1$ //$NON-NLS-2$
			properties.put("textImageFrom", TeachUsSession.get().getString("ImageBox.from")); //$NON-NLS-1$ //$NON-NLS-2$
			
			StringBuilder sb = new StringBuilder();
			
			sb.append("$.ImageBox.init(").append(NEWLINE); //$NON-NLS-1$
			sb.append("{").append(NEWLINE); //$NON-NLS-1$
			int i = 0;
			for (String key : properties.keySet()) {
				i++;
				Object value = properties.get(key);
				sb.append(TAB).append(key).append(": "); //$NON-NLS-1$
				
				if (value instanceof CharSequence) {
					sb.append("'").append(value).append("'"); //$NON-NLS-1$ //$NON-NLS-2$
				} else {
					sb.append(value);
				}
				
				if (i < properties.size()) {
					sb.append(","); //$NON-NLS-1$
				}
				sb.append(NEWLINE);
			}
			sb.append("}").append(NEWLINE); //$NON-NLS-1$
			sb.append(")"); //$NON-NLS-1$
			
			response.renderOnLoadJavascript(sb.toString());
		}
	}
	
	private static class StyleSheetModel extends LoadableDetachableModel  {
		private static final long serialVersionUID = 1L;

		@Override
		protected Object load() {
			Map<String, CharSequence> variables = new HashMap<String, CharSequence>();
			RequestCycle requestCycle = RequestCycle.get();
			
			variables.put("spacerUrl", requestCycle.urlFor(SPACER)); //$NON-NLS-1$
			variables.put("nextImageUrl", requestCycle.urlFor(NEXT)); //$NON-NLS-1$
			variables.put("prevImageUrl", requestCycle.urlFor(PREV)); //$NON-NLS-1$
			
			return variables;
		}
	}
	
	private boolean attached = false;
	
	private List<ImageResource> images;
	
	public ImageBox(String id, List<ImageResource> images) {
		super(id);
		
		add(new JQueryBehavior());
		add(HeaderContributor.forJavaScript(ImageBox.JS_IUTIL_1_2));
		add(HeaderContributor.forJavaScript(ImageBox.JS_IMAGEBOX_1_2));
		
		add(new HeaderContributor(new ImageBoxConfiguration()));
		add(CSS_IMAGEBOX);
		
		this.images = images;
	}
	
	@Override
	protected void onAttach() {
		if (attached == false) {			
			RepeatingView imagesContainer = new RepeatingView("images"); //$NON-NLS-1$
			add(imagesContainer);
			
			for (ImageResource imageResource : images) {
				ResourceReference image = imageResource.getImage();
				ResourceReference imageThumb = imageResource.getImageThumb();
				final String title = imageResource.getTitle();
				
				ExternalLink imageLink = new ExternalLink(imagesContainer.newChildId(), getRequestCycle().urlFor(image).toString());
				imageLink.add(new SimpleAttributeModifier("rel", "imagebox-"+getMarkupId())); //$NON-NLS-1$ //$NON-NLS-2$
				imageLink.add(new AttributeModifier("title", true, new Model(title)) { //$NON-NLS-1$
					private static final long serialVersionUID = 1L;

					@Override
					public boolean isEnabled(Component component) {
						return Strings.isEmpty(title) == false;
					}
				});
				imagesContainer.add(imageLink);
				
				imageLink.add(new Image("image", imageThumb)); //$NON-NLS-1$
			}
			
			attached = true;
		}
	}	
}
