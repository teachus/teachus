package dk.teachus.frontend.components.calendar;

import wicket.markup.html.panel.Panel;

public class BookingPeriodDateComponentPanel extends Panel {
	private static final long serialVersionUID = 1L;

	public BookingPeriodDateComponentPanel(String id) {
		super(id);		
	}

//	private void createBookingLink(final Booking booking, final Pupil pupil, final Period period, final PupilBooking pupilBooking, final DateTime dt) {
//		AjaxLink link = new AjaxLink("link") { //$NON-NLS-1$
//			private static final long serialVersionUID = 1L;
//
//			private PupilBooking booking = pupilBooking;
//			
//			@Override
//			public void onClick(AjaxRequestTarget target) {
//				BookingDAO bookingDAO = TeachUsApplication.get().getBookingDAO();
//				
//				if (booking == null) {
//					booking = bookingDAO.createPupilBookingObject();
//					
//					booking.setDate(dt.toDate());
//					booking.setPupil(pupil);
//					booking.setPeriod(period);
//
//					bookingDAO.book(booking);
//
//					this.add(new AttributeAppender("class", new Model("selected"), " ")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
//				} else {
//					bookingDAO.deleteBooking(booking);
//					booking = null;
//					
//					this.add(new SimpleAttributeModifier("class", "")); //$NON-NLS-1$ //$NON-NLS-2$
//				}
//											
//				target.addComponent(this);
//			}					
//		};
//		if (booking != null) {
//			link.add(new AttributeAppender("class", new Model("selected"), " ")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
//		}
//		link.setOutputMarkupId(true);
//		add(link);
//		add(new WebComponent("icon").setVisible(false)); //$NON-NLS-1$
//	}
//	
//	private boolean mayChangeBooking(DateTime dateTime) {
//		boolean mayChangeBooking = false;
//		
//		if (TeachUsSession.get().getUserLevel() == UserLevel.TEACHER) {
//			mayChangeBooking = true;
//		} else {
//			DateTime today = new DateTime().withTime(23, 59, 59, 999);
//			mayChangeBooking = dateTime.isAfter(today);
//		}
//		
//		return mayChangeBooking;
//	}
	
	

}
