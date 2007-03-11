package dk.teachus.frontend.components.form;

import wicket.markup.html.form.TextField;
import wicket.markup.html.panel.FeedbackPanel;
import wicket.model.IModel;

public class DecimalFieldElement extends TextFieldElement {
	private static final long serialVersionUID = 1L;

	public DecimalFieldElement(String label, IModel inputModel) {
		super(label, inputModel);
	}
	
	public DecimalFieldElement(String label, IModel inputModel, int size) {
		super(label, inputModel, size);
	}
	
	public DecimalFieldElement(String label, IModel inputModel, boolean required) {
		super(label, inputModel, required);
	}
	
	public DecimalFieldElement(String label, IModel inputModel, boolean required, int size) {
		super(label, inputModel, required, size);
	}

	@Override
	protected TextField getNewInputComponent(String wicketId, FeedbackPanel feedbackPanel) {
		TextField textField = super.getNewInputComponent(wicketId, feedbackPanel);
		textField.setType(Double.class);
		return textField;
	}
	
}
