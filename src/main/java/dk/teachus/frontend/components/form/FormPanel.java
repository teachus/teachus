package dk.teachus.frontend.components.form;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import wicket.markup.html.WebMarkupContainer;
import wicket.markup.html.form.Form;
import wicket.markup.html.form.validation.IFormValidator;
import wicket.markup.html.panel.Panel;
import wicket.markup.repeater.RepeatingView;

public class FormPanel extends Panel {
	private static final long serialVersionUID = 1L;
	static final String ELEMENT_ID = "element";
	
	public static interface FormValidator extends Serializable {
		IFormValidator getFormValidator();
	}
	
	private RepeatingView elements;
	private Form form;
	private List<FormValidator> validators = new ArrayList<FormValidator>();

	public FormPanel(String id) {
		super(id);
		
		form = new Form("form");
		add(form);
		
		elements = new RepeatingView("elements");
		form.add(elements);		
	}
	
	public void addElement(FormElement element) {
		WebMarkupContainer elementContainer = new WebMarkupContainer(elements.newChildId());
		elements.add(elementContainer);
		
		elementContainer.add(element);
	}

	public void addValidator(FormValidator formValidator) {
		validators.add(formValidator);
	}
	
	@Override
	public void internalAttach() {
		super.internalAttach();
		
		for (FormValidator formValidator : validators) {
			form.add(formValidator.getFormValidator());
		}
	}
	
}
