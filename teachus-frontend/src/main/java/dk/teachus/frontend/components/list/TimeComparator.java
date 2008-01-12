package dk.teachus.frontend.components.list;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Date;

import org.joda.time.DateTime;

public class TimeComparator implements Comparator<Date>, Serializable {
	private static final long serialVersionUID = 1L;

	public int compare(Date o1, Date o2) {
		int compare = 0;
		
		if (o1 != null && o2 != null) {
			DateTime d1 = new DateTime(o1).withDate(1, 1, 1);
			DateTime d2 = new DateTime(o2).withDate(1, 1, 1);
			
			compare = d1.compareTo(d2);
		} else if (o1 != null) {
			compare = -1;
		} else if (o2 != null) {
			compare = 1;
		}
		
		return compare;
	}
	
}
