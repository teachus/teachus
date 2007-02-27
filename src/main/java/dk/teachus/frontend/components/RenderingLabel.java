package dk.teachus.frontend.components;

import wicket.Component;
import wicket.markup.html.basic.Label;
import wicket.markup.html.form.IChoiceRenderer;
import wicket.model.AbstractReadOnlyModel;
import wicket.model.IModel;
import wicket.util.string.Strings;

public class RenderingLabel extends Label {
	private static final long serialVersionUID = 1L;

	private RenderingModel renderingModel;
	private IChoiceRenderer renderer;
	
	public RenderingLabel(String id, IChoiceRenderer renderer) {
		super(id);
		this.renderer = renderer;
	}
	
	public RenderingLabel(String id, IModel model, IChoiceRenderer renderer) {
		super(id);
		renderingModel = new RenderingModel(model);
		this.renderer = renderer;
	}
	
	@Override
	protected IModel initModel() {
		if (renderingModel == null) {
			IModel nestedModel = super.initModel();
			renderingModel = new RenderingModel(nestedModel);
		}
		
		return renderingModel;
	}
	
	private class RenderingModel extends AbstractReadOnlyModel {
		private static final long serialVersionUID = 1L;

		private IModel nestedModel;
		
		private RenderingModel(IModel nestedModel) {
			this.nestedModel = nestedModel;
		}

		@Override
		public Object getObject(Component component) {
			Object displayValue;
			if (renderer != null) {
				displayValue = renderer.getDisplayValue(nestedModel.getObject(component));
			} else {
				displayValue = nestedModel.getObject(component);
			}
			
			if (displayValue == null || Strings.isEmpty(displayValue.toString())) {
				displayValue = "&nbsp;";
				component.setEscapeModelStrings(false);
			}
			
			return displayValue;
		}
		
	}

}
