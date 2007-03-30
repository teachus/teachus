package dk.teachus.frontend.components.form;

import java.util.List;

import wicket.Component;
import wicket.feedback.ComponentFeedbackMessageFilter;
import wicket.feedback.IFeedbackMessageFilter;
import wicket.markup.html.form.DropDownChoice;
import wicket.markup.html.form.FormComponent;
import wicket.markup.html.form.IChoiceRenderer;
import wicket.markup.html.form.validation.IValidator;
import wicket.markup.html.panel.FeedbackPanel;
import wicket.model.IModel;
import wicket.model.Model;

public class DropDownElement extends AbstractChoiceElement {
	private static final long serialVersionUID = 1L;
	
	private DropDownChoice dropDownChoice;
	
	public DropDownElement(String label, IModel inputModel, List choices) {
		this(label, inputModel, choices, null, false);
	}
	
	public DropDownElement(String label, IModel inputModel, List choices, IChoiceRenderer choiceRenderer) {
		this(label, inputModel, choices, choiceRenderer, false);
	}
	
	public DropDownElement(String label, IModel inputModel, List choices, boolean required) {
		this(label, inputModel, choices, null, required);
	}
	
	public DropDownElement(String label, IModel inputModel, List choices, IChoiceRenderer choiceRenderer, boolean required) {
		super(label, inputModel, choices, choiceRenderer, required);
	}
	
	@Override
	protected IFeedbackMessageFilter getFeedbackMessageFilter() {
		return new ComponentFeedbackMessageFilter(dropDownChoice);
	}

	@Override
	protected Component newInputComponent(String wicketId, FeedbackPanel feedbackPanel) {
		DropDownElementPanel elementPanel = new DropDownElementPanel(wicketId);
		elementPanel.setRenderBodyOnly(true);
		
		dropDownChoice = new DropDownChoice("dropDown", inputModel, choices, choiceRenderer);
		dropDownChoice.setLabel(new Model(label));
		elementPanel.add(dropDownChoice);
		dropDownChoice.setRequired(required);
		dropDownChoice.setNullValid(required == false);
				
		return elementPanel;
	}
	
	@Override
	protected void addValidator(IValidator validator) {
		dropDownChoice.add(validator);
	}
	
	@Override
	public FormComponent getFormComponent() {
		return dropDownChoice;
	}

	@Override
	protected String getValidationEvent() {
		return "onchange";
	}
	
}
