package dk.teachus.frontend.utils;

import java.text.NumberFormat;

import wicket.markup.html.form.ChoiceRenderer;
import dk.teachus.frontend.TeachUsSession;

public class PercentChoiceRenderer extends ChoiceRenderer {
	private static final long serialVersionUID = 1L;

	@Override
	public Object getDisplayValue(Object object) {
		String display = "";
		
		if (object != null) {
			if (object instanceof Double) {
				Double value = (Double) object;
				display = NumberFormat.getPercentInstance(TeachUsSession.get().getLocale()).format(value);
			}
		}
		
		return display;
	}
	
}
