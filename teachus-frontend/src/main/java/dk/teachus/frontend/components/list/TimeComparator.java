package dk.teachus.frontend.components.list;

import java.io.Serializable;
import java.util.Comparator;

import org.joda.time.LocalTime;

public class TimeComparator implements Comparator<LocalTime>, Serializable {
	private static final long serialVersionUID = 1L;

	public int compare(LocalTime o1, LocalTime o2) {
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
