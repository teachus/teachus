package dk.teachus.frontend.components.person;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import wicket.Application;
import wicket.RestartResponseAtInterceptPageException;
import wicket.ajax.AjaxRequestTarget;
import wicket.markup.html.form.FormComponent;
import wicket.markup.html.form.validation.EmailAddressPatternValidator;
import wicket.markup.html.form.validation.EqualInputValidator;
import wicket.markup.html.form.validation.IFormValidator;
import wicket.markup.html.form.validation.IValidator;
import wicket.markup.html.form.validation.StringValidator;
import wicket.markup.html.panel.Panel;
import wicket.model.PropertyModel;
import wicket.util.string.Strings;
import dk.teachus.dao.PersonDAO;
import dk.teachus.domain.Person;
import dk.teachus.domain.Theme;
import dk.teachus.frontend.TeachUsApplication;
import dk.teachus.frontend.TeachUsSession;
import dk.teachus.frontend.components.form.ButtonPanelElement;
import dk.teachus.frontend.components.form.DropDownElement;
import dk.teachus.frontend.components.form.FormPanel;
import dk.teachus.frontend.components.form.IntegerFieldElement;
import dk.teachus.frontend.components.form.PasswordFieldElement;
import dk.teachus.frontend.components.form.ReadOnlyElement;
import dk.teachus.frontend.components.form.TextFieldElement;
import dk.teachus.frontend.components.form.FormPanel.FormValidator;
import dk.teachus.frontend.models.PersonModel;
import dk.teachus.frontend.pages.persons.PersonsPage;
import dk.teachus.frontend.utils.LocaleChoiceRenderer;
import dk.teachus.frontend.utils.ThemeChoiceRenderer;

public abstract class PersonPanel extends Panel {
	protected String password1;
	protected String password2;

	public PersonPanel(String id, final PersonModel personModel) {
		super(id, personModel);
		
		if (allowUserEditing(TeachUsSession.get().getPerson(), personModel.getObject(this)) == false) {
			throw new RestartResponseAtInterceptPageException(Application.get().getHomePage());
		}
		
		FormPanel formPanel = new FormPanel("form"); //$NON-NLS-1$
		add(formPanel);
		
		// Name
		TextFieldElement nameField = new TextFieldElement(TeachUsSession.get().getString("General.name"), new PropertyModel(personModel, "name"), true, 32); //$NON-NLS-1$ //$NON-NLS-2$
		nameField.add(StringValidator.lengthBetween(2, 100));
		formPanel.addElement(nameField);
		
		// Email
		TextFieldElement emailField = new TextFieldElement(TeachUsSession.get().getString("General.email"), new PropertyModel(personModel, "email"), true, 50); //$NON-NLS-1$ //$NON-NLS-2$
		emailField.add(EmailAddressPatternValidator.getInstance());
		formPanel.addElement(emailField);
		
		// Phone number
		formPanel.addElement(new IntegerFieldElement(TeachUsSession.get().getString("General.phoneNumber"), new PropertyModel(personModel, "phoneNumber"), 10)); //$NON-NLS-1$ //$NON-NLS-2$
		
		// Username
		if (isUsernameEnabled()) {
			TextFieldElement usernameField = new TextFieldElement(TeachUsSession.get().getString("General.username"), new PropertyModel(personModel, "username"), true); //$NON-NLS-1$ //$NON-NLS-2$
			usernameField.add(StringValidator.lengthBetween(3, 50));

			// validate the username checking for dublicates
			usernameField.add(new IValidator() {
				private static final long serialVersionUID = 1L;

				public void validate(FormComponent component) {
					PersonDAO personDAO = TeachUsApplication.get().getPersonDAO();
					String username = component.getValue();
					
					Person existingPerson = personDAO.usernameExists(username);
					
					if (existingPerson != null && existingPerson.getId().equals(personModel.getPersonId()) == false) {
						String localeString = TeachUsSession.get().getString("PersonPanel.userAlreadyExists"); //$NON-NLS-1$
						localeString = localeString.replace("${username}", username); //$NON-NLS-1$
						component.error(localeString);
					}
				}				
			});
			formPanel.addElement(usernameField);
		} else {
			formPanel.addElement(new ReadOnlyElement(TeachUsSession.get().getString("General.username"), new PropertyModel(personModel, "username"))); //$NON-NLS-1$ //$NON-NLS-2$
		}
	
		// Password 1
		if (isPasswordVisible()) {
			final PasswordFieldElement password1Field = new PasswordFieldElement(TeachUsSession.get().getString("General.password"), new PropertyModel(this, "password1"), personModel.getPersonId() == null); //$NON-NLS-1$ //$NON-NLS-2$
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
		
		// Locale
		if (isLocaleVisible()) {
			List<Locale> availableLocales = TeachUsApplication.get().getAvailableLocales();
			formPanel.addElement(new DropDownElement(TeachUsSession.get().getString("General.locale"), new PropertyModel(personModel, "locale"), availableLocales, new LocaleChoiceRenderer())); //$NON-NLS-1$ //$NON-NLS-2$
		}
		
		if (isCurrencyVisible()) {
			TextFieldElement currencyField = new TextFieldElement(TeachUsSession.get().getString("General.currency"), new PropertyModel(personModel, "currency"), 4); //$NON-NLS-1$ //$NON-NLS-2$
			currencyField.add(StringValidator.lengthBetween(0, 10));
			formPanel.addElement(currencyField); //$NON-NLS-1$ //$NON-NLS-2$
		}
		
		// Theme
		if (isThemeVisible()) {
			List<Theme> themes = Arrays.asList(Theme.values());
			formPanel.addElement(new DropDownElement(TeachUsSession.get().getString("General.theme"), new PropertyModel(personModel, "theme"), themes, new ThemeChoiceRenderer())); //$NON-NLS-1$ //$NON-NLS-2$
		}
		
		// Teacher
		if (isTeacherVisible()) {
			formPanel.addElement(new ReadOnlyElement(TeachUsSession.get().getString("General.teacher"), new PropertyModel(personModel, "teacher.name"))); //$NON-NLS-1$ //$NON-NLS-2$
		}
		
		// Buttons
		formPanel.addElement(new ButtonPanelElement() {
			private static final long serialVersionUID = 1L;

			@Override
			protected void onCancel(AjaxRequestTarget target) {
				getRequestCycle().setResponsePage(getPersonsPageClass());
			}

			@Override
			protected void onSave(AjaxRequestTarget target) {
				if (Strings.isEmpty(password1) == false) {
					personModel.setPassword(PersonPanel.this, password1);
				}
				
				personModel.save(PersonPanel.this);			
				
				PersonPanel.this.onSave(personModel.getObject(PersonPanel.this));
				
				getRequestCycle().setResponsePage(getPersonsPageClass());
			}			
		});
	}
	
	protected abstract boolean allowUserEditing(Person loggedInPerson, Person editPerson);
	
	protected abstract Class<? extends PersonsPage> getPersonsPageClass();
	
	protected boolean isUsernameEnabled() {
		return true;
	}
	
	protected boolean isLocaleVisible() {
		return true;
	}
	
	protected boolean isCurrencyVisible() {
		return false;
	}

	protected boolean isTeacherVisible() {
		return false;
	}

	protected boolean isThemeVisible() {
		return false;
	}
	
	protected boolean isPasswordVisible() {
		return true;
	}
	
	protected void onSave(Person person) {
	}
	
}
