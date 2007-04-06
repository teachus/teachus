package dk.teachus.frontend.utils;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.DateTimeFormatterBuilder;
import org.joda.time.format.ISODateTimeFormat;

import wicket.Session;
import dk.teachus.domain.Person;
import dk.teachus.domain.Pupil;
import dk.teachus.domain.Teacher;
import dk.teachus.frontend.TeachUsSession;

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

	public static DateTimeFormatter getFormatShortPrettyDate() {
		return new DateTimeFormatterBuilder()
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
		NumberFormat currencyFormat = null;
		
		Person person = TeachUsSession.get().getPerson();
		if (person instanceof Teacher) {
			Teacher teacher = (Teacher) person;
			currencyFormat = createCurrencyFormat(teacher);
		} else if (person instanceof Pupil) {
			Pupil pupil = (Pupil) person;
			Teacher teacher = pupil.getTeacher();
			currencyFormat = createCurrencyFormat(teacher);
		} else {
			NumberFormat.getNumberInstance(Session.get().getLocale());
		}		
		
		return currencyFormat;
	}

	private static NumberFormat createCurrencyFormat(Teacher teacher) {
		NumberFormat currencyFormat;
		DecimalFormatSymbols symbols = new DecimalFormatSymbols(Session.get().getLocale());
		symbols.setCurrencySymbol(teacher.getCurrency() != null ? teacher.getCurrency() : "");
		currencyFormat = new DecimalFormat("\u00A4 #,##0.00", symbols);
		return currencyFormat;
	}

	public static DateTimeFormatter getFormatOnlyMinutes() {
		return DateTimeFormat
				.forPattern(":mm")
				.withLocale(Session.get().getLocale());
	}
	
}
