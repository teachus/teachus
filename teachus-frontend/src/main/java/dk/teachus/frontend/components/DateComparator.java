package dk.teachus.frontend.components;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Date;

public class DateComparator implements Comparator<Date>, Serializable {
	private static final long serialVersionUID = 1L;

	public int compare(Date o1, Date o2) {
		int compare = 0;
		
		if (o1 != null && o2 != null) {
			compare = o1.compareTo(o2);
		} else if (o1 != null) {
			compare = -1;
		} else if (o2 != null) {
			compare = 1;
		}
		
		return compare;
	}
	
}
