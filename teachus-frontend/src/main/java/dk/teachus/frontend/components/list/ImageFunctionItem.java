package dk.teachus.frontend.components.list;

import org.apache.wicket.Component;
import org.apache.wicket.ResourceReference;

public abstract class ImageFunctionItem extends FunctionItem {
	private ResourceReference imageReference;

	public ImageFunctionItem(ResourceReference imageReference) {
		this.imageReference = imageReference;
	}
	
	public ImageFunctionItem(ResourceReference imageReference, String title) {
		super(title);
		this.imageReference = imageReference;
	}

	@Override
	public Component createLabelComponent(String wicketId, Object object) {
		return new ImageFunctionItemPanel(wicketId, imageReference).setRenderBodyOnly(true);
	}
	
}
