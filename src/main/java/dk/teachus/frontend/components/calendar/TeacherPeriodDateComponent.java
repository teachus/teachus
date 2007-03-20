package dk.teachus.frontend.components.calendar;

import java.util.Date;

import org.joda.time.DateMidnight;
import org.joda.time.DateTime;

import wicket.MarkupContainer;
import wicket.markup.html.link.Link;
import dk.teachus.dao.BookingDAO;
import dk.teachus.domain.Booking;
import dk.teachus.domain.Bookings;
import dk.teachus.domain.Period;
import dk.teachus.domain.Pupil;
import dk.teachus.domain.PupilBooking;
import dk.teachus.domain.Teacher;
import dk.teachus.domain.TeacherBooking;
import dk.teachus.frontend.pages.calendar.PupilCalendarPage;

public class TeacherPeriodDateComponent extends BookingPeriodDateComponent {
	private static final long serialVersionUID = 1L;

	private Teacher teacher;

	public TeacherPeriodDateComponent(String id, Teacher teacher, Period period, DateMidnight date, Bookings bookings) {
		super(id, period, date, bookings);
		
		this.teacher = teacher;
	}

	@Override
	protected Booking createNewBookingObject(BookingDAO bookingDAO) {
		TeacherBooking teacherBooking = bookingDAO.createTeacherBookingObject();
		teacherBooking.setTeacher(teacher);
		return teacherBooking;
	}

	@Override
	protected boolean isChangeable(Booking booking) {
		return booking instanceof TeacherBooking;
	}

	@Override
	protected boolean shouldDisplayStringInsteadOfOccupiedIcon() {
		return true;
	}

	@Override
	protected boolean mayChangeBooking(DateTime dateTime) {
		return true;
	}
	
	@Override
	protected MarkupContainer getBookingDisplayStringLink(String linkId, Booking booking) {
		MarkupContainer displayLink = null;
		
		if (booking instanceof PupilBooking) {
			PupilBooking pupilBooking = (PupilBooking) booking;
			final Pupil pupil = pupilBooking.getPupil();
			final Date date = pupilBooking.getDate();
			
			displayLink = new Link(linkId) {
				private static final long serialVersionUID = 1L;

				@Override
				public void onClick() {
					getRequestCycle().setResponsePage(new PupilCalendarPage(date, pupil));
				}				
			};
		}
		
		return displayLink;
	}
	
	@Override
	protected BookingPeriodDateComponent createNewInstance(String id, Period period, DateMidnight date, Bookings bookings) {
		return new TeacherPeriodDateComponent(id, teacher, period, date, bookings);
	}
}
