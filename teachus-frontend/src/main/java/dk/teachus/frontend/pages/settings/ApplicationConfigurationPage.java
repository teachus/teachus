package dk.teachus.frontend.pages.settings;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.basic.Label;

import dk.teachus.backend.domain.ApplicationConfiguration;
import dk.teachus.frontend.TeachUsSession;
import dk.teachus.frontend.UserLevel;
import dk.teachus.frontend.components.form.ButtonPanelElement;
import dk.teachus.frontend.components.form.FormPanel;
import dk.teachus.frontend.components.form.TextFieldElement;
import dk.teachus.frontend.models.ApplicationConfigurationModel;
import dk.teachus.frontend.pages.AuthenticatedBasePage;

public class ApplicationConfigurationPage extends AuthenticatedBasePage {
	private static final long serialVersionUID = 1L;
	
	private Label saveLabel;
	
	public ApplicationConfigurationPage() {
		super(UserLevel.ADMIN);
		
		saveLabel = new Label("saveLabel", ""); //$NON-NLS-1$ //$NON-NLS-2$
		saveLabel.setOutputMarkupId(true);
		add(saveLabel);
		
		FormPanel form = new FormPanel("settingsForm"); //$NON-NLS-1$
		add(form);
		
		form.addElement(new TextFieldElement(TeachUsSession.get().getString("ApplicationConfigurationPage.serverUrl"), new ApplicationConfigurationModel(ApplicationConfiguration.SERVER_URL), true, 40)); //$NON-NLS-1$
		
		form.addElement(new ButtonPanelElement() {
			private static final long serialVersionUID = 1L;

			@Override
			protected void onCancel(AjaxRequestTarget target) {
				setResponsePage(ApplicationConfigurationPage.class);
			}

			@Override
			protected void onSave(AjaxRequestTarget target) {
				saveLabel.setModelObject(TeachUsSession.get().getString("ApplicationConfigurationPage.settingsSaved")); //$NON-NLS-1$
				target.addComponent(saveLabel);
			}			
		});
	}

	@Override
	protected AuthenticatedPageCategory getPageCategory() {
		return AuthenticatedPageCategory.GLOBAL_CONFIGURATION;
	}

	@Override
	protected String getPageLabel() {
		return TeachUsSession.get().getString("General.globalConfiguration"); //$NON-NLS-1$
	}

}
