package dk.teachus.frontend.components.jquery.cluetip;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.model.Model;

public class JQueryCluetipBehavior extends AbstractJQueryCluetipBehavior {
	private static final long serialVersionUID = 1L;

	public static enum Style {
		STANDARD,
		NO_HEADER
	}
	
	private final Style style;
	
	public JQueryCluetipBehavior() {
		this(null);
	}
	
	public JQueryCluetipBehavior(Style style) {
		if (style == null) {
			style = Style.STANDARD;
		}
		
		this.style = style;		
	}
	
	@Override
	public void onRenderHead(IHeaderResponse response) {
		super.onRenderHead(response);

		StringBuilder tipConf = new StringBuilder();
		tipConf.append("$('.tooltip").append(style.name()).append("').cluetip({");
		if (style == Style.STANDARD) {
			tipConf.append("splitTitle: '|'");
		} else {
			tipConf.append("splitTitle: '|',");
			tipConf.append("showTitle: false");
		}
		tipConf.append("})");
		
		response.renderOnDomReadyJavaScript(tipConf.toString());
	}
	
	@Override
	public void bind(Component component) {
		super.bind(component);

		component.add(AttributeModifier.append("class", new Model<String>("tooltip"+style.name())));
	}
	
}
