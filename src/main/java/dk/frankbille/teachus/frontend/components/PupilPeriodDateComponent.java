package dk.frankbille.teachus.frontend.components;

import java.util.Date;

import org.joda.time.DateTime;

import wicket.Component;

import dk.frankbille.teachus.domain.Period;
import dk.frankbille.teachus.domain.Pupil;
import dk.frankbille.teachus.domain.PupilBooking;

public class PupilPeriodDateComponent extends PeriodDateComponent {
	private static final long serialVersionUID = 1L;

	public PupilPeriodDateComponent(String id, Pupil pupil, Period period, Date d) {
		super(id, pupil, period, d);
	}

	@Override
	protected Component getTimeContent(String wicketId, PupilBooking pupilBooking, Pupil pupil, Period period, DateTime time) {
		return new PupilPeriodDateComponentPanel(wicketId, pupilBooking, pupil, period, time);
	}

}
