package dk.teachus.frontend.components.form;

import java.io.Serializable;

import wicket.Component;
import wicket.WicketRuntimeException;
import wicket.ajax.AjaxRequestTarget;
import wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import wicket.behavior.AbstractBehavior;
import wicket.behavior.IBehavior;

public class ElementModifier extends AbstractBehavior {
	private static final long serialVersionUID = 1L;

	public static interface BehaviorAdder extends Serializable {
		IBehavior createNewBehavior();
	}
	
	private String event;
	private ValidationProducer validationProducer;
	
	public ElementModifier(String event) {
		this(event, null);
	}
	
	public ElementModifier(String event, ValidationProducer validationProducer) {
		this.event = event;
		this.validationProducer = validationProducer;
	}

	@Override
	public void bind(Component component) {
		final ValidationProducer validationProducer = getValidationProducer(component);
		
		validationProducer.add(new BehaviorAdder() {
			private static final long serialVersionUID = 1L;

			public IBehavior createNewBehavior() {
				return new AjaxFormComponentUpdatingBehavior(event) {
					private static final long serialVersionUID = 1L;
					
					@Override
					protected void onUpdate(AjaxRequestTarget target) {
						Component[] components = validationProducer.inputValid();
						
						for (Component component2 : components) {
							target.addComponent(component2);
						}
					}		
					
					@Override
					protected void onError(AjaxRequestTarget target, RuntimeException e) {
						Component[] components = validationProducer.inputInvalid();
						
						for (Component component2 : components) {
							target.addComponent(component2);
						}
					}
				};
			}
			
		});
		
		if (validationProducer.hasError()) {
			validationProducer.inputInvalid();
		} else {
			validationProducer.inputValid();
		}
	}
	
	private ValidationProducer getValidationProducer(Component component) {
		if (validationProducer != null) {
			return validationProducer;
		} else {
			if (component instanceof ValidationProducer == false) {
				throw new WicketRuntimeException();
			}
			
			return (ValidationProducer) component;
		}
	}
	
}
