package dk.teachus.frontend.components.form;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.basic.MultiLineLabel;
import org.apache.wicket.model.IModel;

public class MultiLineReadOnlyElement extends FormElement {
	private static final long serialVersionUID = 1L;

	public MultiLineReadOnlyElement(String label, IModel inputModel) {
		add(new Label("label", label));
		add(new MultiLineLabel("readOnly", inputModel));
	}
	
}
