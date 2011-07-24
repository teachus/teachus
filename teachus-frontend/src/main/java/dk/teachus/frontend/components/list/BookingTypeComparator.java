package dk.teachus.frontend.components.list;

import java.io.Serializable;
import java.util.Comparator;

import dk.teachus.backend.domain.Booking;
import dk.teachus.frontend.utils.BookingTypeRenderer;

public class BookingTypeComparator implements Comparator<Class<? extends Booking>>, Serializable {
	private static final long serialVersionUID = 1L;

	public int compare(Class<? extends Booking> o1, Class<? extends Booking> o2) {
		int compare = 0;
		
		if (o1 != null && o2 != null) {
			BookingTypeRenderer bookingTypeRenderer = new BookingTypeRenderer();
			String o1String = (String) bookingTypeRenderer.getDisplayValue(o1);
			String o2String = (String) bookingTypeRenderer.getDisplayValue(o2);
			
			compare = o1String.compareTo(o2String);
		} else if (o1 != null) {
			compare = -1;
		} else if (o2 != null) {
			compare = 1;
		}
		
		return compare;
	}
	
}
