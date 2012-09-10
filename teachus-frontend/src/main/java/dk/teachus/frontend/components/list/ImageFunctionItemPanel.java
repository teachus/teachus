package dk.teachus.frontend.components.list;

import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.request.resource.ResourceReference;

class ImageFunctionItemPanel extends Panel {
	private static final long serialVersionUID = 1L;

	public ImageFunctionItemPanel(String id, ResourceReference imageReference) {
		super(id);
		
		Image image = new Image("icon", imageReference);
		add(image);
	}
	
}
