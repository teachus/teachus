package dk.teachus.frontend.utils;

import org.apache.wicket.markup.html.form.ChoiceRenderer;

import dk.teachus.backend.domain.MessageState;
import dk.teachus.frontend.TeachUsSession;

public class MessageStateChoiceRenderer extends ChoiceRenderer {
	private static final long serialVersionUID = 1L;
	
	@Override
	public Object getDisplayValue(Object object) {
		String display = null;
		
		if (object != null) {
			if (object instanceof MessageState) {
				MessageState state = (MessageState) object;
				
				switch (state) {
					case DRAFT:
						display = TeachUsSession.get().getString("MessageStateChoiceRenderer.draft"); //$NON-NLS-1$
						break;
					case FINAL:
						display = TeachUsSession.get().getString("MessageStateChoiceRenderer.final"); //$NON-NLS-1$
						break;
					case SENT:
						display = TeachUsSession.get().getString("MessageStateChoiceRenderer.sent"); //$NON-NLS-1$
						break;
					case ERROR_SENDING_INVALID_RECIPIENT:
						display = TeachUsSession.get().getString("MessageStateChoiceRenderer.invalidEmailAddress"); //$NON-NLS-1$
						break;
					case ERROR_SENDING_UNKNOWN:
						display = TeachUsSession.get().getString("MessageStateChoiceRenderer.sendingError"); //$NON-NLS-1$
						break;
					default:
						display = state.name();
						break;
				}
			}
		}
		
		return display;
	}
	
}
