package dk.teachus.frontend.components.list;

import java.io.Serializable;
import java.util.Comparator;

import dk.teachus.backend.domain.MessageState;

public class MessageStateComparator implements Comparator<MessageState>, Serializable {
	private static final long serialVersionUID = 1L;
	
	public int compare(MessageState m1, MessageState m2) {
		int compare = 0;
		
		if (m1 != null && m2 != null) {
			Integer prio1 = getPriority(m1);
			Integer prio2 = getPriority(m2);
			
			compare = prio1.compareTo(prio2);
		} else if (m1 != null) {
			compare = -1;
		} else if (m2 != null) {
			compare = 1;
		}
		
		return compare;
	}
	
	private Integer getPriority(MessageState state) {
		int prio = Integer.MAX_VALUE;
		
		switch (state) {
			case DRAFT:
				prio = 1;
				break;
			case FINAL:
				prio = 2;
				break;
			case SENT:
				prio = 3;
				break;
			case ERROR_SENDING_INVALID_RECIPIENT:
				prio = 4;
				break;
			case ERROR_SENDING_UNKNOWN:
				prio = 5;
				break;
			default:
				throw new IllegalArgumentException("Unknown message state: "+state);
		}
		
		return prio;
	}
	
}
