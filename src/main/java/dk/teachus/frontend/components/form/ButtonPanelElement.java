package dk.teachus.frontend.components.form;

import java.util.HashMap;
import java.util.Map;

import wicket.Component;
import wicket.ajax.AjaxEventBehavior;
import wicket.ajax.AjaxRequestTarget;
import wicket.ajax.markup.html.form.AjaxSubmitButton;
import wicket.behavior.SimpleAttributeModifier;
import wicket.markup.html.form.Button;
import wicket.markup.html.form.Form;
import dk.teachus.frontend.TeachUsSession;

public abstract class ButtonPanelElement extends FormElement {
	private static final long serialVersionUID = 1L;
	
	private Map<ValidationProducer, Boolean> errorStates = new HashMap<ValidationProducer, Boolean>();
	
	private Form associatedForm;

	public ButtonPanelElement() {
		this(TeachUsSession.get().getString("General.save"));
	}
	
	public ButtonPanelElement(String submitLabel) {
		Button cancelButton = new Button("cancelButton");
		cancelButton.add(new SimpleAttributeModifier("value", TeachUsSession.get().getString("PeriodPanel.regretInput")));
		cancelButton.add(new AjaxEventBehavior("onclick") {
			private static final long serialVersionUID = 1L;

			@Override
			protected void onEvent(AjaxRequestTarget target) {
				onCancel(target);
			}			
		});
		add(cancelButton);
		
		AjaxSubmitButton saveButton = new AjaxSubmitButton("saveButton") {
			private static final long serialVersionUID = 1L;

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form form) {
				onSave(target);
			}
			
			@Override
			protected void onError(final AjaxRequestTarget target, Form form) {
				associatedForm.visitChildren(ValidationProducer.class, new wicket.Component.IVisitor() {
					public Object component(Component component) {
						ValidationProducer validationProducer = (ValidationProducer) component;
						
						boolean hasErrorMessage = validationProducer.hasError();
						if (hasErrorMessage != errorStates.get(validationProducer)) {
							errorStates.put(validationProducer, hasErrorMessage);
							
							Component[] components = null;
							
							if (hasErrorMessage) {
								components = validationProducer.inputInvalid();
							} else {
								components = validationProducer.inputValid();
							}
							
							if (components != null) {
								for (Component component2 : components) {
									target.addComponent(component2);
								}
							}
						}
						
						return CONTINUE_TRAVERSAL;
					}
				});
			}
		};
		saveButton.add(new SimpleAttributeModifier("value", submitLabel));
		add(saveButton);
	}
	
	@Override
	protected void onAttach() {
		associatedForm = (Form) findParent(Form.class);
		
		associatedForm.visitChildren(ValidationProducer.class, new IVisitor() {
			public Object component(Component component) {
				ValidationProducer validationProducer = (ValidationProducer) component;
				
				errorStates.put(validationProducer, validationProducer.hasError());
				
				return CONTINUE_TRAVERSAL;
			}
		});
	}
	
	protected abstract void onCancel(AjaxRequestTarget target);
	
	protected abstract void onSave(AjaxRequestTarget target);
	
}
