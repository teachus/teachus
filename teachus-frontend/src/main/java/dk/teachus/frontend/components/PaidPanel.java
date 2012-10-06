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
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxFallbackLink;
import org.apache.wicket.markup.html.WebComponent;
import org.apache.wicket.markup.html.panel.GenericPanel;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;

import dk.teachus.backend.dao.BookingDAO;
import dk.teachus.backend.domain.PupilBooking;
import dk.teachus.frontend.TeachUsApplication;

public class PaidPanel extends GenericPanel<PupilBooking> {
	private static final long serialVersionUID = 1L;
	
	public PaidPanel(String id, final IModel<PupilBooking> model) {
		super(id, model);
		
		AjaxFallbackLink<PupilBooking> link = new BlockingAjaxLink<PupilBooking>("link", model) { //$NON-NLS-1$
			private static final long serialVersionUID = 1L;
			
			@Override
			public void onClick(AjaxRequestTarget target) {
				PupilBooking pupilBooking = getModelObject();
				
				BookingDAO bookingDAO = TeachUsApplication.get().getBookingDAO();
				bookingDAO.changePaidStatus(pupilBooking);
				
				if (target != null) {
					target.add(this);
				}
			}
		};
		link.setOutputMarkupId(true);
		WebComponent image = new WebComponent("icon"); //$NON-NLS-1$
		image.add(AttributeModifier.replace("class", new IconModel(model))); //$NON-NLS-1$
		link.add(image);
		add(link);
	}
	
	private static class IconModel extends AbstractReadOnlyModel<CharSequence> {
		private static final long serialVersionUID = 1L;
		
		private IModel<PupilBooking> rowModel;
		
		public IconModel(IModel<PupilBooking> rowModel) {
			this.rowModel = rowModel;
		}
		
		@Override
		public CharSequence getObject() {
			PupilBooking pupilBooking = rowModel.getObject();
			if (pupilBooking.isPaid()) {
				return "icon-ok";
			} else {
				return "icon-download-alt";
			}
		}
	}
}
