package dk.teachus.frontend.components;

import dk.teachus.dao.BookingDAO;
import dk.teachus.domain.PupilBooking;
import dk.teachus.frontend.TeachUsApplication;
import dk.teachus.frontend.utils.Resources;
import wicket.AttributeModifier;
import wicket.Component;
import wicket.RequestCycle;
import wicket.ResourceReference;
import wicket.ajax.AjaxRequestTarget;
import wicket.ajax.markup.html.AjaxLink;
import wicket.markup.html.WebComponent;
import wicket.markup.html.panel.Panel;
import wicket.model.AbstractReadOnlyModel;
import wicket.model.IModel;

public class PaidPanel extends Panel {
	private static final long serialVersionUID = 1L;

	public PaidPanel(String id, final IModel model) {
		super(id, model);
		
		AjaxLink link = new AjaxLink("link") { //$NON-NLS-1$
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(AjaxRequestTarget target) {
				PupilBooking pupilBooking = (PupilBooking) model.getObject(PaidPanel.this);
				
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
		public Object getObject(Component component) {
			PupilBooking pupilBooking = (PupilBooking) rowModel.getObject(component);
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
