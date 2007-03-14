package dk.teachus.frontend.components.form;

import java.util.List;

import wicket.Component;
import wicket.feedback.ComponentFeedbackMessageFilter;
import wicket.feedback.IFeedbackMessageFilter;
import wicket.markup.html.form.CheckBoxMultipleChoice;
import wicket.markup.html.form.FormComponent;
import wicket.markup.html.form.IChoiceRenderer;
import wicket.markup.html.form.validation.IValidator;
import wicket.markup.html.panel.FeedbackPanel;
import wicket.model.IModel;
import wicket.model.Model;
import dk.teachus.frontend.components.form.ElementModifier.BehaviorAdder;

public class CheckGroupElement extends AbstractChoiceElement {
	private static final long serialVersionUID = 1L;
	private CheckBoxMultipleChoice checkGroup;
	
	public CheckGroupElement(String label, IModel inputModel, List choices) {
		this(label, inputModel, choices, null, false);
	}
	
	public CheckGroupElement(String label, IModel inputModel, List choices, IChoiceRenderer choiceRenderer) {
		this(label, inputModel, choices, choiceRenderer, false);
	}
	
	public CheckGroupElement(String label, IModel inputModel, List choices, boolean required) {
		this(label, inputModel, choices, null, required);
	}
	
	public CheckGroupElement(String label, IModel inputModel, List choices, IChoiceRenderer choiceRenderer, boolean required) {
		super(label, inputModel, choices, choiceRenderer, required);
	}

	@Override
	public FormComponent getFormComponent() {
		return checkGroup;
	}

	@Override
	protected Component newInputComponent(String wicketId, FeedbackPanel feedbackPanel) {
		checkGroup = new CheckBoxMultipleChoice(wicketId, inputModel, choices, choiceRenderer);
		checkGroup.setRequired(required);
		checkGroup.setLabel(new Model(label));
		return checkGroup;
	}

	@Override
	protected IFeedbackMessageFilter getFeedbackMessageFilter() {
		return new ComponentFeedbackMessageFilter(checkGroup);
	}

	@Override
	protected Component[] onInputInvalid(FeedbackPanel feedbackPanel) {
		return new Component[]{feedbackPanel};
	}

	@Override
	protected Component[] onInputValid(FeedbackPanel feedbackPanel) {
		return new Component[]{feedbackPanel};
	}

	@Override
	protected void addValidator(IValidator validator) {
		checkGroup.add(validator);
	}

	@Override
	protected String getValidationEvent() {
		return null; // Not needed
	}
	
	@Override
	public void add(BehaviorAdder behaviorAdder) {
		// Do nothing
	}
	
}