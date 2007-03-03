package dk.teachus.frontend.pages;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import wicket.RestartResponseAtInterceptPageException;
import wicket.ajax.AjaxRequestTarget;
import wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import wicket.markup.html.basic.Label;
import wicket.markup.html.form.Button;
import wicket.markup.html.form.CheckBox;
import wicket.markup.html.form.DropDownChoice;
import wicket.markup.html.form.Form;
import wicket.markup.html.form.FormComponentLabel;
import wicket.markup.html.form.PasswordTextField;
import wicket.markup.html.form.TextField;
import wicket.markup.html.form.persistence.CookieValuePersister;
import wicket.markup.html.form.persistence.IValuePersister;
import wicket.markup.html.panel.FeedbackPanel;
import wicket.model.CompoundPropertyModel;
import wicket.model.Model;
import wicket.model.PropertyModel;
import wicket.protocol.http.WebApplication;
import dk.teachus.frontend.TeachUsApplication;
import dk.teachus.frontend.TeachUsSession;
import dk.teachus.frontend.utils.LocaleChoiceRenderer;

public abstract class UnAuthenticatedBasePage extends BasePage {
	
	public static enum UnAuthenticatedPageCategory implements PageCategory {
		INFO
	}
	
	public static class User implements Serializable {
		private static final long serialVersionUID = 1L;

		private String username;

		private String password;
		
		private boolean remember;

		public boolean isRemember() {
			return remember;
		}

		public void setRemember(boolean remember) {
			this.remember = remember;
		}

		public String getPassword() {
			return password;
		}

		public void setPassword(String password) {
			this.password = password;
		}

		public String getUsername() {
			return username;
		}

		public void setUsername(String username) {
			this.username = username;
		}
	}
	
	private Locale locale;
	
	private User user;
	
	public UnAuthenticatedBasePage() {
		createSignInBox();
		
		createLocaleBox();
	}

	private void createSignInBox() {
		add(new Label("signInLabel", TeachUsSession.get().getString("General.signIn"))); //$NON-NLS-1$ //$NON-NLS-2$
		
		add(new FeedbackPanel("feedback"));
		
		user = new User();
		final Form signInForm = new Form("signInForm", new CompoundPropertyModel(user)) { //$NON-NLS-1$
			private static final long serialVersionUID = 1L;

			@Override
			protected void onSubmit() {
				signin();
			}
		};
		add(signInForm);
		
		final TextField username = new TextField("username"); //$NON-NLS-1$
		username.setOutputMarkupId(true);
		username.setRequired(true);
		signInForm.add(username);
		signInForm.add(new FormComponentLabel("usernameLabel", username).add(new Label("usernameLabel", TeachUsSession.get().getString("General.username")).setRenderBodyOnly(true))); //$NON-NLS-1$ //$NON-NLS-2$
		
		final PasswordTextField password = new PasswordTextField("password"); //$NON-NLS-1$
		password.setResetPassword(false);
		signInForm.add(password);
		signInForm.add(new FormComponentLabel("passwordLabel", password).add(new Label("passwordLabel", TeachUsSession.get().getString("General.password")).setRenderBodyOnly(true))); //$NON-NLS-1$ //$NON-NLS-2$
		
		CheckBox remember = new CheckBox("remember");
		remember.add(new AjaxFormComponentUpdatingBehavior("onclick") {
			private static final long serialVersionUID = 1L;

			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				username.setPersistent(user.isRemember());
				password.setPersistent(user.isRemember());
			}
		});
		signInForm.add(remember);
		signInForm.add(new FormComponentLabel("rememberLabel", remember).add(new Label("rememberLabel", TeachUsSession.get().getString("General.remember")).setRenderBodyOnly(true)));
		
		signInForm.add(new Button("signIn", new Model("Log ind")));
		
		IValuePersister persister = new CookieValuePersister();
		persister.load(username);
		persister.load(password);
		
		user.setUsername(username.getModelObjectAsString());
		user.setPassword(password.getModelObjectAsString());
		
		signin();
	}
		
	private void signin() {		
		TeachUsSession teachUsSession = TeachUsSession.get();
		
		teachUsSession.signIn(user.getUsername(), user.getPassword());
		
		if (teachUsSession.isAuthenticated()) {
			throw new RestartResponseAtInterceptPageException(WebApplication.get().getHomePage());
		}
	}

	private void createLocaleBox() {
		add(new Label("localeLabel", TeachUsSession.get().getString("General.locale")));
		
		Form localeForm = new Form("localeForm") {
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isVisible() {
				return TeachUsSession.get().isAuthenticated() == false;
			}
		};
		add(localeForm);
		
		setLocale(TeachUsSession.get().getLocale());
		
		List<Locale> availableLocales = TeachUsApplication.get().getAvailableLocales();
		
		DropDownChoice localeDropDown = new DropDownChoice("locale", new PropertyModel(this, "locale"), availableLocales, new LocaleChoiceRenderer());
		localeForm.add(localeDropDown);
		
		localeDropDown.add(new AjaxFormComponentUpdatingBehavior("onchange") {
			private static final long serialVersionUID = 1L;

			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				TeachUsSession.get().changeLocale(getLocale());
				
				getRequestCycle().setResponsePage(UnAuthenticatedBasePage.this.getClass());
			}			
		});
	}

	public Locale getLocale() {
		return locale;
	}

	public void setLocale(Locale locale) {
		this.locale = locale;
	}
	
	@Override
	protected List<MenuItem> createMenuItems() {
		List<MenuItem> menuItems = new ArrayList<MenuItem>();

		menuItems.add(new MenuItem(InfoPage.class, TeachUsSession.get().getString("General.info"), UnAuthenticatedPageCategory.INFO));
		
		return menuItems;
	}
	
	@Override
	protected abstract UnAuthenticatedPageCategory getPageCategory();

}
