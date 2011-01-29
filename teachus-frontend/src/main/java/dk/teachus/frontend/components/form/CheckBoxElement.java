package dk.teachus.frontend.components.form;

import org.apache.wicket.Component;
import org.apache.wicket.feedback.ComponentFeedbackMessageFilter;
import org.apache.wicket.feedback.IFeedbackMessageFilter;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.validation.IValidator;

public class CheckBoxElement extends AbstractInputElement<Boolean> {
	private static final long serialVersionUID = 1L;
	
	private CheckBox checkBox;

	private final IModel<Boolean> model;

	public CheckBoxElement(String label, IModel<Boolean> model) {
		super(label);
		this.model = model;
	}
	
	public CheckBoxElement(String label, boolean required, IModel<Boolean> model) {
		super(label, required);
		this.model = model;
	}

	@Override
	protected Component newInputComponent(String wicketId, FeedbackPanel feedbackPanel) {
		CheckBoxElementPanel panel = new CheckBoxElementPanel(wicketId);
		checkBox = new CheckBox("inputField", model);
		panel.add(checkBox);
		return panel;
	}
	
	@Override
	protected IFeedbackMessageFilter getFeedbackMessageFilter() {
		return new ComponentFeedbackMessageFilter(this);
	}
	
	@Override
	protected void addValidator(IValidator validator) {
		checkBox.add(validator);
	}
	
	@Override
	protected FormComponent getFormComponent() {
		return checkBox;
	}
	
}
