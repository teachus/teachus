package dk.teachus.frontend.components.form;

import java.util.Locale;

import org.apache.wicket.util.convert.converter.AbstractConverter;
import org.joda.time.DateMidnight;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class DateMidnightConverter extends AbstractConverter<DateMidnight> {
	private static final long serialVersionUID = 1L;
	
	private static final DateTimeFormatter FORMATTER = DateTimeFormat.forPattern("yyyy-MM-dd");
	
	@Override
	public DateMidnight convertToObject(String value, Locale locale) {
		return FORMATTER.parseDateTime(value).toDateMidnight();
	}
	
	@Override
	public String convertToString(DateMidnight value, Locale locale) {
		if (value == null) {
			return null;
		}
		
		return FORMATTER.print(value);
	}
	
	@Override
	protected Class<DateMidnight> getTargetType() {
		return DateMidnight.class;
	}
	
}
