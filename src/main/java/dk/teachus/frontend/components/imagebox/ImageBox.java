package dk.teachus.frontend.components.imagebox;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
	
	public static final ResourceReference JS_JQUERY_1_2 = new JavascriptResourceReference(ImageBox.class, "resources/jquery-1.2.js"); //$NON-NLS-1$
	public static final ResourceReference JS_IUTIL_1_2 = new JavascriptResourceReference(ImageBox.class, "resources/iutil-1.2.js"); //$NON-NLS-1$
	public static final ResourceReference JS_IMAGEBOX_1_2 = new JavascriptResourceReference(ImageBox.class, "resources/imagebox-1.2.js"); //$NON-NLS-1$
	public static final ResourceReference LOADING = new ResourceReference(ImageBox.class, "resources/loading.gif"); //$NON-NLS-1$;
	public static final ResourceReference SPACER = new ResourceReference(ImageBox.class, "resources/spacer.gif"); //$NON-NLS-1$;
	public static final ResourceReference PREV = new ResourceReference(ImageBox.class, "resources/prev_image.jpg"); //$NON-NLS-1$;
	public static final ResourceReference NEXT = new ResourceReference(ImageBox.class, "resources/next_image.jpg"); //$NON-NLS-1$;
	public static final TextTemplateHeaderContributor CSS_IMAGEBOX = TextTemplateHeaderContributor.forCss(ImageBox.class, "resources/imagebox-1.2.css", new StyleSheetModel());
	
	private static final String NEWLINE = "\n";
	private static final String TAB = "\t";

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
			properties.put("loaderSRC", requestCycle.urlFor(ImageBox.LOADING));
			properties.put("closeHTML", "Close");
			properties.put("textImage", "Showing image");
			properties.put("textImageFrom", "from");
			
			StringBuilder sb = new StringBuilder();
			
			sb.append("$.ImageBox.init(").append(NEWLINE);
			sb.append("{").append(NEWLINE);
			int i = 0;
			for (String key : properties.keySet()) {
				i++;
				Object value = properties.get(key);
				sb.append(TAB).append(key).append(": ");
				
				if (value instanceof CharSequence) {
					sb.append("'").append(value).append("'");
				} else {
					sb.append(value);
				}
				
				if (i < properties.size()) {
					sb.append(",");
				}
				sb.append(NEWLINE);
			}
			sb.append("}").append(NEWLINE);
			sb.append(")");
			
			response.renderOnLoadJavascript(sb.toString());
		}
	}
	
	private static class StyleSheetModel extends LoadableDetachableModel  {
		private static final long serialVersionUID = 1L;

		@Override
		protected Object load() {
			Map<String, CharSequence> variables = new HashMap<String, CharSequence>();
			RequestCycle requestCycle = RequestCycle.get();
			
			variables.put("spacerUrl", requestCycle.urlFor(SPACER));
			variables.put("nextImageUrl", requestCycle.urlFor(NEXT));
			variables.put("prevImageUrl", requestCycle.urlFor(PREV));
			
			return variables;
		}
	}
	
	private boolean attached = false;
	
	private List<ImageResource> images;
	
	public ImageBox(String id, List<ImageResource> images) {
		super(id);
		
		add(HeaderContributor.forJavaScript(ImageBox.JS_JQUERY_1_2));
		add(HeaderContributor.forJavaScript(ImageBox.JS_IUTIL_1_2));
		add(HeaderContributor.forJavaScript(ImageBox.JS_IMAGEBOX_1_2));
		
		add(new HeaderContributor(new ImageBoxConfiguration()));
		add(CSS_IMAGEBOX);
		
		this.images = images;
	}
	
	@Override
	protected void onAttach() {
		if (attached == false) {			
			RepeatingView imagesContainer = new RepeatingView("images");
			add(imagesContainer);
			
			for (ImageResource imageResource : images) {
				ResourceReference image = imageResource.getImage();
				ResourceReference imageThumb = imageResource.getImageThumb();
				final String title = imageResource.getTitle();
				
				ExternalLink imageLink = new ExternalLink(imagesContainer.newChildId(), getRequestCycle().urlFor(image).toString());
				imageLink.add(new SimpleAttributeModifier("rel", "imagebox-"+getMarkupId()));
				imageLink.add(new AttributeModifier("title", true, new Model(title)) {
					private static final long serialVersionUID = 1L;

					@Override
					public boolean isEnabled(Component component) {
						return Strings.isEmpty(title) == false;
					}
				});
				imagesContainer.add(imageLink);
				
				imageLink.add(new Image("image", imageThumb));
			}
			
			attached = true;
		}
	}	
}
