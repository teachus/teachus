package dk.teachus.frontend.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import dk.teachus.backend.domain.TeachUsDate;
import dk.teachus.frontend.TeachUsSession;

public class LocalizationUtils {
	
	public static String replaceDate(String text, String dateKey, TeachUsDate date) {
		Pattern pattern = Pattern.compile(".*?(\\{"+dateKey+"\\|([^\\}]+)\\}).*?");
		Matcher matcher = pattern.matcher(text);
		while(matcher.find()) {
			String matcherKey = matcher.group(1);
			String format = matcher.group(2);
			
			DateTimeFormatter formatter = DateTimeFormat.forPattern(format).withLocale(TeachUsSession.get().getLocale());
			String formattedDate = formatter.print(date.getDateTime());
			text = text.replace(matcherKey, formattedDate);
		}
		return text;
	}
	
}
