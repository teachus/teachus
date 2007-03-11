package dk.teachus.frontend.components.form;

import wicket.Component;
import wicket.behavior.SimpleAttributeModifier;
import wicket.feedback.ComponentFeedbackMessageFilter;
import wicket.feedback.IFeedbackMessageFilter;
import wicket.markup.html.form.FormComponent;
import wicket.markup.html.form.TextField;
import wicket.markup.html.form.validation.IValidator;
import wicket.markup.html.panel.FeedbackPanel;
import wicket.model.IModel;
import wicket.model.Model;
import dk.teachus.frontend.components.TextFieldErrorModifier;

public class TextFieldElement extends AbstractInputElement {
	private static final long serialVersionUID = 1L;

	private IModel inputModel;
	private int size;
	private TextField textField;

	public TextFieldElement(String label, IModel inputModel) {
		this(label, inputModel, false);
	}
	
	public TextFieldElement(String label, IModel inputModel, int size) {
		this(label, inputModel, false, size);
	}
	
	public TextFieldElement(String label, IModel inputModel, boolean required) {
		this(label, inputModel, required, -1);
	}
	
	public TextFieldElement(String label, IModel inputModel, boolean required, int size) {
		super(label, required);
		this.inputModel = inputModel;
		this.size = size;
	}
	
	@Override
	protected final Component newInputComponent(String wicketId, FeedbackPanel feedbackPanel) {
		TextFieldElementPanel inputPanel = new TextFieldElementPanel(wicketId);
		inputPanel.setRenderBodyOnly(true);
		
		textField = getNewInputComponent("inputField", feedbackPanel);
		textField.setLabel(new Model(label));
		inputPanel.add(textField);
		textField.setRequired(required);
		
		return inputPanel;
	}
	
	@Override
	protected IFeedbackMessageFilter getFeedbackMessageFilter() {
		return new ComponentFeedbackMessageFilter(textField);
	}

	protected TextField getNewInputComponent(String wicketId, FeedbackPanel feedbackPanel) {
		TextField textField = new TextField(wicketId, inputModel);
		textField.add(new TextFieldErrorModifier(feedbackPanel, "onchange"));
		if (size > -1) {
			textField.add(new SimpleAttributeModifier("size", ""+size));
		}
		return textField;
	}
	
	@Override
	protected void addValidator(IValidator validator) {
		textField.add(validator);
	}
	
	@Override
	public FormComponent getFormComponent() {
		return textField;
	}
	
}
