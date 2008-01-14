package dk.teachus.frontend.components.list;

import java.util.Date;

import org.joda.time.DateMidnight;

/**
 * A filter that compares the date parts of two dates.
 */
public class DateFilter implements IFilter<Date> {
	private static final long serialVersionUID = 1L;

	public boolean include(Date objectProperty, Date stateProperty) {
		boolean include = false;
				
		if (objectProperty != null && stateProperty != null) {
			DateMidnight dm1 = new DateMidnight(objectProperty);
			DateMidnight dm2 = new DateMidnight(stateProperty);
			
			include = dm1.equals(dm2);
		} else if (stateProperty == null) {
			include = true;
		}
		
		return include;
	}	
}
