package dk.teachus.frontend.utils;

import org.apache.wicket.markup.html.form.ChoiceRenderer;

import dk.teachus.backend.domain.Period.Status;
import dk.teachus.frontend.TeachUsSession;

public class PeriodStatusRenderer extends ChoiceRenderer {
	private static final long serialVersionUID = 1L;

	@Override
	public Object getDisplayValue(Object object) {
		String display = null;
		
		if (object != null) {
			if (object instanceof Status) {
				Status status = (Status) object;
				
				if (status == Status.DELETED) {
					display = TeachUsSession.get().getString("PeriodStatusRenderer.deleted"); //$NON-NLS-1$
				} else if (status == Status.DRAFT) {
					display = TeachUsSession.get().getString("PeriodStatusRenderer.draft"); //$NON-NLS-1$
				} else if (status == Status.FINAL) {
					display = TeachUsSession.get().getString("PeriodStatusRenderer.active"); //$NON-NLS-1$
				}
			}
		}
		
		return display;
	}
	
}
