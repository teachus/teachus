package dk.teachus.frontend.pages.stats.admin;

import java.util.List;

import dk.teachus.backend.dao.StatisticsDAO;
import dk.teachus.backend.domain.PupilBooking;
import dk.teachus.backend.domain.TeachUsDate;
import dk.teachus.frontend.TeachUsApplication;
import dk.teachus.frontend.TeachUsSession;
import dk.teachus.frontend.utils.LocalizationUtils;

public class BookingLogProvider implements LogProvider {
	private static final long serialVersionUID = 1L;
	
	public void appendEntries(List<Entry> entries, TeachUsDate fromDate, TeachUsDate toDate) {
		StatisticsDAO statisticsDAO = TeachUsApplication.get().getStatisticsDAO();
		List<PupilBooking> bookings = statisticsDAO.getAllBookings(fromDate, toDate);
		
		for (PupilBooking pupilBooking : bookings) {
			if (pupilBooking.isActive()) {
				entries.add(createEntry(pupilBooking));
			} else {
				TeachUsDate createDate = pupilBooking.getCreateDate();
				TeachUsDate updateDate = pupilBooking.getUpdateDate();
				
				if (createDate.intervalMinutes(updateDate) > 60) {
					entries.add(createEntry(pupilBooking));
					entries.add(createDeleteEntry(pupilBooking));
				}
			}
		}
		
//		PersonDAO personDAO = TeachUsApplication.get().getPersonDAO();
//		BookingDAO bookingDAO = TeachUsApplication.get().getBookingDAO();
//		
//		List<Teacher> teachers = personDAO.getPersons(Teacher.class);
//		for (Teacher teacher : teachers) {
//			
//			Bookings bookings = bookingDAO.getBookings(teacher, fromDate, toDate);
//			List<Booking> bookingList = bookings.getBookingList();
//			for (Booking booking : bookingList) {
//				if (booking instanceof PupilBooking) {
//					PupilBooking pupilBooking = (PupilBooking) booking;
//					
//					if (pupilBooking.isActive()) {
//						entries.add(createEntry(pupilBooking));
//					} else {
//						TeachUsDate createDate = booking.getCreateDate();
//						TeachUsDate updateDate = booking.getUpdateDate();
//						
//						if (createDate.intervalMinutes(updateDate) > 60) {
//							entries.add(createEntry(pupilBooking));
//							entries.add(createDeleteEntry(pupilBooking));
//						}
//					}
//				}
//			}
//		}
	}
	
	private Entry createEntry(PupilBooking booking) {
		String text = TeachUsSession.get().getString("TeachersLogPage.booking.create");
		text = text.replace("{pupil.name}", booking.getPupil().getName());
		text = text.replace("{teacher.name}", booking.getTeacher().getName());
		text = LocalizationUtils.replaceDate(text, "bookingDate", booking.getDate());
		return new Entry(booking.getTeacher(), booking.getCreateDate().getDateTime(), text);
	}
	
	private Entry createDeleteEntry(PupilBooking booking) {
		String text = TeachUsSession.get().getString("TeachersLogPage.booking.delete");
		text = text.replace("{pupil.name}", booking.getPupil().getName());
		text = text.replace("{teacher.name}", booking.getTeacher().getName());
		text = LocalizationUtils.replaceDate(text, "bookingDate", booking.getDate());
		return new Entry(booking.getTeacher(), booking.getUpdateDate().getDateTime(), text);
	}
}