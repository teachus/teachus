package dk.teachus.frontend;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dk.teachus.backend.domain.Person;
import dk.teachus.backend.domain.Pupil;
import dk.teachus.backend.domain.Teacher;
import dk.teachus.backend.domain.TeacherAttribute;
import dk.teachus.backend.domain.TeacherAttribute.ValueChangeListener;

public class TeacherAttributes implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Map<Long,List<TeacherAttribute>> teacherAttributes = new HashMap<Long, List<TeacherAttribute>>();

	public <A extends TeacherAttribute> A getTeacherAttribute(Class<A> attributeClass, Person person) {
		List<TeacherAttribute> attributes = getTeacherAttributes(person);
		
		return getTeacherAttribute(attributes, attributeClass);
	}
	
	public <A extends TeacherAttribute> A getTeacherAttribute(Class<A> attributeClass, Teacher teacher) {
		List<TeacherAttribute> attributes = getTeacherAttributes(teacher);
		
		return getTeacherAttribute(attributes, attributeClass);
	}
	
	public List<TeacherAttribute> getTeacherAttributes() {
		return getTeacherAttributes(TeachUsSession.get().getPerson());
	}
	
	public List<TeacherAttribute> getTeacherAttributes(Person person) {
		if (person == null) {
			throw new IllegalArgumentException("Person must not be null");
		}
		
		if (person instanceof Teacher) {
			Teacher teacher = (Teacher) person;
			return getTeacherAttributes(teacher);
		} else if (person instanceof Pupil) {
			Pupil pupil = (Pupil) person;
			return getTeacherAttributes(pupil.getTeacher());
		}
		
		throw new IllegalArgumentException("Unsupported person type: "+person);
	}
	
	public List<TeacherAttribute> getTeacherAttributes(Teacher teacher) {
		if (teacher == null) {
			throw new IllegalArgumentException("Teacher must not be null.");
		}
		
		if (teacher.getId() == null) {
			throw new IllegalArgumentException("Teacher must have been persisted. (Id must not be null).");
		}
		
		List<TeacherAttribute> attributes = teacherAttributes.get(teacher.getId());
		if (attributes == null) {
			attributes = TeachUsApplication.get().getPersonDAO().getAttributes(teacher);
			if (attributes != null) {
				for (TeacherAttribute teacherAttribute : attributes) {
					addValueChangeListener(teacherAttribute);
				}
			}
			teacherAttributes.put(teacher.getId(), attributes);
		}
		
		return attributes;
	}
	
	public void refreshAttributes(Teacher teacher) {
		if (teacher == null) {
			throw new IllegalArgumentException("Teacher must not be null.");
		}
		
		if (teacher.getId() == null) {
			throw new IllegalArgumentException("Teacher must have been persisted. (Id must not be null).");
		}
		
		teacherAttributes.remove(teacher.getId());
	}

	private void addValueChangeListener(TeacherAttribute teacherAttribute) {
		teacherAttribute.addValueChangeListener(new ValueChangeListener() {
			private static final long serialVersionUID = 1L;
			
			public void onValueChanged(TeacherAttribute teacherAttribute, String oldValue, String newValue) {
				TeachUsApplication.get().getPersonDAO().saveAttribute(teacherAttribute);
			}
		});
	}

	@SuppressWarnings("unchecked")
	private <A extends TeacherAttribute> A getTeacherAttribute(List<TeacherAttribute> attributes, Class<A> attributeClass) {
		A foundAttribute = null;
		
		if (attributes != null) {
			for (TeacherAttribute teacherAttribute : attributes) {
				if (attributeClass.isInstance(teacherAttribute)) {
					foundAttribute = (A) teacherAttribute;
					break;
				}
			}
		}
		
		return foundAttribute;
	}
	
}
