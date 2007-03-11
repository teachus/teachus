package dk.teachus.frontend.components;

import wicket.WicketRuntimeException;
import wicket.markup.ComponentTag;
import wicket.markup.html.form.TextField;
import wicket.markup.html.panel.FeedbackPanel;

public class TextFieldErrorModifier extends ErrorModifier {
	private static final long serialVersionUID = 1L;
	
	public TextFieldErrorModifier(FeedbackPanel feedbackPanel, String event) {
		super(feedbackPanel, event);
	}
	
	
	@Override
	protected void onBind() {
		super.onBind();
		
		if (getComponent() instanceof TextField == false) {
			throw new WicketRuntimeException("Can only bind this error modifier to a TextField");
		}
	}
		
	@Override
	public void onComponentTag(ComponentTag tag) {
		super.onComponentTag(tag);
		
		if (getComponent().hasErrorMessage()) {
			CharSequence classAttribute = tag.getString("class");
			
			classAttribute = removeCssClass(classAttribute, "valid");
			classAttribute = addCssClass(classAttribute, "error");
			
			tag.put("class", classAttribute);
		} else {
			CharSequence classAttribute = tag.getString("class");
			
			classAttribute = addCssClass(classAttribute, "valid");
			classAttribute = removeCssClass(classAttribute, "error");
			
			tag.put("class", classAttribute);
		}
	}
}
