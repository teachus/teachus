package dk.teachus.backend.domain.impl;

public class AbstractBooleanTeacherAttribute extends AbstractTeacherAttribute {
	private static final long serialVersionUID = 1L;
	
	public boolean getBooleanValue() {
		return Boolean.parseBoolean(getValue());
	}
	
	public void setBooleanValue(boolean value) {
		setValue(Boolean.toString(value));
	}
	
}
