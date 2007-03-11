package dk.teachus.frontend.components.form;

import wicket.markup.html.form.TextField;
import wicket.markup.html.panel.FeedbackPanel;
import wicket.model.IModel;

public class IntegerFieldElement extends TextFieldElement {
	private static final long serialVersionUID = 1L;
	
	public IntegerFieldElement(String label, IModel inputModel) {
		super(label, inputModel);
	}
	
	public IntegerFieldElement(String label, IModel inputModel, int size) {
		super(label, inputModel, size);
	}
	
	public IntegerFieldElement(String label, IModel inputModel, boolean required) {
		super(label, inputModel, required);
	}
	
	public IntegerFieldElement(String label, IModel inputModel, boolean required, int size) {
		super(label, inputModel, required, size);
	}

	@Override
	protected TextField getNewInputComponent(String wicketId, FeedbackPanel feedbackPanel) {
		TextField textField = super.getNewInputComponent(wicketId, feedbackPanel);
		textField.setType(Integer.class);
		return textField;
	}
	
}
