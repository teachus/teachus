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
import wicket.markup.html.form.FormComponent;
import wicket.markup.html.panel.FeedbackPanel;
import dk.teachus.frontend.TeachUsSession;

public abstract class ButtonPanelElement extends FormElement {
	private static final long serialVersionUID = 1L;
	
	private Map<FormComponent, Boolean> errorStates = new HashMap<FormComponent, Boolean>();
	
	private Form associatedForm;

	public ButtonPanelElement() {
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
				associatedForm.visitChildren(FormComponent.class, new wicket.Component.IVisitor() {
					public Object component(Component component) {
						FormComponent formComponent = (FormComponent) component;
						
						if (formComponent.hasErrorMessage() != errorStates.get(formComponent)) {
							errorStates.put(formComponent, formComponent.hasErrorMessage());
							target.addComponent(formComponent);
						}
						
						return CONTINUE_TRAVERSAL;
					}
				});
				
				associatedForm.visitChildren(FeedbackPanel.class, new wicket.Component.IVisitor() {
					public Object component(Component component) {
						target.addComponent(component);
						
						return CONTINUE_TRAVERSAL;
					}
				});
			}
		};
		saveButton.add(new SimpleAttributeModifier("value", TeachUsSession.get().getString("General.save")));
		add(saveButton);
	}
	
	@Override
	protected void onAttach() {
		associatedForm = (Form) findParent(Form.class);
		
		associatedForm.visitChildren(FormComponent.class, new IVisitor() {
			public Object component(Component component) {
				FormComponent formComponent = (FormComponent) component;
				
				errorStates.put(formComponent, formComponent.hasErrorMessage());
				formComponent.setOutputMarkupId(true);
				
				return CONTINUE_TRAVERSAL;
			}
		});
	}
	
	protected abstract void onCancel(AjaxRequestTarget target);
	
	protected abstract void onSave(AjaxRequestTarget target);
	
}
