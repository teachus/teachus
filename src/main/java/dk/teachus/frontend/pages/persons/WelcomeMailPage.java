package dk.teachus.frontend.pages.persons;

import wicket.markup.html.form.validation.EqualInputValidator;
import wicket.markup.html.form.validation.IFormValidator;
import wicket.markup.html.form.validation.StringValidator;
import wicket.model.PropertyModel;
import dk.teachus.frontend.TeachUsSession;
import dk.teachus.frontend.UserLevel;
import dk.teachus.frontend.components.form.FormPanel;
import dk.teachus.frontend.components.form.PasswordFieldElement;
import dk.teachus.frontend.components.form.FormPanel.FormValidator;
import dk.teachus.frontend.models.PupilModel;
import dk.teachus.frontend.pages.AuthenticatedBasePage;

public class WelcomeMailPage extends AuthenticatedBasePage {
	private static final long serialVersionUID = 1L;

	private String password1;
	private String password2;
	
	public WelcomeMailPage(Long pupilId) {
		super(UserLevel.TEACHER, true);
		
		if (pupilId == null) {
			throw new IllegalArgumentException("Must provide a valid pupilId");
		}
		
		PupilModel pupilModel = new PupilModel(pupilId);
		
		FormPanel formPanel = new FormPanel("passwordForm");
		add(formPanel);
		
		// Password 1
		final PasswordFieldElement password1Field = new PasswordFieldElement(TeachUsSession.get().getString("General.password"), new PropertyModel(this, "password1"), pupilModel.getPersonId() == null); //$NON-NLS-1$ //$NON-NLS-2$
		password1Field.add(StringValidator.lengthBetween(4, 32));
		formPanel.addElement(password1Field);
		
		// Password 2
		final PasswordFieldElement password2Field = new PasswordFieldElement(TeachUsSession.get().getString("PersonPanel.repeatPassword"), new PropertyModel(this, "password2")); //$NON-NLS-1$ //$NON-NLS-2$
		formPanel.addElement(password2Field);
		
		// Password validator
		formPanel.addValidator(new FormValidator() {
			private static final long serialVersionUID = 1L;

			public IFormValidator getFormValidator() {
				return new EqualInputValidator(password1Field.getFormComponent(), password2Field.getFormComponent());
			}			
		});
	}

	@Override
	protected AuthenticatedPageCategory getPageCategory() {
		return AuthenticatedPageCategory.PUPILS;
	}

	@Override
	protected String getPageLabel() {
		return "Send login informations";
	}

	public String getPassword1() {
		return password1;
	}

	public void setPassword1(String password1) {
		this.password1 = password1;
	}

	public String getPassword2() {
		return password2;
	}

	public void setPassword2(String password2) {
		this.password2 = password2;
	}

}
