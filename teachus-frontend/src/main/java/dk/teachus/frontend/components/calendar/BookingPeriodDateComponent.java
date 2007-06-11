/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package dk.teachus.frontend.components.calendar;

import org.joda.time.DateMidnight;
import org.joda.time.DateTime;

import wicket.Component;
import wicket.MarkupContainer;
import wicket.ajax.AjaxRequestTarget;
import wicket.behavior.AttributeAppender;
import wicket.behavior.SimpleAttributeModifier;
import wicket.markup.html.WebComponent;
import wicket.markup.html.basic.Label;
import wicket.markup.html.image.Image;
import wicket.model.Model;
import dk.teachus.backend.dao.BookingDAO;
import dk.teachus.backend.domain.Booking;
import dk.teachus.backend.domain.Bookings;
import dk.teachus.backend.domain.Period;
import dk.teachus.backend.domain.PupilBooking;
import dk.teachus.frontend.TeachUsApplication;
import dk.teachus.frontend.components.BlockingAjaxLink;
import dk.teachus.frontend.utils.Resources;

public abstract class BookingPeriodDateComponent extends PeriodDateComponent {
	private static final String LINK_ID = "link";
	private static final String DISPLAYLINK_ID = "displayLink";
	private static final String LABEL_ID = "label";
	private static final String ICON_ID = "icon";
	
	private Bookings bookings;
	
	public BookingPeriodDateComponent(String id, final Period period, DateMidnight date, Bookings bookings) {
		super(id, period, date);
		
		setOutputMarkupId(true);
		
		this.bookings = bookings;
	}

	@Override
	protected final Component getTimeContent(String wicketId, final Period period, final DateTime time, final MarkupContainer contentContainer) {
		final Booking booking = bookings.getBooking(time);
		
		BookingPeriodDateComponentPanel bookingPanel = new BookingPeriodDateComponentPanel(wicketId);
		
		// Configuration from the implementation
		boolean changeable = isChangeable(booking);
		boolean displayString = shouldDisplayStringInsteadOfOccupiedIcon();
		
		// Components which must be instantiated
		Component displayLink = null;
		Component link = null;
		Component icon = null;
		Component label = null;
		
		if (booking != null && changeable == false) {			
			if (displayString) {
				MarkupContainer bookingDisplayStringLink = getBookingDisplayStringLink(DISPLAYLINK_ID, booking);
				if (bookingDisplayStringLink == null) {
					link = createInvisibleLink(); 
					icon = createInvisibleIcon(); 
					label = getLabelWithDisplayString(booking);
					displayLink = createInvisibleDisplayLink();
				} else {
					link = createInvisibleLink(); 
					icon = createInvisibleIcon(); 
					label = createInvisibleLabel();
					bookingDisplayStringLink.add(getLabelWithDisplayString(booking));
					displayLink = bookingDisplayStringLink;
				}
			} else {
				link = createInvisibleLink(); 
				displayLink = createInvisibleDisplayLink();
				if (mayChangeBooking(period, time)) {
					icon = new Image(ICON_ID, Resources.OCCUPIED);
					label = createInvisibleLabel(); 
				} else {
					icon = createInvisibleIcon();
					label = createEmptyLabel();
				}
			}
		} else {
			displayLink = createInvisibleDisplayLink();
			if (mayChangeBooking(period, time)) {
				link = createLink(period, time, booking);
				icon = createInvisibleIcon(); 
				label = createInvisibleLabel(); 
			} else {
				link = createInvisibleLink();
				if (booking != null) {
					icon = new Image(ICON_ID, Resources.BOOKED);			
					label = createInvisibleLabel();
				} else {
					icon = createInvisibleIcon();
					label = createEmptyLabel();
				}
			}
		}
		
		// Add the components to the bookingPanel
		bookingPanel.add(link);
		bookingPanel.add(icon);
		bookingPanel.add(label);
		bookingPanel.add(displayLink);
		
		return bookingPanel;
	}
	
	@Override
	protected boolean shouldDisplayTimeContent(Period period, DateTime time) {
		boolean shouldDisplayTimeContent = false;
		
		if (period.mayBook(time)) {
			if (bookings.getBooking(time) == null) {
				if (bookings.mayBook(period, time)) {
					shouldDisplayTimeContent = true;
				}
			} else {
				shouldDisplayTimeContent = true;
			}
		}
		
		return shouldDisplayTimeContent;
	}
	
	@Override
	protected int getRowSpanForTimeContent(Period period, DateTime time) {
		int rowSpan = 1;
		
		if (bookings.getBooking(time) != null) {
			rowSpan = period.getLessonDuration() / period.getIntervalBetweenLessonStart();
		}
		
		return rowSpan;
	}
	
	@Override
	protected boolean shouldHideEmptyContent(Period period, DateTime time) {
		return bookings.isBeforeBooking(period, time) == false;
	}

	private Label getLabelWithDisplayString(final Booking booking) {
		return new Label(LABEL_ID, getBookingDisplayString(booking));
	}

	private Component createInvisibleDisplayLink() {
		return new WebComponent(DISPLAYLINK_ID).setVisible(false);
	}

	private Component createInvisibleLink() {
		return new WebComponent(LINK_ID).setVisible(false);
	}

	private Component createInvisibleLabel() {
		return new WebComponent(LABEL_ID).setVisible(false);
	}

	private Component createInvisibleIcon() {
		return new WebComponent(ICON_ID).setVisible(false);
	}

	private Component createEmptyLabel() {
		return new Label(LABEL_ID, "&nbsp;").setEscapeModelStrings(false).setRenderBodyOnly(true);
	}

	private Component createLink(final Period period, final DateTime time, final Booking booking) {
		Component link;
		link = new BlockingAjaxLink(LINK_ID) { 
			private static final long serialVersionUID = 1L;
			
			private Booking localBooking = booking;
			
			@Override
			public void onClick(AjaxRequestTarget target) {
				BookingDAO bookingDAO = TeachUsApplication.get().getBookingDAO();
				
				if (localBooking == null) {
					localBooking = createNewBookingObject(bookingDAO);
					
					localBooking.setDate(time.toDate());
					localBooking.setPeriod(period);

					bookingDAO.book(localBooking);
					
					bookings.getBookingList().add(localBooking);

					this.add(new AttributeAppender("class", new Model("selected"), " ")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				} else {
					bookingDAO.deleteBooking(localBooking);
					bookings.getBookingList().remove(localBooking);
					localBooking = null;
					
					this.add(new SimpleAttributeModifier("class", "")); //$NON-NLS-1$ //$NON-NLS-2$
				}
											
				String id2 = BookingPeriodDateComponent.this.getId();
				Period period2 = BookingPeriodDateComponent.this.period;
				DateMidnight date2 = date;
				Bookings bookings2 = bookings;
				BookingPeriodDateComponent newInstance = createNewInstance(id2, period2, date2, bookings2);
				BookingPeriodDateComponent.this.getParent().replace(newInstance);
				target.addComponent(newInstance);
			}					
		};
		if (booking != null) {
			link.add(new AttributeAppender("class", new Model("selected"), " ")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		}
		
		link.setOutputMarkupId(true);
		return link;
	}

	protected abstract boolean isChangeable(Booking booking);
	
	protected abstract boolean mayChangeBooking(Period period, DateTime bookingStartTime);
	
	protected abstract boolean shouldDisplayStringInsteadOfOccupiedIcon();
	
	protected abstract Booking createNewBookingObject(BookingDAO bookingDAO);
	
	protected abstract BookingPeriodDateComponent createNewInstance(String id, Period period, DateMidnight date, Bookings bookings);
	
	protected MarkupContainer getBookingDisplayStringLink(String linkId, Booking booking) {
		return null;
	}
	
	protected String getBookingDisplayString(Booking booking) {
		String display = "";
		
		if (booking instanceof PupilBooking) {
			PupilBooking pupilBooking = (PupilBooking) booking;
			display = pupilBooking.getPupil().getName();
		} else {
			display = booking.getTeacher().getName();
		}
		
		return display;
	}
	
}
