package dk.frankbille.teachus.frontend.components;

import wicket.markup.ComponentTag;
import wicket.markup.html.image.NonCachingImage;
import dk.frankbille.teachus.frontend.resources.JFreeChartResource;

public class JFreeChartImage extends NonCachingImage {
	private static final long serialVersionUID = 1L;

	private JFreeChartResource jFreeChartResource;
	
	public JFreeChartImage(String id, JFreeChartResource jFreeChartResource) {
		super(id, jFreeChartResource);
		this.jFreeChartResource = jFreeChartResource;
	}
	
	@Override
	protected void onComponentTag(ComponentTag tag) {
		super.onComponentTag(tag);
		
		tag.put("width", jFreeChartResource.getWidth());
		tag.put("height", jFreeChartResource.getHeight());
	}

}
