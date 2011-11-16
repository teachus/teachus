package dk.teachus.frontend.components.list;

import org.apache.wicket.Component;
import org.apache.wicket.ResourceReference;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.Model;

import dk.teachus.frontend.components.jquery.cluetip.JQueryCluetipBehavior;
import dk.teachus.frontend.components.jquery.cluetip.JQueryCluetipBehavior.Style;

public abstract class ImageFunctionItem<T> extends DefaultFunctionItem<T> {
	private static final long serialVersionUID = 1L;
	
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
	
	@Override
	public void modifyLink(final Link<T> link) {
		link.add(new AttributeAppender("class", true, new Model<String>("imglink"), " "));
		
		link.add(new JQueryCluetipBehavior(Style.NO_HEADER) {
			private static final long serialVersionUID = 1L;
			
			@Override
			public boolean isEnabled(Component component) {
				return link.isEnabled() && getTitle(null) != null;
			}
		});
	}
	
	@Override
	public String getTitle(T rowObject) {
		return "|"+super.getTitle(rowObject);
	}
	
}
