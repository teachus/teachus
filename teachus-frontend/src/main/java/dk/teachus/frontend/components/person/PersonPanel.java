/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package dk.teachus.frontend.components.person;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import org.apache.wicket.Application;
import org.apache.wicket.RestartResponseAtInterceptPageException;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.form.validation.EqualInputValidator;
import org.apache.wicket.markup.html.form.validation.IFormValidator;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.util.string.Strings;
import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.IValidator;
import org.apache.wicket.validation.ValidationError;
import org.apache.wicket.validation.validator.EmailAddressValidator;
import org.apache.wicket.validation.validator.PatternValidator;
import org.apache.wicket.validation.validator.StringValidator;

import dk.teachus.backend.dao.PersonDAO;
import dk.teachus.backend.domain.Person;
import dk.teachus.backend.domain.Theme;
import dk.teachus.frontend.TeachUsApplication;
import dk.teachus.frontend.TeachUsSession;
import dk.teachus.frontend.components.form.ButtonPanelElement;
import dk.teachus.frontend.components.form.DropDownElement;
import dk.teachus.frontend.components.form.FormPanel;
import dk.teachus.frontend.components.form.IntegerFieldElement;
import dk.teachus.frontend.components.form.PasswordFieldElement;
import dk.teachus.frontend.components.form.ReadOnlyElement;
import dk.teachus.frontend.components.form.TextAreaElement;
import dk.teachus.frontend.components.form.TextFieldElement;
import dk.teachus.frontend.components.form.FormPanel.FormValidator;
import dk.teachus.frontend.ical.IcalUrlModel;
import dk.teachus.frontend.models.PersonModel;
import dk.teachus.frontend.pages.persons.PersonsPage;
import dk.teachus.frontend.utils.LocaleChoiceRenderer;
import dk.teachus.frontend.utils.ThemeChoiceRenderer;

public abstract class PersonPanel extends Panel {
	private static final long serialVersionUID = 1L;
	
	protected String password1;
	protected String password2;

	public PersonPanel(String id, final PersonModel<? extends Person> personModel) {
		super(id, personModel);
		
		if (allowUserEditing(TeachUsSession.get().getPerson(), personModel.getObject()) == false) {
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
		emailField.add(EmailAddressValidator.getInstance());
		formPanel.addElement(emailField);
		
		// Phone number
		formPanel.addElement(new IntegerFieldElement(TeachUsSession.get().getString("General.phoneNumber"), new PropertyModel(personModel, "phoneNumber"), 10)); //$NON-NLS-1$ //$NON-NLS-2$
		
		// Username
		if (isUsernameEnabled()) {
			TextFieldElement usernameField = new TextFieldElement(TeachUsSession.get().getString("General.username"), new PropertyModel(personModel, "username"), true); //$NON-NLS-1$ //$NON-NLS-2$
			usernameField.add(StringValidator.lengthBetween(3, 50));

			// Validate the username for correct content
			usernameField.add(new PatternValidator("^[a-zA-Z0-9-_]+$") { //$NON-NLS-1$
				private static final long serialVersionUID = 1L;

				@Override
				public void error(IValidatable validatable) {
					ValidationError validationError = new ValidationError();
					validationError.setMessage(TeachUsSession.get().getString("PersonPanel.usernameCharacters"));
					validatable.error(validationError); //$NON-NLS-1$
				}
			});
			
			// validate the username checking for dublicates
			usernameField.add(new IValidator() {
				private static final long serialVersionUID = 1L;

				public void validate(IValidatable validatable) {
					PersonDAO personDAO = TeachUsApplication.get().getPersonDAO();
					String username = validatable.getValue().toString();
					
					Person existingPerson = personDAO.usernameExists(username);
					
					if (existingPerson != null && existingPerson.getId().equals(personModel.getPersonId()) == false) {
						String localeString = TeachUsSession.get().getString("PersonPanel.userAlreadyExists"); //$NON-NLS-1$
						localeString = localeString.replace("${username}", username); //$NON-NLS-1$
						ValidationError validationError = new ValidationError();
						validationError.setMessage(localeString);
						validatable.error(validationError);
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
		
		// iCalendar URL
		formPanel.addElement(new ReadOnlyElement(TeachUsSession.get().getString("Ical.label"), new IcalUrlModel(personModel)));
		
		// Additional elements
		appendElements(formPanel);
		
		// Notes
		if (isNotesVisible()) {
			formPanel.addElement(new TextAreaElement("Notes", new PropertyModel(personModel, "notes")));
		}
		
		// Buttons
		formPanel.addElement(new ButtonPanelElement() {
			private static final long serialVersionUID = 1L;

			@Override
			protected void onCancel() {
				getRequestCycle().setResponsePage(getPersonsPageClass());
			}

			@Override
			protected void onSave(AjaxRequestTarget target) {
				if (Strings.isEmpty(password1) == false) {
					personModel.setPassword(password1);
				}
				
				personModel.save();			
				
				PersonPanel.this.onSave(personModel.getObject());
				
				getRequestCycle().setResponsePage(getPersonsPageClass());
			}			
		});
	}
	
	protected abstract boolean allowUserEditing(Person loggedInPerson, Person editPerson);
	
	protected abstract Class<? extends PersonsPage<? extends Person>> getPersonsPageClass();
	
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
	
	protected boolean isNotesVisible() {
		return false;
	}
	
	protected void onSave(Person person) {
	}
	
	protected void appendElements(FormPanel formPanel) {
	}
	
}
