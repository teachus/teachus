package dk.teachus.backend.dao.jpa;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import dk.teachus.backend.dao.PeriodDAO;
import dk.teachus.backend.dao.PersonDAO;
import dk.teachus.backend.domain.Period;
import dk.teachus.backend.domain.PeriodStatus;
import dk.teachus.backend.domain.Periods;
import dk.teachus.backend.domain.Teacher;
import dk.teachus.backend.domain.WeekDay;
import dk.teachus.backend.rdms.test.AbstractSpringTests;

public class TestJdoPeriodDAO extends AbstractSpringTests {

	@Autowired
	private PeriodDAO periodDao;
	
	@Autowired
	private PersonDAO personDao;

	@Test
	public void save() {
		Period p = periodDao.createPeriodObject();
		p.addWeekDay(WeekDay.WEDNESDAY);
		p.addWeekDay(WeekDay.FRIDAY);
		p.setName("P1");
		
		periodDao.save(p);
		
		Period persistedPeriod = periodDao.get(p.getId());
		assertEquals(p.getName(), persistedPeriod.getName());
		assertEquals(p.getWeekDays().size(), persistedPeriod.getWeekDays().size());
		assertTrue(persistedPeriod.getWeekDays().contains(WeekDay.WEDNESDAY));
		assertTrue(persistedPeriod.getWeekDays().contains(WeekDay.FRIDAY));
	}
	
	@Test
	public void getPeriods() {
		Teacher teacher = personDao.createTeacherObject();
		teacher.setName("T1");
		teacher.setUsername("t1");
		teacher.setActive(true);
		personDao.save(teacher);
		
		Period p1 = periodDao.createPeriodObject();
		p1.addWeekDay(WeekDay.WEDNESDAY);
		p1.addWeekDay(WeekDay.FRIDAY);
		p1.setName("P1");
		p1.setTeacher(teacher);
		p1.setStatus(PeriodStatus.FINAL);
		periodDao.save(p1);
		
		Period p2 = periodDao.createPeriodObject();
		p2.addWeekDay(WeekDay.MONDAY);
		p2.addWeekDay(WeekDay.WEDNESDAY);
		p2.setName("P2");
		p2.setTeacher(teacher);
		p2.setStatus(PeriodStatus.FINAL);
		periodDao.save(p2);
		
		Periods periods = periodDao.getPeriods(teacher);
		assertNotNull(periods);
		assertEquals(2, periods.getPeriods().size());
	}
	
}
