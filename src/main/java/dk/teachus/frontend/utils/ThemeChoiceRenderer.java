package dk.teachus.frontend.utils;

import dk.teachus.domain.Theme;
import dk.teachus.frontend.TeachUsSession;
import wicket.markup.html.form.ChoiceRenderer;

public class ThemeChoiceRenderer extends ChoiceRenderer {
	private static final long serialVersionUID = 1L;

	@Override
	public Object getDisplayValue(Object object) {
		String display = "";
		
		if (object != null) {
			if (object instanceof Theme) {
				Theme theme = (Theme) object;
				
				String localeString = "Theme."+theme.name().toLowerCase();
				display = TeachUsSession.get().getString(localeString);
			}
		}
		
		return display;
	}
	
}
