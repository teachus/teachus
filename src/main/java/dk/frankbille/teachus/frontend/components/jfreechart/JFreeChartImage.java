package dk.frankbille.teachus.frontend.components.jfreechart;

import wicket.markup.ComponentTag;
import wicket.markup.html.image.NonCachingImage;

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
		
		tag.put("width", jFreeChartResource.getWidth()); //$NON-NLS-1$
		tag.put("height", jFreeChartResource.getHeight()); //$NON-NLS-1$
	}

}
