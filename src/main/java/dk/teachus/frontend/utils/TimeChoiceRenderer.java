package dk.teachus.frontend.utils;

import java.util.Date;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;

import wicket.markup.html.form.ChoiceRenderer;

public class TimeChoiceRenderer extends ChoiceRenderer {
	private static final DateTimeFormatter FORMAT_TIME = Formatters.getFormatTime();
	private static final long serialVersionUID = 1L;

	@Override
	public Object getDisplayValue(Object object) {
		String display = ""; //$NON-NLS-1$
		
		if (object != null) {
			if (object instanceof Integer) {
				Integer minutesOfDay = (Integer) object;
				DateTime dt = new DateTime().withTime(0, 0, 0, 0).plusMinutes(minutesOfDay);
				display = FORMAT_TIME.print(dt);
			} else if (object instanceof Date) {
				Date date = (Date) object;
				display = FORMAT_TIME.print(new DateTime(date));
			}
		}
		
		return display;
	}
}
