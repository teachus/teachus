package dk.teachus.frontend.components.form;

import wicket.Component;
import wicket.extensions.yui.calendar.DateField;
import wicket.feedback.ComponentFeedbackMessageFilter;
import wicket.feedback.IFeedbackMessageFilter;
import wicket.markup.html.form.FormComponent;
import wicket.markup.html.form.validation.IValidator;
import wicket.markup.html.image.Image;
import wicket.markup.html.panel.FeedbackPanel;
import wicket.model.AbstractReadOnlyModel;
import wicket.model.IModel;
import wicket.model.Model;
import dk.teachus.frontend.utils.Resources;

public class DateElement extends AbstractValidationInputElement {
	private static final long serialVersionUID = 1L;

	private class StateModel extends AbstractReadOnlyModel {
		private static final long serialVersionUID = 1L;

		@Override
		public Object getObject(Component component) {
			return valid ? Resources.PAID : Resources.UNPAID;
		}		
	}

	private IModel inputModel;
	private DateField dateField;
	
	private boolean valid;
	private Image state;

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
		
		state = new Image("state", new StateModel());
		state.setOutputMarkupId(true);
		elementPanel.add(state);
		
		return elementPanel;
	}

	@Override
	protected Component[] onInputInvalid(FeedbackPanel feedbackPanel) {
		valid = false;
		return new Component[] {state, feedbackPanel};
	}

	@Override
	protected Component[] onInputValid(FeedbackPanel feedbackPanel) {
		valid = true;
		return new Component[] {state, feedbackPanel};
	}

}
