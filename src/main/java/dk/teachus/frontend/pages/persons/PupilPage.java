package dk.teachus.frontend.pages.persons;

import dk.teachus.domain.Pupil;
import dk.teachus.frontend.TeachUsSession;
import dk.teachus.frontend.UserLevel;
import dk.teachus.frontend.components.person.PersonPanel;
import dk.teachus.frontend.components.person.PupilPanel;

public class PupilPage extends PersonPage<Pupil> {
	private static final long serialVersionUID = 1L;

	public PupilPage() {
		this((Pupil) TeachUsSession.get().getPerson());
	}
	
	public PupilPage(Pupil pupil) {
		super(UserLevel.PUPIL, pupil);
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
	protected PersonPanel createPersonPanel(String wicketId, Pupil pupil) {
		return new PupilPanel(wicketId, pupil);
	}

}
