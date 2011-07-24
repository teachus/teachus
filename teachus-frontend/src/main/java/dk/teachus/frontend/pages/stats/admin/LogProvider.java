/**
 * 
 */
package dk.teachus.frontend.pages.stats.admin;

import java.io.Serializable;
import java.util.List;

import org.joda.time.DateMidnight;


public interface LogProvider extends Serializable {
	void appendEntries(List<Entry> entries, DateMidnight fromDate, DateMidnight toDate);
}