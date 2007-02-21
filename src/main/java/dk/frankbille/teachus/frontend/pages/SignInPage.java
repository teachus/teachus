package dk.frankbille.teachus.frontend.pages;

import java.io.Serializable;

import wicket.behavior.HeaderContributor;
import wicket.markup.html.IHeaderContributor;
import wicket.markup.html.IHeaderResponse;
import wicket.markup.html.basic.Label;
import wicket.markup.html.form.Form;
import wicket.markup.html.form.ImageButton;
import wicket.markup.html.form.PasswordTextField;
import wicket.markup.html.form.TextField;
import wicket.markup.html.image.Image;
import wicket.model.CompoundPropertyModel;
import wicket.protocol.http.WebApplication;
import dk.frankbille.teachus.frontend.TeachUsSession;
import dk.frankbille.teachus.frontend.utils.Resources;

public class SignInPage extends BasePage {
	private static final long serialVersionUID = 1L;

	public SignInPage() {
		final User user = new User();
		final Form signInForm = new Form("signInForm", new CompoundPropertyModel(user)) { //$NON-NLS-1$
			private static final long serialVersionUID = 1L;

			@Override
			protected void onSubmit() {
				TeachUsSession teachUsSession = TeachUsSession.get();
				
				teachUsSession.signIn(user.getUsername(), user.getPassword());
				
				if (teachUsSession.isAuthenticated()) {
					getRequestCycle().setRedirect(true);
					getRequestCycle().setResponsePage(WebApplication.get().getHomePage());
				}
			}
		};
		add(signInForm);

		signInForm.add(new Image("signInIcon", Resources.LOCK)); //$NON-NLS-1$
		
		signInForm.add(new Label("signInLabel", TeachUsSession.get().getString("SignInPage.signIn"))); //$NON-NLS-1$ //$NON-NLS-2$
		
		signInForm.add(new Label("usernameLabel", TeachUsSession.get().getString("General.username"))); //$NON-NLS-1$ //$NON-NLS-2$
		final TextField username = new TextField("username"); //$NON-NLS-1$
		username.setOutputMarkupId(true);
		signInForm.add(username);
		
		signInForm.add(new Label("passwordLabel", TeachUsSession.get().getString("General.password"))); //$NON-NLS-1$ //$NON-NLS-2$
		signInForm.add(new PasswordTextField("password")); //$NON-NLS-1$
		
		signInForm.add(new ImageButton("signIn", Resources.SIGN_IN)); //$NON-NLS-1$
		
		add(new HeaderContributor(new IHeaderContributor() {
			private static final long serialVersionUID = 1L;

			public void renderHead(IHeaderResponse response) {
				response.renderJavascript("window.onload = function() {$('"+signInForm.getMarkupId()+"').focusFirstElement();}", "js1"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			}
		}));
	}

	public static class User implements Serializable {
		private static final long serialVersionUID = 1L;

		private String username;

		private String password;

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
