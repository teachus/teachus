package dk.teachus.backend.domain;

import java.util.TimeZone;

import org.joda.time.DateTimeZone;

import dk.teachus.backend.domain.TeacherAttribute.ValueChangeListener;
import dk.teachus.backend.domain.impl.CalendarNarrowTimesTeacherAttribute;
import dk.teachus.backend.domain.impl.TeacherImpl;
import dk.teachus.backend.domain.impl.TimeZoneAttribute;
import dk.teachus.backend.domain.impl.WelcomeIntroductionTeacherAttribute;
import junit.framework.TestCase;

public class TestTeacherAttributes extends TestCase {
	
	public void testCalendarNarrowTimesTeacherAttribute() {
		CalendarNarrowTimesTeacherAttribute a = new CalendarNarrowTimesTeacherAttribute();
		a.setId(1L);
		a.setVersion(2);
		TeacherImpl teacher = new TeacherImpl();
		a.setTeacher(teacher);
		a.setBooleanValue(true);
		
		assertEquals(new Long(1), a.getId());
		assertEquals(2, a.getVersion());
		assertSame(teacher, a.getTeacher());
		assertTrue(a.getBooleanValue());
	}
	
	public void testTimeZoneAttribute() {
		TimeZoneAttribute a = new TimeZoneAttribute();
		a.setId(1L);
		a.setVersion(2);
		TeacherImpl teacher = new TeacherImpl();
		a.setTeacher(teacher);
		
		assertNull(a.getTimeZone());
		
		a.setTimeZone(TimeZone.getDefault());
		
		assertEquals(new Long(1), a.getId());
		assertEquals(2, a.getVersion());
		assertSame(teacher, a.getTeacher());
		assertEquals(TimeZone.getDefault(), a.getTimeZone());
	}
	
	public void testListeners() {
		WelcomeIntroductionTeacherAttribute a = new WelcomeIntroductionTeacherAttribute();
		MockListener listener = new MockListener();

		a.setId(1L);
		a.setVersion(2);
		TeacherImpl teacher = new TeacherImpl();
		a.setTeacher(teacher);
		
		a.addValueChangeListener(listener);
		
		assertNull(listener.getOldValue());
		assertNull(listener.getNewValue());
		
		a.setValue("something");
		
		assertNull(listener.getOldValue());
		assertEquals("something", listener.getNewValue());
		
		a.setValue("another");
		
		assertEquals("something", listener.getOldValue());
		assertEquals("another", listener.getNewValue());
		
		a.setValue(null);
		
		assertEquals("another", listener.getOldValue());
		assertNull(listener.getNewValue());
		
		a.removeValueChangeListener(listener);
		
		a.setValue("new");
		
		assertEquals("another", listener.getOldValue());
		assertNull(listener.getNewValue());
	}
	
	private static class MockListener implements ValueChangeListener {
		private static final long serialVersionUID = 1L;

		private String oldValue;
		private String newValue;
		
		public String getOldValue() {
			return oldValue;
		}
		
		public String getNewValue() {
			return newValue;
		}
		
		public void onValueChanged(TeacherAttribute teacherAttribute, String oldValue, String newValue) {
			this.oldValue = oldValue;
			this.newValue = newValue;
		}
		
	}
	
}
