package dk.teachus.frontend.components;

import wicket.WicketRuntimeException;
import wicket.ajax.AjaxRequestTarget;
import wicket.markup.html.form.DropDownChoice;
import wicket.markup.html.image.Image;
import wicket.markup.html.panel.FeedbackPanel;
import dk.teachus.frontend.utils.Resources;

public class DropdownErrorModifier extends ErrorModifier {
	private static final long serialVersionUID = 1L;

	private Image stateComponent;
	
	public DropdownErrorModifier(FeedbackPanel feedbackPanel, String event, Image stateComponent) {
		super(feedbackPanel, event);
		
		this.stateComponent = stateComponent;
		createStateComponent();
	}

	@Override
	protected void onBind() {
		super.onBind();
		
		if (getComponent() instanceof DropDownChoice == false) {
			throw new WicketRuntimeException("Can only bind this error modifier to a DropDownChoice");
		}
	}
	
	@Override
	protected void onUpdate(AjaxRequestTarget target) {
		super.onUpdate(target);
		
		createStateComponent();
		target.addComponent(stateComponent);
	}
	
	@Override
	protected void onError(AjaxRequestTarget target, RuntimeException e) {
		super.onError(target, e);
		
		createStateComponent();
		target.addComponent(stateComponent);
	}

	private void createStateComponent() {
		boolean hasError = false;
		if (getComponent() != null) {
			hasError = getComponent().hasErrorMessage();
		}
		
		Image valid = new Image(stateComponent.getId(), hasError ? Resources.UNPAID : Resources.PAID);
		valid.setOutputMarkupId(true);
		stateComponent.getParent().replace(valid);
		stateComponent = valid;
	}
	
}
