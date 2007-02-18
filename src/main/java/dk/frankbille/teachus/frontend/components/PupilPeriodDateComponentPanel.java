package dk.frankbille.teachus.frontend.components;

import org.joda.time.DateTime;

import wicket.ResourceReference;
import wicket.ajax.AjaxRequestTarget;
import wicket.ajax.markup.html.AjaxLink;
import wicket.behavior.AttributeAppender;
import wicket.behavior.SimpleAttributeModifier;
import wicket.markup.html.WebComponent;
import wicket.markup.html.image.Image;
import wicket.markup.html.panel.Panel;
import wicket.model.Model;
import dk.frankbille.teachus.dao.BookingDAO;
import dk.frankbille.teachus.domain.Period;
import dk.frankbille.teachus.domain.Pupil;
import dk.frankbille.teachus.domain.PupilBooking;
import dk.frankbille.teachus.frontend.TeachUsApplication;
import dk.frankbille.teachus.frontend.pages.CalendarPage;

public class PupilPeriodDateComponentPanel extends Panel {
	private static final long serialVersionUID = 1L;

	public PupilPeriodDateComponentPanel(String id, final PupilBooking pupilBooking, final Pupil pupil, final Period period, DateTime time) {
		super(id);
		
		if (pupilBooking != null
				&& pupilBooking.getPupil().getId().equals(pupil.getId()) == false) {
			add(new WebComponent("link").setVisible(false)); //$NON-NLS-1$
			add(new Image("icon", new ResourceReference(CalendarPage.class, "resources/occupied.png"))); //$NON-NLS-1$ //$NON-NLS-2$
		} else {
			final DateTime dt = time;
			AjaxLink link = new AjaxLink("link") { //$NON-NLS-1$
				private static final long serialVersionUID = 1L;

				private PupilBooking booking = pupilBooking;
				
				@Override
				public void onClick(AjaxRequestTarget target) {
					BookingDAO bookingDAO = TeachUsApplication.get().getBookingDAO();
					
					if (booking == null) {
						booking = bookingDAO.createBookingObject();
						
						booking.setDate(dt.toDate());
						booking.setPupil(pupil);
						booking.setPeriod(period);

						bookingDAO.bookPupil(booking);

						this.add(new AttributeAppender("class", new Model("selected"), " ")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
					} else {
						bookingDAO.deleteBooking(booking);
						booking = null;
						
						this.add(new SimpleAttributeModifier("class", "")); //$NON-NLS-1$ //$NON-NLS-2$
					}
												
					target.addComponent(this);
				}					
			};
			if (pupilBooking != null) {
				link.add(new AttributeAppender("class", new Model("selected"), " ")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			}
			link.setOutputMarkupId(true);
			add(link);
			add(new WebComponent("icon").setVisible(false)); //$NON-NLS-1$
		}
	}

}
