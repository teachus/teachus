package dk.teachus.frontend.components.list;

import org.joda.time.DateMidnight;
import org.joda.time.DateTime;

/**
 * A filter that compares the date parts of two dates.
 */
public class DateFilter implements IFilter<DateTime> {
	private static final long serialVersionUID = 1L;

	public boolean include(DateTime objectProperty, DateTime stateProperty) {
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
