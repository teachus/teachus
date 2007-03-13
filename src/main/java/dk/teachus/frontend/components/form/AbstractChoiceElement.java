package dk.teachus.frontend.components.form;

import java.util.List;

import wicket.markup.html.form.IChoiceRenderer;
import wicket.model.IModel;

public abstract class AbstractChoiceElement extends AbstractValidationInputElement {
	protected IModel inputModel;
	protected List choices;
	protected IChoiceRenderer choiceRenderer;
	
	public AbstractChoiceElement(String label, IModel inputModel, List choices) {
		this(label, inputModel, choices, null, false);
	}
	
	public AbstractChoiceElement(String label, IModel inputModel, List choices, IChoiceRenderer choiceRenderer) {
		this(label, inputModel, choices, choiceRenderer, false);
	}
	
	public AbstractChoiceElement(String label, IModel inputModel, List choices, boolean required) {
		this(label, inputModel, choices, null, required);
	}
	
	public AbstractChoiceElement(String label, IModel inputModel, List choices, IChoiceRenderer choiceRenderer, boolean required) {
		super(label, required);
		
		this.inputModel = inputModel;
		this.choices = choices;
		this.choiceRenderer = choiceRenderer;
	}
	
}
