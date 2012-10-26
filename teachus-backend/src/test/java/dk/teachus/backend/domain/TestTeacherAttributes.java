package dk.teachus.backend.domain;

import java.util.TimeZone;

import junit.framework.Assert;
import junit.framework.TestCase;
import dk.teachus.backend.domain.TeacherAttribute.ValueChangeListener;
import dk.teachus.backend.domain.impl.CalendarNarrowTimesTeacherAttribute;
import dk.teachus.backend.domain.impl.TeacherImpl;
import dk.teachus.backend.domain.impl.TimeZoneAttribute;
import dk.teachus.backend.domain.impl.WelcomeIntroductionTeacherAttribute;

public class TestTeacherAttributes extends TestCase {
	
	public void testCalendarNarrowTimesTeacherAttribute() {
		final CalendarNarrowTimesTeacherAttribute a = new CalendarNarrowTimesTeacherAttribute();
		a.setId(1L);
		final TeacherImpl teacher = new TeacherImpl();
		a.setTeacher(teacher);
		a.setBooleanValue(true);
		
		Assert.assertEquals(new Long(1), a.getId());
		Assert.assertSame(teacher, a.getTeacher());
		Assert.assertTrue(a.getBooleanValue());
	}
	
	public void testTimeZoneAttribute() {
		final TimeZoneAttribute a = new TimeZoneAttribute();
		a.setId(1L);
		final TeacherImpl teacher = new TeacherImpl();
		a.setTeacher(teacher);
		
		Assert.assertNull(a.getTimeZone());
		
		a.setTimeZone(TimeZone.getDefault());
		
		Assert.assertEquals(new Long(1), a.getId());
		Assert.assertSame(teacher, a.getTeacher());
		Assert.assertEquals(TimeZone.getDefault(), a.getTimeZone());
	}
	
	public void testListeners() {
		final WelcomeIntroductionTeacherAttribute a = new WelcomeIntroductionTeacherAttribute();
		final MockListener listener = new MockListener();
		
		a.setId(1L);
		final TeacherImpl teacher = new TeacherImpl();
		a.setTeacher(teacher);
		
		a.addValueChangeListener(listener);
		
		Assert.assertNull(listener.getOldValue());
		Assert.assertNull(listener.getNewValue());
		
		a.setValue("something");
		
		Assert.assertNull(listener.getOldValue());
		Assert.assertEquals("something", listener.getNewValue());
		
		a.setValue("another");
		
		Assert.assertEquals("something", listener.getOldValue());
		Assert.assertEquals("another", listener.getNewValue());
		
		a.setValue(null);
		
		Assert.assertEquals("another", listener.getOldValue());
		Assert.assertNull(listener.getNewValue());
		
		a.removeValueChangeListener(listener);
		
		a.setValue("new");
		
		Assert.assertEquals("another", listener.getOldValue());
		Assert.assertNull(listener.getNewValue());
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
		
		@Override
		public void onValueChanged(final TeacherAttribute teacherAttribute, final String oldValue, final String newValue) {
			this.oldValue = oldValue;
			this.newValue = newValue;
		}
		
	}
	
}
