package dk.frankbille.teachus.frontend.components;

import org.joda.time.DateTime;

import wicket.ajax.AjaxRequestTarget;
import wicket.ajax.markup.html.AjaxLink;
import wicket.behavior.AttributeAppender;
import wicket.behavior.SimpleAttributeModifier;
import wicket.markup.html.WebComponent;
import wicket.markup.html.image.Image;
import wicket.markup.html.panel.Panel;
import wicket.model.Model;
import dk.frankbille.teachus.dao.BookingDAO;
import dk.frankbille.teachus.domain.Booking;
import dk.frankbille.teachus.domain.Period;
import dk.frankbille.teachus.domain.Pupil;
import dk.frankbille.teachus.domain.PupilBooking;
import dk.frankbille.teachus.frontend.TeachUsApplication;
import dk.frankbille.teachus.frontend.TeachUsSession;
import dk.frankbille.teachus.frontend.UserLevel;
import dk.frankbille.teachus.frontend.utils.Resources;

public class PupilPeriodDateComponentPanel extends Panel {
	private static final long serialVersionUID = 1L;

	public PupilPeriodDateComponentPanel(String id, final Booking booking, final Pupil pupil, final Period period, DateTime time) {
		super(id);
		
		boolean occupied = false;
		if (booking != null) {
			if (booking instanceof PupilBooking) {
				PupilBooking pupilBooking = (PupilBooking) booking;
				occupied = pupilBooking.getPupil().getId().equals(pupil.getId()) == false;
			} else {
				occupied = true;
			}
		}
		
		if (occupied) {
			add(new WebComponent("link").setVisible(false)); //$NON-NLS-1$
			add(new Image("icon", Resources.OCCUPIED)); //$NON-NLS-1$ //$NON-NLS-2$
		} else {
			final PupilBooking pupilBooking = (PupilBooking) booking;
			
			final DateTime dt = time;
			// Check if the booking state may be changed
			if (mayChangeBooking(dt)) {
				createBookingLink(booking, pupil, period, pupilBooking, dt);
			} else {
				add(new WebComponent("link").setVisible(false)); //$NON-NLS-1$
				if (pupilBooking != null) {
					add(new Image("icon", Resources.BOOKED));
				} else {
					add(new Image("icon", Resources.AVAILABLE));
				}
			}
		}
	}

	private void createBookingLink(final Booking booking, final Pupil pupil, final Period period, final PupilBooking pupilBooking, final DateTime dt) {
		AjaxLink link = new AjaxLink("link") { //$NON-NLS-1$
			private static final long serialVersionUID = 1L;

			private PupilBooking booking = pupilBooking;
			
			@Override
			public void onClick(AjaxRequestTarget target) {
				BookingDAO bookingDAO = TeachUsApplication.get().getBookingDAO();
				
				if (booking == null) {
					booking = bookingDAO.createPupilBookingObject();
					
					booking.setDate(dt.toDate());
					booking.setPupil(pupil);
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
		add(new WebComponent("icon").setVisible(false)); //$NON-NLS-1$
	}
	
	private boolean mayChangeBooking(DateTime dateTime) {
		boolean mayChangeBooking = false;
		
		if (TeachUsSession.get().getUserLevel() == UserLevel.TEACHER) {
			mayChangeBooking = true;
		} else {
			DateTime today = new DateTime().withTime(23, 59, 59, 999);
			mayChangeBooking = dateTime.isAfter(today);
		}
		
		return mayChangeBooking;
	}

}
