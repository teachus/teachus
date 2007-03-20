package dk.teachus.frontend.models;

import dk.teachus.domain.Pupil;
import dk.teachus.domain.impl.PupilImpl;
import dk.teachus.frontend.TeachUsApplication;

public class PupilModel extends PersonModel<Pupil> {
	private static final long serialVersionUID = 1L;

	public PupilModel(Long personId) {
		super(personId);
	}

	@Override
	protected Pupil createNewPerson() {
		PupilImpl pupil = new PupilImpl();
		return pupil;
	}
	
	@Override
	protected void onSavedNewPerson(Pupil person) {
		TeachUsApplication.get().getMailBean().sendWelcomeMail(person, TeachUsApplication.get().getServerName());
	}

}
