package dk.teachus.frontend.components.list;

import java.io.Serializable;
import java.util.Comparator;

/**
 * Case insensitive string comparator
 */
public class StringComparator implements Comparator<String>, Serializable {
	private static final long serialVersionUID = 1L;

	public int compare(String o1, String o2) {
		int compare = 0;
		
		if (o1 != null && o2 != null) {
			compare = o1.toLowerCase().compareTo(o2.toLowerCase());
		} else if (o1 != null) {
			compare = -1;
		} else if (o2 != null) {
			compare = 1;
		}
		
		return compare;
	} 
}