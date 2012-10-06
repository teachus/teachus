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
import java.util.List;

import org.apache.wicket.Application;
import org.apache.wicket.RestartResponseAtInterceptPageException;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;

import dk.teachus.backend.dao.BookingDAO;
import dk.teachus.backend.domain.Pupil;
import dk.teachus.backend.domain.PupilBooking;
import dk.teachus.backend.domain.Teacher;
import dk.teachus.frontend.TeachUsApplication;
import dk.teachus.frontend.TeachUsSession;
import dk.teachus.frontend.UserLevel;
import dk.teachus.frontend.components.list.FunctionItem;
import dk.teachus.frontend.components.list.FunctionsColumn;
import dk.teachus.frontend.components.list.ListPanel;
import dk.teachus.frontend.components.list.RendererPropertyColumn;
import dk.teachus.frontend.functions.CancelPubilBookingFunction;
import dk.teachus.frontend.functions.PaidFunction;
import dk.teachus.frontend.utils.CurrencyChoiceRenderer;
import dk.teachus.frontend.utils.DateChoiceRenderer;
import dk.teachus.frontend.utils.TimeChoiceRenderer;

public class PaymentPage extends AuthenticatedBasePage {
	private static final long serialVersionUID = 1L;

	public PaymentPage() {
		super(UserLevel.PUPIL);
		
		init();
	}
	
	private void init() {		
		List<FunctionItem> functions = new ArrayList<FunctionItem>();

		if (TeachUsSession.get().getUserLevel() == UserLevel.TEACHER) {
			functions.add(new PaidFunction());
		}
		
		functions.add(new CancelPubilBookingFunction() {
			private static final long serialVersionUID = 1L;

			@Override
			protected void onBookingCancelled() {
				getRequestCycle().setResponsePage(PaymentPage.class);
			}
		});
		
		List<IColumn> columns = new ArrayList<IColumn>();
		columns.add(new PropertyColumn(new Model(TeachUsSession.get().getString("General.pupil")), "pupil.name", "pupil.name")); //$NON-NLS-1$ //$NON-NLS-2$
		columns.add(new RendererPropertyColumn(new Model(TeachUsSession.get().getString("General.date")), "date", "date", new DateChoiceRenderer())); //$NON-NLS-1$ //$NON-NLS-2$
		columns.add(new RendererPropertyColumn(new Model(TeachUsSession.get().getString("General.time")), "date", new TimeChoiceRenderer())); //$NON-NLS-1$ //$NON-NLS-2$
		columns.add(new RendererPropertyColumn(new Model(TeachUsSession.get().getString("General.price")), "period.price", "period.price", new CurrencyChoiceRenderer())); //$NON-NLS-1$ //$NON-NLS-2$
		columns.add(new FunctionsColumn(new Model(TeachUsSession.get().getString("General.functions")), functions));
		
		IModel bookingsModel = new LoadableDetachableModel() {
			private static final long serialVersionUID = 1L;

			@Override
			protected Object load() {
				BookingDAO bookingDAO = TeachUsApplication.get().getBookingDAO();
				List<PupilBooking> pupilBookings = null;
				
				if (TeachUsSession.get().getPerson() instanceof Pupil) {
					pupilBookings = bookingDAO.getUnpaidBookings((Pupil) TeachUsSession.get().getPerson());
				} else if (TeachUsSession.get().getPerson() instanceof Teacher) {
					pupilBookings = bookingDAO.getUnpaidBookings((Teacher) TeachUsSession.get().getPerson());
				} else {
					throw new RestartResponseAtInterceptPageException(Application.get().getHomePage());
				}
				
				return pupilBookings;
			}
			
		};

		add(new ListPanel("list", columns, new PaymentDataProvider(bookingsModel)));
	}
	
	@Override
	protected AuthenticatedPageCategory getPageCategory() {
		return AuthenticatedPageCategory.PAYMENT;
	}

}
