package dk.teachus.frontend.pages.settings;

import dk.teachus.domain.Person;
import dk.teachus.frontend.TeachUsSession;
import dk.teachus.frontend.components.person.TeacherPanel;
import dk.teachus.frontend.models.TeacherModel;

public class TeacherSettingsPage extends AbstractSettingsPage {
	private static final long serialVersionUID = 1L;

	public TeacherSettingsPage() {
		add(new TeacherPanel("teacherPanel", new TeacherModel(TeachUsSession.get().getPerson().getId())) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void onSave(Person person) {
				TeachUsSession.get().signIn(person.getUsername(), person.getPassword());
			}
		});
	}

	@Override
	protected String getPageLabel() {
		return TeachUsSession.get().getString("Settings.personalInformation");
	}

}
