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
package dk.teachus.frontend.pages;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.joda.time.DateMidnight;
import org.joda.time.LocalTime;

import dk.teachus.backend.dao.BookingDAO;
import dk.teachus.backend.domain.Booking;
import dk.teachus.backend.domain.Bookings;
import dk.teachus.backend.domain.Period;
import dk.teachus.backend.domain.PupilBooking;
import dk.teachus.backend.domain.TeachUsDate;
import dk.teachus.backend.domain.Teacher;
import dk.teachus.frontend.TeachUsApplication;
import dk.teachus.frontend.TeachUsSession;
import dk.teachus.frontend.UserLevel;
import dk.teachus.frontend.components.list.FunctionItem;
import dk.teachus.frontend.components.list.FunctionsColumn;
import dk.teachus.frontend.components.list.LinkPropertyColumn;
import dk.teachus.frontend.components.list.ListPanel;
import dk.teachus.frontend.components.list.RendererPropertyColumn;
import dk.teachus.frontend.functions.CancelPubilBookingFunction;
import dk.teachus.frontend.models.PupilModel;
import dk.teachus.frontend.pages.persons.PupilPage;
import dk.teachus.frontend.utils.CurrencyChoiceRenderer;
import dk.teachus.frontend.utils.DateChoiceRenderer;
import dk.teachus.frontend.utils.TimeChoiceRenderer;

public class AgendaPage extends AuthenticatedBasePage {
	private static final long serialVersionUID = 1L;

	public AgendaPage() {
		super(UserLevel.TEACHER, true);
		
		final Teacher teacher = (Teacher) TeachUsSession.get().getPerson();
				
		
		BookingDAO bookingDAO = TeachUsApplication.get().getBookingDAO();
		Bookings bookings = bookingDAO.getBookings(teacher, TeachUsSession.get().createNewDate(new DateMidnight(2006, 1, 1)), TeachUsSession.get().createNewDate(new DateMidnight(2015, 12, 31)));
		List<Booking> bookingList = bookings.getBookingList();
		Collections.sort(bookingList, new Comparator<Booking>() {
			public int compare(Booking o1, Booking o2) {
				int compare = 0;
				
				if (o1 != null && o2 != null) {
					TeachUsDate d1 = o1.getDate();
					TeachUsDate d2 = o2.getDate();
					
					if (d1 != null && d2 != null) {
						compare = d1.compareTo(d2);
					} else if (d1 != null) {
						compare = -1;
					} else if (d2 != null) {
						compare = 1;
					}
				} else if (o1 != null) {
					compare = -1;
				} else if (o2 != null) {
					compare = 1;
				}
				
				return compare;
			}
		});
		for (Booking booking : bookingList) {
			if (booking.getPeriod().mayBook(booking.getDate()) == false) {
				System.out.println("This booking is wrong:");
				System.out.println(booking.getClass().getSimpleName()+" - "+booking.getDate());
				System.out.println("The period would have these booking times:");
				Period period = booking.getPeriod();
				LocalTime startTime = period.getStartTime().getLocalTime();
				while(startTime.isBefore(period.getEndTime().getLocalTime())) {
					System.out.println(startTime);
					startTime = startTime.plusMinutes(period.getIntervalBetweenLessonStart());
				}
			}
		}
		
		
		List<FunctionItem> functions = new ArrayList<FunctionItem>();
		functions.add(new CancelPubilBookingFunction() {
			private static final long serialVersionUID = 1L;

			@Override
			protected void onBookingCancelled() {
				getRequestCycle().setResponsePage(AgendaPage.class);
			}
		});
		
		IColumn[] columns = new IColumn[] {
				new LinkPropertyColumn(new Model(TeachUsSession.get().getString("General.pupil")), "pupil.name", "pupil.name") {
					private static final long serialVersionUID = 1L;

					@Override
					protected void onClick(Object rowModelObject) {
						PupilBooking booking = (PupilBooking) rowModelObject;
						getRequestCycle().setResponsePage(new PupilPage(new PupilModel(booking.getPupil().getId())));
					}
				},
				new RendererPropertyColumn(new Model(TeachUsSession.get().getString("General.date")), "date.date", "date.date", new DateChoiceRenderer()),
				new RendererPropertyColumn(new Model(TeachUsSession.get().getString("General.time")), "date.date", new TimeChoiceRenderer()),
				new RendererPropertyColumn(new Model(TeachUsSession.get().getString("General.phoneNumber")), "pupil.phoneNumber", "pupil.phoneNumber"),
				new RendererPropertyColumn(new Model(TeachUsSession.get().getString("General.price")), "period.price", "period.price", new CurrencyChoiceRenderer()),
				new FunctionsColumn(new Model(TeachUsSession.get().getString("General.functions")), functions)
		};
		
		IModel bookingsModel = new LoadableDetachableModel() {
			private static final long serialVersionUID = 1L;

			@Override
			protected Object load() {
				BookingDAO bookingDAO = TeachUsApplication.get().getBookingDAO();
				List<PupilBooking> bookings = bookingDAO.getFutureBookingsForTeacher(teacher);
				return bookings;
			}
			
		};
		
		add(new ListPanel("list", columns, new AgendaDataProvider(bookingsModel)));
	}

	@Override
	protected String getPageLabel() {
		return TeachUsSession.get().getString("General.agenda"); //$NON-NLS-1$
	}

	@Override
	protected AuthenticatedPageCategory getPageCategory() {
		return AuthenticatedPageCategory.AGENDA;
	}

}
