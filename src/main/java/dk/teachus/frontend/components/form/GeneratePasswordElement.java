package dk.teachus.frontend.components.form;

import org.joda.time.DateTime;

import wicket.ajax.AjaxRequestTarget;
import wicket.ajax.markup.html.AjaxLink;
import wicket.markup.html.basic.Label;

public abstract class GeneratePasswordElement extends FormElement {
	
	public GeneratePasswordElement() {
		this("");
	}
	
	public GeneratePasswordElement(String label) {
		this(label, "password");
	}
	
	public GeneratePasswordElement(String label, final String seed) {
		add(new Label("label", label).setRenderBodyOnly(true));
		
		AjaxLink generatePasswordLink = new AjaxLink("generatePasswordLink") {
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(AjaxRequestTarget target) {
				String password = seed;
				
				if (password == null) {
					password = "";
				}
				
				password += new DateTime().getMillisOfSecond();
				
				passwordGenerated(target, password);
			}			
		};
		add(generatePasswordLink);
		
		generatePasswordLink.add(new Label("generatePasswordLabel", "Generate").setRenderBodyOnly(true));
	}
	
	protected abstract void passwordGenerated(AjaxRequestTarget target, String password);
}
