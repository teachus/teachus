package dk.teachus.frontend.models;

import dk.teachus.domain.Pupil;
import dk.teachus.domain.Teacher;
import dk.teachus.domain.impl.PupilImpl;
import dk.teachus.frontend.TeachUsSession;

public class PupilModel extends PersonModel<Pupil> {
	private static final long serialVersionUID = 1L;

	public PupilModel(Long personId) {
		super(personId);
	}

	@Override
	protected Pupil createNewPerson() {
		PupilImpl pupil = new PupilImpl();
		
		if (TeachUsSession.get().getPerson() instanceof Teacher) {
			pupil.setTeacher((Teacher) TeachUsSession.get().getPerson());
		}
		
		return pupil;
	}

}
