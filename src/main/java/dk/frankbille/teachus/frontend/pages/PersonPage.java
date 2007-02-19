package dk.frankbille.teachus.frontend.pages;

import java.util.List;
import java.util.Locale;

import wicket.RestartResponseAtInterceptPageException;
import wicket.ajax.AjaxRequestTarget;
import wicket.ajax.markup.html.form.AjaxSubmitButton;
import wicket.behavior.SimpleAttributeModifier;
import wicket.markup.html.WebMarkupContainer;
import wicket.markup.html.basic.Label;
import wicket.markup.html.form.DropDownChoice;
import wicket.markup.html.form.Form;
import wicket.markup.html.form.PasswordTextField;
import wicket.markup.html.form.TextField;
import wicket.markup.html.form.validation.EmailAddressPatternValidator;
import wicket.markup.html.form.validation.EqualInputValidator;
import wicket.markup.html.form.validation.StringValidator;
import wicket.markup.html.panel.FeedbackPanel;
import wicket.model.CompoundPropertyModel;
import wicket.model.PropertyModel;
import wicket.protocol.http.WebApplication;
import wicket.util.string.Strings;
import dk.frankbille.teachus.dao.PersonDAO;
import dk.frankbille.teachus.domain.Person;
import dk.frankbille.teachus.domain.Pupil;
import dk.frankbille.teachus.frontend.TeachUsApplication;
import dk.frankbille.teachus.frontend.TeachUsSession;
import dk.frankbille.teachus.frontend.UserLevel;
import dk.frankbille.teachus.frontend.utils.LocaleChoiceRenderer;

public abstract class PersonPage extends AuthenticatedBasePage {	
	private Person person;
	
	protected String password1;
	protected String password2;
	private FeedbackPanel feedbackPanel;
	
	public PersonPage(UserLevel userLevel, final Person person) {
		super(userLevel);
		
		if (allowUserEditing(TeachUsSession.get().getPerson(), person) == false) {
			throw new RestartResponseAtInterceptPageException(WebApplication.get().getHomePage());
		}
		
		this.person = person;
		
		Form form = new Form("form", new CompoundPropertyModel(person)); //$NON-NLS-1$
		form.setOutputMarkupId(true);
		add(form);
		
		feedbackPanel = new FeedbackPanel("feedback"); //$NON-NLS-1$
		feedbackPanel.setOutputMarkupId(true);
		form.add(feedbackPanel);
		
		form.add(new Label("nameLabel", TeachUsSession.get().getString("General.name"))); //$NON-NLS-1$ //$NON-NLS-2$
		TextField nameField = new TextField("name"); //$NON-NLS-1$
		nameField.setRequired(true);
		nameField.add(StringValidator.lengthBetween(2, 100));
		form.add(nameField);
		
		form.add(new Label("emailLabel", TeachUsSession.get().getString("General.email"))); //$NON-NLS-1$ //$NON-NLS-2$
		TextField emailField = new TextField("email"); //$NON-NLS-1$
		emailField.setRequired(true);
		emailField.add(EmailAddressPatternValidator.getInstance());
		form.add(emailField);
		
		form.add(new Label("phoneNumberLabel", TeachUsSession.get().getString("General.phoneNumber"))); //$NON-NLS-1$ //$NON-NLS-2$
		form.add(new TextField("phoneNumber")); //$NON-NLS-1$
		
		form.add(new Label("usernameLabel", TeachUsSession.get().getString("General.username"))); //$NON-NLS-1$ //$NON-NLS-2$
		TextField usernameField = new TextField("username") {
			private static final long serialVersionUID = 1L; //$NON-NLS-1$

			@Override
			public boolean isEnabled() {
				return isUsernameEnabled();
			}
		};
		usernameField.setRequired(true);
		usernameField.add(StringValidator.lengthBetween(3, 50));
		form.add(usernameField);
		
		form.add(new Label("password1Label", TeachUsSession.get().getString("General.password"))); //$NON-NLS-1$ //$NON-NLS-2$
		PasswordTextField passwordTextField1 = new PasswordTextField("password", new PropertyModel(this, "password1")); //$NON-NLS-1$
		if (person.getId() != null) {
			passwordTextField1.setRequired(false);
		}
		passwordTextField1.add(StringValidator.lengthBetween(4, 32));
		form.add(passwordTextField1);
		
		form.add(new Label("password2Label", TeachUsSession.get().getString("PersonPanel.repeatPassword"))); //$NON-NLS-1$ //$NON-NLS-2$
		final PasswordTextField passwordTextField2 = new PasswordTextField("password2", new PropertyModel(this, "password2")); //$NON-NLS-1$ //$NON-NLS-2$
		passwordTextField2.setRequired(false);
		form.add(passwordTextField2);
		
		form.add(new EqualInputValidator(passwordTextField1, passwordTextField2));
		
		// Locale row
		WebMarkupContainer localeRow = new WebMarkupContainer("localeRow") {
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isVisible() {
				return isLocaleVisible();
			}
		};
		form.add(localeRow);
		localeRow.add(new Label("localeLabel", TeachUsSession.get().getString("General.locale"))); //$NON-NLS-1$ //$NON-NLS-2$
		List<Locale> availableLocales = TeachUsApplication.get().getAvailableLocales();
		localeRow.add(new DropDownChoice("locale", availableLocales, new LocaleChoiceRenderer())); //$NON-NLS-1$
		
		// Teacher row
		WebMarkupContainer teacherRow = new WebMarkupContainer("teacherRow") { //$NON-NLS-1$
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isVisible() {
				return person instanceof Pupil;
			}
		};
		form.add(teacherRow);
		teacherRow.add(new Label("teacherNameLabel", TeachUsSession.get().getString("General.teacher"))); //$NON-NLS-1$ //$NON-NLS-2$
		teacherRow.add(new Label("teacher.name")); //$NON-NLS-1$
		
		AjaxSubmitButton saveButton = new AjaxSubmitButton("saveButton", form) { //$NON-NLS-1$
			private static final long serialVersionUID = 1L;

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form form) {
				boolean newPerson = person.getId() == null;
				
				if (Strings.isEmpty(password1) == false) {
					person.setPassword(password1);
				}
				
				PersonDAO personDAO = TeachUsApplication.get().getPersonDAO();
				
				personDAO.save(person);
				
				// Send welcome mail if the person is new AND a pupil
				if (newPerson && person instanceof Pupil) {
					personDAO.sendWelcomeMail(person.getId(), TeachUsApplication.get().getServerName());
				}
				
				getRequestCycle().setResponsePage(getPersonsPageClass());
			}		
			
			@Override
			protected void onError(AjaxRequestTarget target, Form form) {
				target.addComponent(feedbackPanel);
			}
		};
		saveButton.add(new SimpleAttributeModifier("value", TeachUsSession.get().getString("General.save"))); //$NON-NLS-1$ //$NON-NLS-2$
		form.add(saveButton);
	}
	
	@Override
	protected String getPageLabel() {
		return person.getName();
	}
	
	protected abstract boolean allowUserEditing(Person loggedInPerson, Person editPerson);
	
	protected abstract Class<? extends PersonsPage> getPersonsPageClass();
	
	protected boolean isUsernameEnabled() {
		return true;
	}
	
	protected boolean isLocaleVisible() {
		return true;
	}
}
