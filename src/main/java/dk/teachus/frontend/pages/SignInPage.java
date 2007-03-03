package dk.teachus.frontend.pages;

import java.io.Serializable;

import wicket.RestartResponseAtInterceptPageException;
import wicket.ajax.AjaxRequestTarget;
import wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import wicket.behavior.HeaderContributor;
import wicket.markup.html.IHeaderContributor;
import wicket.markup.html.IHeaderResponse;
import wicket.markup.html.basic.Label;
import wicket.markup.html.form.Button;
import wicket.markup.html.form.CheckBox;
import wicket.markup.html.form.Form;
import wicket.markup.html.form.FormComponentLabel;
import wicket.markup.html.form.PasswordTextField;
import wicket.markup.html.form.TextField;
import wicket.markup.html.form.persistence.CookieValuePersister;
import wicket.markup.html.form.persistence.IValuePersister;
import wicket.markup.html.panel.FeedbackPanel;
import wicket.model.CompoundPropertyModel;
import wicket.model.Model;
import wicket.protocol.http.WebApplication;
import dk.teachus.frontend.TeachUsSession;

public class SignInPage extends UnAuthenticatedBasePage {
	private static final long serialVersionUID = 1L;
	
	private User user;
	
	public SignInPage() {		
		user = new User();
		final Form signInForm = new Form("signInForm", new CompoundPropertyModel(user)) { //$NON-NLS-1$
			private static final long serialVersionUID = 1L;

			@Override
			protected void onSubmit() {				
				signin();
			}
		};
		add(signInForm);
		
		signInForm.add(new Label("signInLabel", TeachUsSession.get().getString("SignInPage.signIn"))); //$NON-NLS-1$ //$NON-NLS-2$
		
		signInForm.add(new FeedbackPanel("feedback"));
		
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
//		signInForm.add(new ("signIn", Resources.SIGN_IN)); //$NON-NLS-1$
		
		add(new HeaderContributor(new IHeaderContributor() {
			private static final long serialVersionUID = 1L;

			public void renderHead(IHeaderResponse response) {
				response.renderJavascript("window.onload = function() {$('"+signInForm.getMarkupId()+"').focusFirstElement();}", "js1"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			}
		}));
		
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

	protected PageCategory getPageCategory() {
		return null;
	}

	protected String getPageLabel() {
		return null;
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

}
