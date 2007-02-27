package dk.teachus.frontend.utils;

import dk.teachus.frontend.TeachUsSession;
import wicket.markup.html.form.ChoiceRenderer;

public class YesNoRenderer extends ChoiceRenderer {
	private static final long serialVersionUID = 1L;

	@Override
	public Object getDisplayValue(Object object) {
		String display = "";
		
		if (object != null) {
			if (object instanceof Boolean) {
				Boolean bool = (Boolean) object;
				
				if (bool) {
					display = TeachUsSession.get().getString("General.yes");
				} else {
					display = TeachUsSession.get().getString("General.no");
				}
			}
		}
		
		return display;
	}
}
