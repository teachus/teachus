package dk.teachus.frontend.components.calendar.v2;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.basic.MultiLineLabel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.PropertyModel;

import dk.teachus.backend.domain.PupilBooking;
import dk.teachus.frontend.TeachUsSession;

public class PeriodBookingTimeSlotDetails extends Panel {
	private static final long serialVersionUID = 1L;

	public PeriodBookingTimeSlotDetails(String id, PupilBooking pupilBooking) {
		super(id);
		
		add(new Label("name", new PropertyModel<String>(pupilBooking, "pupil.name"))); //$NON-NLS-1$ //$NON-NLS-2$
		add(new Label("telephoneLabel", TeachUsSession.get().getString("General.phoneNumber"))); //$NON-NLS-1$ //$NON-NLS-2$
		add(new Label("telephone", new PropertyModel<String>(pupilBooking, "pupil.phoneNumber"))); //$NON-NLS-1$ //$NON-NLS-2$
		add(new Label("emailLabel", TeachUsSession.get().getString("General.email"))); //$NON-NLS-1$ //$NON-NLS-2$
		add(new Label("email", new PropertyModel<String>(pupilBooking, "pupil.email"))); //$NON-NLS-1$ //$NON-NLS-2$
		add(new MultiLineLabel("notes", new PropertyModel<String>(pupilBooking, "pupil.notes"))); //$NON-NLS-1$ //$NON-NLS-2$
	}
	
}
