package dk.teachus.frontend.utils;

import java.util.Locale;
import java.util.Properties;

import wicket.markup.html.form.ChoiceRenderer;
import dk.teachus.frontend.TeachUsSession;

public class LocaleChoiceRenderer extends ChoiceRenderer {
	private static final long serialVersionUID = 1L;

	@Override
	public Object getDisplayValue(Object object) {
		StringBuilder display= new StringBuilder();
		
		if (object != null) {
			if (object instanceof Locale) {
				Locale locale = (Locale) object;
				
				display.append(locale.getDisplayLanguage(locale));
				
				if ("singers".equals(locale.getVariant())) {
					display.append(" (");
					Properties resourceBundle = TeachUsSession.get().createResourceBundle(locale);
					display.append(resourceBundle.getProperty("General.pupils"));
					display.append(")");
				}
			}
		}
		
		return display;
	}
}
