package dk.teachus.frontend.components.form;

import wicket.Component;
import wicket.markup.html.panel.FeedbackPanel;
import dk.teachus.frontend.components.form.ElementModifier.BehaviorAdder;


public abstract class AbstractValidationInputElement extends AbstractInputElement implements ValidationProducer {

	public AbstractValidationInputElement(String label) {
		super(label);
	}
	
	public AbstractValidationInputElement(String label, boolean required) {
		super(label, required);
	}

	public void add(BehaviorAdder behaviorAdder) {
		getFormComponent().add(behaviorAdder.createNewBehavior());
	}
	
	public boolean hasError() {
		return getFormComponent().hasErrorMessage();
	}
	
	public final Component[] inputValid() {
		return onInputValid(feedbackPanel);
	}

	public final Component[] inputInvalid() {
		return onInputInvalid(feedbackPanel);
	}
	
	@Override
	protected void onEndAttach() {
		add(new ElementModifier(getValidationEvent()));
	}
	
	protected abstract String getValidationEvent();

	public Component[] onInputValid(FeedbackPanel feedbackPanel) {
		return new Component[] {feedbackPanel};
	}

	public Component[] onInputInvalid(FeedbackPanel feedbackPanel) {
		return new Component[] {feedbackPanel};
	}


}
