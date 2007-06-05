package dk.teachus.backend.dao.hibernate;

import java.util.List;

import org.joda.time.DateTime;

import dk.teachus.backend.domain.TeacherStatistics;
import dk.teachus.backend.test.SpringTestCase;

public class TestStatisticsDAO extends SpringTestCase {
	private static final long serialVersionUID = 1L;

	public void testGetTeachers() {
		createTeacher();
		
		createTeacherBooking(2L, 2L, new DateTime(2007, 5, 15, 14, 0, 0, 0));
		
		List<TeacherStatistics> teachers = getStatisticsDAO().getTeachers();
		endTransaction();
		assertNotNull(teachers);
		
		assertEquals(2, teachers.size());
		
		TeacherStatistics teacherStatistics = teachers.get(0);
		
		assertEquals(3, teacherStatistics.getPeriodCount());
		assertEquals(22, teacherStatistics.getPupilCount());
		assertEquals(130, teacherStatistics.getPupilBookingCount());
		assertEquals(1, teacherStatistics.getTeacherBookingCount());
	}

}
