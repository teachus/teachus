package dk.frankbille.teachus.frontend.utils;

import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;

import dk.frankbille.teachus.domain.impl.PeriodImpl.WeekDay;
import wicket.markup.html.form.ChoiceRenderer;

public class WeekDayChoiceRenderer extends ChoiceRenderer {
	private static final long serialVersionUID = 1L;
	
	public static enum Format {
		SHORT,
		LONG;
	}
	
	private Format format;
	
	public WeekDayChoiceRenderer(Format format) {
		this.format = format;
	}

	@SuppressWarnings("unchecked") //$NON-NLS-1$
	@Override
	public Object getDisplayValue(Object object) {
		StringBuilder display = new StringBuilder();
		DateTimeFormatter formatter = null;
		if (format == Format.LONG) {
			formatter = Formatters.getFormatWeekDay();
		} else if (format == Format.SHORT) {
			formatter = Formatters.getFormatWeekDayShort();
		}
		
		if (object != null) {
			if (object instanceof List) {
				DateTime dt = new DateTime();
				List<WeekDay> weekDays = (List<WeekDay>) object;
				
				for (WeekDay weekDay : weekDays) {
					if (display.length() > 0) {
						display.append(", "); //$NON-NLS-1$
					}
				
					dt = dt.withDayOfWeek(weekDay.getYodaWeekDay());
					display.append(formatter.print(dt));

				}
			} else if (object instanceof WeekDay) {
				WeekDay weekDay = (WeekDay) object;
				DateTime dt = new DateTime().withDayOfWeek(weekDay.getYodaWeekDay());
				display.append(formatter.print(dt));
			}
		}
		
		return display;
	}
}
