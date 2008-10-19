/**
 * 
 */
package dk.teachus.frontend.pages.stats.admin;

import java.io.Serializable;
import java.util.List;

import dk.teachus.backend.domain.TeachUsDate;


public interface LogProvider extends Serializable {
	void appendEntries(List<Entry> entries, TeachUsDate fromDate, TeachUsDate toDate);
}