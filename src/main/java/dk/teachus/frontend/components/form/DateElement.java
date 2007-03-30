package dk.teachus.frontend.components.form;

import wicket.Component;
import wicket.extensions.yui.calendar.DateField;
import wicket.feedback.ComponentFeedbackMessageFilter;
import wicket.feedback.IFeedbackMessageFilter;
import wicket.markup.html.form.FormComponent;
import wicket.markup.html.form.validation.IValidator;
import wicket.markup.html.panel.FeedbackPanel;
import wicket.model.IModel;
import wicket.model.Model;

public class DateElement extends AbstractValidationInputElement {
	private static final long serialVersionUID = 1L;

	private IModel inputModel;
	private DateField dateField;
	
	public DateElement(String label, IModel inputModel) {
		this(label, inputModel, false);
	}
	
	public DateElement(String label, IModel inputModel, boolean required) {
		super(label, required);
		
		this.inputModel = inputModel;
	}

	@Override
	protected void addValidator(IValidator validator) {
		dateField.add(validator);
	}

	@Override
	protected IFeedbackMessageFilter getFeedbackMessageFilter() {
		return new ComponentFeedbackMessageFilter(dateField);
	}

	@Override
	public FormComponent getFormComponent() {
		return dateField;
	}

	@Override
	protected String getValidationEvent() {
		return "onchange";
	}

	@Override
	protected Component newInputComponent(String wicketId, FeedbackPanel feedbackPanel) {
		DateElementPanel elementPanel = new DateElementPanel(wicketId);
		elementPanel.setRenderBodyOnly(true);
		
		dateField = new DateField("dateField", inputModel);
		dateField.setLabel(new Model(label));
		dateField.setRequired(required);
		elementPanel.add(dateField);
		
		return elementPanel;
	}

}
