package dk.teachus.frontend.components.list;

import java.util.List;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.extensions.markup.html.repeater.data.table.filter.ChoiceFilter;
import org.apache.wicket.extensions.markup.html.repeater.data.table.filter.FilterForm;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.model.IModel;

import dk.teachus.frontend.components.Select2Enabler;

public class TeachUsChoiceFilter<T> extends ChoiceFilter<T> {
	private static final long serialVersionUID = 1L;

	public TeachUsChoiceFilter(String id, IModel<T> model, FilterForm<?> form, IModel<List<? extends T>> choices, boolean autoSubmit) {
		super(id, model, form, choices, autoSubmit);
	}
	
	public TeachUsChoiceFilter(String id, IModel<T> model, FilterForm<?> form, List<? extends T> choices, boolean autoSubmit) {
		super(id, model, form, choices, autoSubmit);
	}
	
	public TeachUsChoiceFilter(String id, IModel<T> model, FilterForm<?> form, List<? extends T> choices, IChoiceRenderer<T> renderer, boolean autoSubmit) {
		super(id, model, form, choices, renderer, autoSubmit);
	}
	
	public TeachUsChoiceFilter(String id, IModel<T> model, FilterForm<?> form, IModel<List<? extends T>> choices, IChoiceRenderer<T> renderer, boolean autoSubmit) {
		super(id, model, form, choices, renderer, autoSubmit);
	}
	
	@Override
	protected DropDownChoice<T> newDropDownChoice(String id, IModel<T> model, IModel<List<? extends T>> choices, IChoiceRenderer<T> renderer) {
		DropDownChoice<T> dropDownChoice = super.newDropDownChoice(id, model, choices, renderer);
		dropDownChoice.add(new Select2Enabler());
		dropDownChoice.setNullValid(true);
		dropDownChoice.add(AttributeModifier.replace("style", "width:100%"));
		return dropDownChoice;
	}
	
}
