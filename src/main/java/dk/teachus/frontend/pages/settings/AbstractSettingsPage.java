package dk.teachus.frontend.pages.settings;

import java.util.ArrayList;
import java.util.List;

import dk.teachus.frontend.TeachUsSession;
import dk.teachus.frontend.UserLevel;
import dk.teachus.frontend.components.Toolbar;
import dk.teachus.frontend.components.Toolbar.ToolbarItem;
import dk.teachus.frontend.pages.AuthenticatedBasePage;

public abstract class AbstractSettingsPage extends AuthenticatedBasePage {

	public AbstractSettingsPage() {
		super(UserLevel.TEACHER, true);
		
		List<ToolbarItem> items = new ArrayList<ToolbarItem>();
		items.add(new ToolbarItem(TeachUsSession.get().getString("Settings.personalInformation")) { //$NON-NLS-1$
			private static final long serialVersionUID = 1L;

			@Override
			public void onEvent() {
				getRequestCycle().setResponsePage(TeacherSettingsPage.class);
			}
		});
		items.add(new ToolbarItem(TeachUsSession.get().getString("TeacherSettingsPage.introductionMailText")) { //$NON-NLS-1$
			private static final long serialVersionUID = 1L;

			@Override
			public void onEvent() {
				getRequestCycle().setResponsePage(WelcomeMailSettingsPage.class);
			}			
		});
		
		add(new Toolbar("toolbar", items)); //$NON-NLS-1$
	}

	@Override
	protected AuthenticatedPageCategory getPageCategory() {
		return AuthenticatedPageCategory.SETTINGS;
	}

}
