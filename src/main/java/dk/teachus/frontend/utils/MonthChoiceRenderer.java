package dk.teachus.frontend.utils;

import org.joda.time.DateMidnight;

import wicket.markup.html.form.ChoiceRenderer;

public class MonthChoiceRenderer extends ChoiceRenderer {
	private static final long serialVersionUID = 1L;

	@Override
	public Object getDisplayValue(Object object) {
		String display = "";
		
		if (object != null) {
			if (object instanceof Integer) {
				Integer monthIndex = (Integer) object;
				DateMidnight month = new DateMidnight().withMonthOfYear(monthIndex);
				display = Formatters.getFormatOnlyMonth().print(month);
			}
		}
		
		return display;
	}
	
}
