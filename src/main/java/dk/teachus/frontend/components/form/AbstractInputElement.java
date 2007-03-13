package dk.teachus.frontend.components.form;

import java.util.ArrayList;
import java.util.List;

import wicket.Component;
import wicket.feedback.IFeedbackMessageFilter;
import wicket.markup.html.basic.Label;
import wicket.markup.html.form.FormComponent;
import wicket.markup.html.form.validation.IValidator;
import wicket.markup.html.panel.FeedbackPanel;

public abstract class AbstractInputElement extends FormElement {
	private static final long serialVersionUID = 1L;
	
	private boolean attached = false;
	protected boolean required;
	protected String label;
	private List<IValidator> validators = new ArrayList<IValidator>();

	protected FeedbackPanel feedbackPanel;

	public AbstractInputElement(String label) {
		this(label, false);
	}
	
	public AbstractInputElement(String label, boolean required) {		
		this.required = required;
		this.label = label;
	}
	
	public void add(IValidator validator) {
		validators.add(validator);
	}
	
	@Override
	protected void onAttach() {
		if (attached == false) {
			Component label = newLabelComponent("label");
			add(label);
			
			feedbackPanel = new FeedbackPanel("feedback");
			feedbackPanel.setOutputMarkupId(true);
			add(feedbackPanel);
			
			Component input = newInputComponent("input", feedbackPanel);
			add(input);
			
			feedbackPanel.setFilter(getFeedbackMessageFilter());
			
			for (IValidator validator : validators) {
				addValidator(validator);
			}
			
			onEndAttach();
			
			attached = true;
		}
	}
	
	protected void onEndAttach() {
	}
	
	protected Component newLabelComponent(String wicketId) {
		return new Label(wicketId, label).setRenderBodyOnly(true);
	}
	
	protected abstract Component newInputComponent(String wicketId, FeedbackPanel feedbackPanel);
	
	protected abstract IFeedbackMessageFilter getFeedbackMessageFilter();
	
	protected abstract void addValidator(IValidator validator);
	
	protected abstract FormComponent getFormComponent();

}
