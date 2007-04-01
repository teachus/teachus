package dk.teachus.frontend.components.form;

import org.joda.time.DateTime;

import dk.teachus.frontend.TeachUsSession;

import wicket.ajax.AjaxRequestTarget;
import wicket.ajax.markup.html.AjaxLink;
import wicket.markup.html.basic.Label;

public abstract class GeneratePasswordElement extends FormElement {
	
	public GeneratePasswordElement() {
		this(""); //$NON-NLS-1$
	}
	
	public GeneratePasswordElement(String label) {
		this(label, "password"); //$NON-NLS-1$
	}
	
	public GeneratePasswordElement(String label, final String seed) {
		add(new Label("label", label).setRenderBodyOnly(true)); //$NON-NLS-1$
		
		AjaxLink generatePasswordLink = new AjaxLink("generatePasswordLink") { //$NON-NLS-1$
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(AjaxRequestTarget target) {
				String password = seed;
				
				if (password == null) {
					password = ""; //$NON-NLS-1$
				}
				
				password += new DateTime().getMillisOfSecond();
				
				passwordGenerated(target, password);
			}			
		};
		add(generatePasswordLink);
		
		generatePasswordLink.add(new Label("generatePasswordLabel", TeachUsSession.get().getString("GeneratePasswordElement.generate")).setRenderBodyOnly(true)); //$NON-NLS-1$ //$NON-NLS-2$
	}
	
	protected abstract void passwordGenerated(AjaxRequestTarget target, String password);
}
