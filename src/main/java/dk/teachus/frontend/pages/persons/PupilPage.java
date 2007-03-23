package dk.teachus.frontend.pages.persons;

import dk.teachus.domain.Pupil;
import dk.teachus.frontend.TeachUsSession;
import dk.teachus.frontend.UserLevel;
import dk.teachus.frontend.components.person.PersonPanel;
import dk.teachus.frontend.components.person.PupilPanel;
import dk.teachus.frontend.models.PupilModel;

public class PupilPage extends PersonPage<PupilModel> {
	private static final long serialVersionUID = 1L;

	public PupilPage() {
		this(new PupilModel(((Pupil) TeachUsSession.get().getPerson()).getId()));
	}
	
	public PupilPage(PupilModel pupilModel) {
		super(UserLevel.PUPIL, pupilModel);
	}

	@Override
	protected AuthenticatedPageCategory getPageCategory() {
		if (TeachUsSession.get().getUserLevel() == UserLevel.PUPIL) {
			return AuthenticatedPageCategory.SETTINGS;
		} else {
			return AuthenticatedPageCategory.PUPILS;
		}
	}

	@Override
	protected PersonPanel createPersonPanel(String wicketId, PupilModel pupilModel) {
		return new PupilPanel(wicketId, pupilModel);
	}

}
