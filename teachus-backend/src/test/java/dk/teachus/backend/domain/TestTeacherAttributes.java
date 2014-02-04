package dk.teachus.backend.domain;

import junit.framework.Assert;
import junit.framework.TestCase;
import dk.teachus.backend.domain.TeacherAttribute.TeacherAttributeProperty;
import dk.teachus.backend.domain.TeacherAttribute.ValueChangeListener;
import dk.teachus.backend.domain.impl.TeacherAttributeImpl;
import dk.teachus.backend.domain.impl.TeacherImpl;

public class TestTeacherAttributes extends TestCase {
	
	public void testListeners() {
		final TeacherAttributeImpl a = new TeacherAttributeImpl();
		final MockListener listener = new MockListener();
		
		a.setId(1L);
		final TeacherImpl teacher = new TeacherImpl();
		a.setTeacher(teacher);
		
		a.addValueChangeListener(listener);
		
		Assert.assertNull(listener.getOldValue());
		Assert.assertNull(listener.getNewValue());
		
		a.setWelcomeIntroduction("something");
		
		Assert.assertNull(listener.getOldValue());
		Assert.assertEquals("something", listener.getNewValue());
		
		a.setWelcomeIntroduction("another");
		
		Assert.assertEquals("something", listener.getOldValue());
		Assert.assertEquals("another", listener.getNewValue());
		
		a.setWelcomeIntroduction(null);
		
		Assert.assertEquals("another", listener.getOldValue());
		Assert.assertNull(listener.getNewValue());
		
		a.removeValueChangeListener(listener);
		
		a.setWelcomeIntroduction("new");
		
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
		public void onValueChanged(final TeacherAttributeProperty property, final String oldValue, final String newValue) {
			this.oldValue = oldValue;
			this.newValue = newValue;
		}
		
	}
	
}
