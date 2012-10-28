package dk.teachus.frontend;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import dk.teachus.backend.domain.Person;
import dk.teachus.backend.domain.Pupil;
import dk.teachus.backend.domain.Teacher;
import dk.teachus.backend.domain.TeacherAttribute;
import dk.teachus.backend.domain.TeacherAttribute.TeacherAttributeProperty;
import dk.teachus.backend.domain.TeacherAttribute.ValueChangeListener;

public class TeacherAttributes implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private final Map<Long, TeacherAttribute> teacherAttributes = new HashMap<Long, TeacherAttribute>();
	
	public TeacherAttribute getTeacherAttribute() {
		return getTeacherAttribute(TeachUsSession.get().getPerson());
	}
	
	public TeacherAttribute getTeacherAttribute(final Person person) {
		if (person == null) {
			throw new IllegalArgumentException("Person must not be null");
		}
		
		if (person instanceof Teacher) {
			final Teacher teacher = (Teacher) person;
			return getTeacherAttributes(teacher);
		} else if (person instanceof Pupil) {
			final Pupil pupil = (Pupil) person;
			return getTeacherAttributes(pupil.getTeacher());
		}
		
		throw new IllegalArgumentException("Unsupported person type: " + person);
	}
	
	public TeacherAttribute getTeacherAttributes(final Teacher teacher) {
		if (teacher == null) {
			throw new IllegalArgumentException("Teacher must not be null.");
		}
		
		if (teacher.getId() == null) {
			return null;
		}
		
		TeacherAttribute attribute = teacherAttributes.get(teacher.getId());
		if (attribute == null) {
			attribute = TeachUsApplication.get().getPersonDAO().getAttribute(teacher);
			if (attribute != null) {
				addValueChangeListener(attribute);
			}
			teacherAttributes.put(teacher.getId(), attribute);
		}
		
		return attribute;
	}
	
	public void refreshAttributes(final Teacher teacher) {
		if (teacher == null) {
			throw new IllegalArgumentException("Teacher must not be null.");
		}
		
		if (teacher.getId() == null) {
			throw new IllegalArgumentException("Teacher must have been persisted. (Id must not be null).");
		}
		
		teacherAttributes.remove(teacher.getId());
	}
	
	private void addValueChangeListener(final TeacherAttribute teacherAttribute) {
		teacherAttribute.addValueChangeListener(new ValueChangeListener() {
			private static final long serialVersionUID = 1L;
			
			@Override
			public void onValueChanged(final TeacherAttributeProperty property, final String oldValue, final String newValue) {
				TeachUsApplication.get().getPersonDAO().saveAttribute(teacherAttribute);
			}
		});
	}
	
}
