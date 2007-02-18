package dk.frankbille.teachus.frontend.components;

import org.joda.time.DateTime;

import wicket.ajax.AjaxRequestTarget;
import wicket.ajax.markup.html.AjaxLink;
import wicket.behavior.AttributeAppender;
import wicket.behavior.SimpleAttributeModifier;
import wicket.markup.html.WebComponent;
import wicket.markup.html.basic.Label;
import wicket.markup.html.panel.Panel;
import wicket.model.Model;
import dk.frankbille.teachus.dao.BookingDAO;
import dk.frankbille.teachus.domain.Booking;
import dk.frankbille.teachus.domain.Period;
import dk.frankbille.teachus.domain.PupilBooking;
import dk.frankbille.teachus.domain.Teacher;
import dk.frankbille.teachus.domain.TeacherBooking;
import dk.frankbille.teachus.frontend.TeachUsApplication;

public class TeacherPeriodDateComponentPanel extends Panel {
	private static final long serialVersionUID = 1L;

	public TeacherPeriodDateComponentPanel(String id, final Teacher teacher, final Booking booking, final Period period, DateTime time) {
		super(id);
		
		if (booking != null && booking instanceof PupilBooking) {
			PupilBooking pupilBooking = (PupilBooking) booking;
			
			add(new WebComponent("link").setVisible(false)); //$NON-NLS-1$
			add(new Label("label", pupilBooking.getPupil().getName()));
		} else {
			final TeacherBooking teacherBooking;
			if (booking != null && booking instanceof TeacherBooking) {
				teacherBooking = (TeacherBooking) booking;
			} else {
				teacherBooking = null;
			}
			
			final DateTime dt = time;
			AjaxLink link = new AjaxLink("link") { //$NON-NLS-1$
				private static final long serialVersionUID = 1L;

				private TeacherBooking booking = teacherBooking;
				
				@Override
				public void onClick(AjaxRequestTarget target) {
					BookingDAO bookingDAO = TeachUsApplication.get().getBookingDAO();
					
					if (booking == null) {
						booking = bookingDAO.createTeacherBookingObject();
						
						booking.setDate(dt.toDate());
						booking.setTeacher(teacher);
						booking.setPeriod(period);

						bookingDAO.book(booking);

						this.add(new AttributeAppender("class", new Model("selected"), " ")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
					} else {
						bookingDAO.deleteBooking(booking);
						booking = null;
						
						this.add(new SimpleAttributeModifier("class", "")); //$NON-NLS-1$ //$NON-NLS-2$
					}
												
					target.addComponent(this);
				}					
			};
			if (booking != null) {
				link.add(new AttributeAppender("class", new Model("selected"), " ")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			}
			link.setOutputMarkupId(true);
			add(link);
			add(new WebComponent("label").setVisible(false)); //$NON-NLS-1$
		}
	}

}
