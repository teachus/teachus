package dk.teachus.frontend.components.form;

import wicket.markup.html.basic.Label;
import wicket.model.IModel;

public class ReadOnlyElement extends FormElement {
	private static final long serialVersionUID = 1L;
	
	public ReadOnlyElement(String label, IModel inputModel) {		
		add(new Label("label", label));
		add(new Label("readOnly", inputModel));
	}

}
