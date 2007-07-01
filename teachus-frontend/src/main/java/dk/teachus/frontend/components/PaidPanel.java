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
package dk.teachus.frontend.components;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.RequestCycle;
import org.apache.wicket.ResourceReference;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.WebComponent;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;

import dk.teachus.backend.dao.BookingDAO;
import dk.teachus.backend.domain.PupilBooking;
import dk.teachus.frontend.TeachUsApplication;
import dk.teachus.frontend.utils.Resources;

public class PaidPanel extends Panel {
	private static final long serialVersionUID = 1L;

	public PaidPanel(String id, final IModel model) {
		super(id, model);
		
		AjaxLink link = new BlockingAjaxLink("link") { //$NON-NLS-1$
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(AjaxRequestTarget target) {
				PupilBooking pupilBooking = (PupilBooking) model.getObject();
				
				BookingDAO bookingDAO = TeachUsApplication.get().getBookingDAO();
				bookingDAO.changePaidStatus(pupilBooking);
				
				target.addComponent(this);
			}
		};
		link.setOutputMarkupId(true);
		WebComponent image = new WebComponent("image"); //$NON-NLS-1$
		image.add(new AttributeModifier("src", true, new ImageModel(model))); //$NON-NLS-1$
		link.add(image);
		add(link);
	}
	
	private static class ImageModel extends AbstractReadOnlyModel {
		private static final long serialVersionUID = 1L;

		private IModel rowModel;
		
		public ImageModel(IModel rowModel) {
			this.rowModel = rowModel;
		}

		@Override
		public Object getObject() {
			PupilBooking pupilBooking = (PupilBooking) rowModel.getObject();
			ResourceReference icon;
			if (pupilBooking.isPaid()) {
				icon = Resources.PAID;
			} else {
				icon = Resources.UNPAID;
			}
			
			return RequestCycle.get().urlFor(icon);
		}		
	}
}
