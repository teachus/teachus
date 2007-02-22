package dk.frankbille.teachus.frontend.utils;

import java.text.NumberFormat;

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.DateTimeFormatterBuilder;
import org.joda.time.format.ISODateTimeFormat;

import wicket.Session;
import dk.frankbille.teachus.frontend.TeachUsSession;

public abstract class Formatters {

	public static DateTimeFormatter getFormatIsoDate() {
		return ISODateTimeFormat.date();
	}

	public static DateTimeFormatter getFormatPrettyDate() {
		return new DateTimeFormatterBuilder()
				.appendDayOfWeekShortText()
				.appendLiteral(", ") //$NON-NLS-1$
				.appendDayOfMonth(1)
				.appendLiteral(". ") //$NON-NLS-1$
				.appendMonthOfYearShortText()
				.appendLiteral(' ')
				.appendYear(4, 4)
				.toFormatter()
				.withLocale(Session.get().getLocale());
	}

	public static DateTimeFormatter getFormatWeekDay() {
		return new DateTimeFormatterBuilder()
				.appendDayOfWeekText()
				.toFormatter()
				.withLocale(Session.get().getLocale());
	}

	public static DateTimeFormatter getFormatWeekDayShort() {
		return new DateTimeFormatterBuilder()
				.appendDayOfWeekShortText()
				.toFormatter()
				.withLocale(Session.get().getLocale());
	}

	public static DateTimeFormatter getFormatTime() {
		return DateTimeFormat
				.forStyle("-S") //$NON-NLS-1$
				.withLocale(Session.get().getLocale());
	}

	public static DateTimeFormatter getFormatMonthYear() {
		return new DateTimeFormatterBuilder()
				.appendYear(4, 4)
				.appendLiteral('-')
				.appendMonthOfYear(2)
				.toFormatter()
				.withLocale(Session.get().getLocale());
	}

	public static DateTimeFormatter getFormatMonth() {
		return new DateTimeFormatterBuilder()
				.appendMonthOfYearText()
				.appendLiteral(", ") //$NON-NLS-1$
				.appendYear(4, 4)
				.toFormatter()
				.withLocale(Session.get().getLocale());
	}
	
	public static DateTimeFormatter getFormatOnlyMonth() {
		return new DateTimeFormatterBuilder()
				.appendMonthOfYearText()
				.toFormatter()
				.withLocale(Session.get().getLocale());
	}

	public static DateTimeFormatter getFormatWeekOfYear() {
		return new DateTimeFormatterBuilder()
				.appendLiteral(TeachUsSession.get().getString("Formatters.week")) //$NON-NLS-1$
				.appendLiteral(" ") //$NON-NLS-1$
				.appendWeekOfWeekyear(1)
				.appendLiteral(", ") //$NON-NLS-1$
				.appendYear(4, 4)
				.toFormatter()
				.withLocale(Session.get().getLocale());
	}

	public static NumberFormat getFormatCurrency() {
		return NumberFormat.getCurrencyInstance(Session.get().getLocale());
	}
	
}
