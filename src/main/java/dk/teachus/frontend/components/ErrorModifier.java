package dk.teachus.frontend.components;

import wicket.ajax.AjaxRequestTarget;
import wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import wicket.markup.html.panel.FeedbackPanel;
import wicket.util.string.Strings;

public class ErrorModifier extends AjaxFormComponentUpdatingBehavior {
	private static final long serialVersionUID = 1L;
	
	private FeedbackPanel feedbackPanel;
	
	public ErrorModifier(FeedbackPanel feedbackPanel, String event) {
		super(event);
		
		this.feedbackPanel = feedbackPanel;
		
		if (feedbackPanel != null) {
			feedbackPanel.setOutputMarkupId(true);
		}
	}
		
	@Override
	protected void onUpdate(AjaxRequestTarget target) {
		target.addComponent(getComponent());
		
		if (feedbackPanel != null) {
			target.addComponent(feedbackPanel);
		}
	}
	
	@Override
	protected void onError(AjaxRequestTarget target, RuntimeException e) {
		target.addComponent(getComponent());
		
		if (feedbackPanel != null) {
			target.addComponent(feedbackPanel);
		}
	}

	protected CharSequence removeCssClass(CharSequence classString, CharSequence cssClass) {
		StringBuilder cssClasses = new StringBuilder();
		
		if (Strings.isEmpty(classString) == false) {
			String[] classes = classString.toString().split(" ");
			for (String clz : classes) {
				if (clz.equals(cssClass) == false) {
					if (cssClasses.length() > 0) {
						cssClasses.append(" ");
					}
					
					cssClasses.append(clz);
				}
			}
		}
		
		return cssClasses;
	}

	protected CharSequence addCssClass(CharSequence classString, CharSequence cssClass) {
		CharSequence cssClasses = new StringBuilder();
		
		if (Strings.isEmpty(classString) == false) {
			cssClasses = removeCssClass(classString, cssClass);
			if (cssClasses.length() > 0) {
				cssClasses = cssClasses + " ";
			}
			cssClasses = cssClasses + cssClass.toString();
		} else {
			cssClasses= cssClass;
		}
		
		return cssClasses;
	}
}
