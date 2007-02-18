package dk.frankbille.teachus.frontend.utils;

import java.util.Date;

import org.joda.time.DateTime;

import wicket.markup.html.form.ChoiceRenderer;

public class DateChoiceRenderer extends ChoiceRenderer {
	private static final long serialVersionUID = 1L;

	@Override
	public Object getDisplayValue(Object object) {
		String display = ""; //$NON-NLS-1$
		
		if (object != null) {
			if (object instanceof Date) {
				Date date = (Date) object;
				display = Formatters.getFormatPrettyDate().print(new DateTime(date));
			}
		}
		
		return display;
	}
}
