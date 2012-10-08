package dk.teachus.frontend.ical;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

import javax.servlet.http.HttpServletResponse;

import org.apache.wicket.request.http.WebResponse;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.request.resource.IResource;
import org.apache.wicket.request.resource.ResourceReference;
import org.apache.wicket.util.string.Strings;
import org.joda.time.DateMidnight;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import dk.teachus.backend.dao.BookingDAO;
import dk.teachus.backend.domain.Booking;
import dk.teachus.backend.domain.Bookings;
import dk.teachus.backend.domain.Person;
import dk.teachus.backend.domain.Pupil;
import dk.teachus.backend.domain.PupilBooking;
import dk.teachus.backend.domain.Teacher;
import dk.teachus.backend.domain.impl.TimeZoneAttribute;
import dk.teachus.frontend.TeachUsApplication;
import dk.teachus.frontend.TeachUsSession;

public class IcalResource extends ResourceReference {
	private static final long serialVersionUID = 1L;
	
	public static final IcalResource RESOURCE = new IcalResource("ical");
	
	private static final String CRLF = "\r\n";
	private static final DateTimeFormatter DATE_FORMAT = DateTimeFormat.forPattern("yyyyMMdd'T'HHmmss'Z'");
	
	private IcalResource(String name) {
		super(name);
	}
	
	@Override
	public IResource getResource() {
		return new IResource() {
			private static final long serialVersionUID = 1L;

			@Override
			public void respond(Attributes attributes) {
				PageParameters pageParameters = attributes.getParameters();
				WebResponse response = (WebResponse) attributes.getResponse();
				
				String username = pageParameters.get(0).toString();
				String privateKey = pageParameters.get(1).toString();
				
				TeachUsSession teachUsSession = TeachUsSession.get();
				teachUsSession.signInWithPrivateKey(username, privateKey);
				if (teachUsSession.isAuthenticated()) {
					Person person = teachUsSession.getPerson();
					
					// Create response
					StringBuilder b = new StringBuilder();
					
					b.append("BEGIN:VCALENDAR").append(CRLF);
					b.append("PRODID:-//TeachUs////EN").append(CRLF);
					b.append("VERSION:2.0").append(CRLF);
					
					// Calendar name
					String calendarName = "";
					if (person instanceof Teacher) {
						calendarName = teachUsSession.getString("Ical.teacherCalendarName");
						calendarName = calendarName.replace("{teacherName}", person.getName());
					} else if (person instanceof Pupil) {
						Pupil pupil = (Pupil) person;
						calendarName = teachUsSession.getString("Ical.pupilCalendarName");
						calendarName = calendarName.replace("{teacherName}", pupil.getTeacher().getName());
					}
					b.append("X-WR-CALNAME:").append(calendarName).append(CRLF);
				
					// Search for upcoming events for this person
					BookingDAO bookingDAO = TeachUsApplication.get().getBookingDAO();
					List<PupilBooking> pupilBookings = new ArrayList<PupilBooking>();
					if (person instanceof Pupil) {
						Pupil pupil = (Pupil) person;
						Bookings bookings = bookingDAO.getBookings(pupil, new DateMidnight().minusWeeks(1), new DateMidnight().plusYears(1));
						List<Booking> bookingList = bookings.getBookingList();
						for (Booking booking : bookingList) {
							if (booking instanceof PupilBooking && booking.isActive()) {
								PupilBooking pupilBooking = (PupilBooking) booking;
								pupilBookings.add(pupilBooking);
							}
						}
					} else if (person instanceof Teacher) {
						Teacher teacher = (Teacher) person;
						Bookings bookings = bookingDAO.getBookings(teacher, new DateMidnight().minusWeeks(1), new DateMidnight().plusYears(1));
						List<Booking> bookingList = bookings.getBookingList();
						for (Booking booking : bookingList) {
							if (booking instanceof PupilBooking && booking.isActive()) {
								PupilBooking pupilBooking = (PupilBooking) booking;
								pupilBookings.add(pupilBooking);
							}
						}
					}
					
					for (PupilBooking pupilBooking : pupilBookings) {
						b.append("BEGIN:VEVENT").append(CRLF);
						
						DateTime startTime = pupilBooking.getDate();
						TimeZone teacherTimeZone = teachUsSession.getTeacherAttribute(TimeZoneAttribute.class).getTimeZone();
						startTime = startTime.withZoneRetainFields(DateTimeZone.forTimeZone(teacherTimeZone));
						startTime = startTime.withZone(DateTimeZone.UTC);
						
						// UID
						b.append("UID:");		
						b.append(DATE_FORMAT.print(startTime));
						b.append("-");
						b.append(pupilBooking.getId());
						b.append("@TeachUs");
						b.append(CRLF);
						
						// Date start
						b.append("DTSTART:");
						b.append(DATE_FORMAT.print(startTime));
						b.append(CRLF);
						
						// Date end		
						b.append("DTEND:");
						DateTime endTime = startTime.plusMinutes(pupilBooking.getPeriod().getLessonDuration());
						b.append(DATE_FORMAT.print(endTime));
						b.append(CRLF);

						// Location 
						if (Strings.isEmpty(pupilBooking.getPeriod().getLocation()) == false) {
							b.append("LOCATION:");
							b.append(pupilBooking.getPeriod().getLocation());
							b.append(CRLF);
						}
						
						// Summary
						b.append("SUMMARY:");
						String summary = "";
						if (person instanceof Teacher) {
							summary = teachUsSession.getString("Ical.teacherSummary");
							summary = summary.replace("{pupilName}", pupilBooking.getPupil().getName());
						} else if (person instanceof Pupil) {
							summary = teachUsSession.getString("Ical.pupilSummary");
							summary = summary.replace("{teacherName}", pupilBooking.getTeacher().getName());
						}
						b.append(summary);
						b.append(CRLF);
						
						b.append("END:VEVENT").append(CRLF);
					}
					
					b.append("END:VCALENDAR").append(CRLF); 
					
					response.setContentType("text/calendar; charset=UTF-8");
					response.setHeader("Content-Disposition", "attachment; filename="+person.getUsername()+".ics");
					try {
						byte[] bytes = b.toString().getBytes("UTF-8");
						response.setContentLength(bytes.length);
						response.write(bytes);
					} catch (UnsupportedEncodingException e) {
						throw new RuntimeException(e);
					}
				} else {
					response.setStatus(HttpServletResponse.SC_FORBIDDEN);
					response.setContentType("text/plain; charset=UTF-8");
					response.write("");
				}
			}
		};
	}
	
}
