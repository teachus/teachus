package dk.teachus.backend.domain.impl;

import java.util.TimeZone;


public class TimeZoneAttribute extends AbstractTeacherAttribute {
	private static final long serialVersionUID = 1L;
	
	public TimeZone getTimeZone() {
		return getValue() != null ? TimeZone.getTimeZone(getValue()) : null;
	}
	
	public void setTimeZone(TimeZone timeZone) {
		String timeZoneValue = null;
		
		if (timeZone != null) {
			timeZoneValue = timeZone.getID();
		}
		
		setValue(timeZoneValue);
	}
	
}
