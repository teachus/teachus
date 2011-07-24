package dk.teachus.frontend.pages.periods;

import org.apache.wicket.extensions.markup.html.repeater.data.grid.DataGridView;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.util.tester.WicketTester;
import org.joda.time.DateMidnight;
import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.LocalTime;

import dk.teachus.backend.domain.Period;
import dk.teachus.backend.domain.Period.Status;
import dk.teachus.backend.domain.impl.PeriodImpl;
import dk.teachus.backend.domain.impl.PeriodImpl.WeekDay;
import dk.teachus.frontend.test.WicketSpringTestCase;

public class TestPeriodsPage extends WicketSpringTestCase {
	private static final long serialVersionUID = 1L;

	public void testDeleteablePeriods() {
		// Create period with no bookings on it at all
		Period period = new PeriodImpl();
		period.setName("Empty period");
		period.setStartTime(new LocalTime(10, 0, 0, 0));
		period.setEndTime(new LocalTime(18, 0, 0, 0));
		period.setEndDate(new DateMidnight(2005, 12, 15));
		period.setLocation("Private");
		period.setPrice(300);
		period.setStatus(Status.FINAL);
		period.setTeacher(getTeacher());
		period.addWeekDay(WeekDay.MONDAY);
		getPeriodDAO().save(period);
		endTransaction();
		
		// Create period which has a deleted booking
		period = new PeriodImpl();
		period.setName("Empty period with deleted booking");
		period.setStartTime(new LocalTime(10, 0, 0, 0));
		period.setEndTime(new LocalTime(18, 0, 0, 0));
		period.setEndDate(new DateMidnight(2005, 12, 15));
		period.setLocation("Private");
		period.setPrice(300);
		period.setStatus(Status.FINAL);
		period.setTeacher(getTeacher());
		period.addWeekDay(WeekDay.TUESDAY);
		getPeriodDAO().save(period);
		endTransaction();
		
		Long booking = createPupilBooking(period.getId(), 3, new DateTime(2005, 10, 10, 11, 0, 0, 0).withDayOfWeek(DateTimeConstants.TUESDAY), new DateTime());
		getBookingDAO().deleteBooking(getBookingDAO().getBooking(booking));
		endTransaction();
		
		// Start the page
		WicketTester tester = createTester();
		
		tester.startPage(PeriodsPage.class);
		
		tester.assertRenderedPage(PeriodsPage.class);
		
		assertNotDeleteable(tester, "Man/Ons/Fre");
		assertNotDeleteable(tester, "Tir/Tor");
		assertNotDeleteable(tester, "Weekend");
		assertDeleteable(tester, "Empty period");
		assertDeleteable(tester, "Empty period with deleted booking");
	}
	
	private void assertDeleteable(WicketTester tester, String periodName) {
		Link link = getDeleteLink(tester, periodName);
		assertTrue("Period '"+periodName+"' is not deleteable", link.isEnabled());
	}
	
	private void assertNotDeleteable(WicketTester tester, String periodName) {
		Link link = getDeleteLink(tester, periodName);
		assertFalse("Period '"+periodName+"' is deleteable", link.isEnabled());
	}

	private Link getDeleteLink(WicketTester tester, String periodName) {
		Item periodRow = getPeriodRow(tester, periodName);		
		Link link = (Link) tester.getComponentFromLastRenderedPage(periodRow.getPageRelativePath()+":cells:8:cell:items:0:item:link");
		return link;
	}

	private Item getPeriodRow(WicketTester tester, String periodName) {
		Item periodRow = null;
		DataGridView gridView = (DataGridView) tester.getComponentFromLastRenderedPage("list:filterForm:table:body:rows");
		for (int i = 1; i <= gridView.size(); i++) {
			Label label = (Label) tester.getComponentFromLastRenderedPage("list:filterForm:table:body:rows:"+i+":cells:1:cell:link:label");
			if (periodName.equals(label.getDefaultModelObjectAsString())) {
				periodRow = (Item) tester.getComponentFromLastRenderedPage("list:filterForm:table:body:rows:"+i);
				break;
			}
		}
		
		assertNotNull(periodRow);
		
		return periodRow;
	}
	
}
