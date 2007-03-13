package dk.teachus.frontend.components.form;

import java.io.Serializable;

import wicket.Component;
import wicket.markup.html.form.FormComponent;
import dk.teachus.frontend.components.form.ElementModifier.BehaviorAdder;

public interface ValidationProducer extends Serializable {

	Component[] inputValid();

	Component[] inputInvalid();
	
	FormComponent getFormComponent();

	void add(BehaviorAdder adder);

	boolean hasError();

}