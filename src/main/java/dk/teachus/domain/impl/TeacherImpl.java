package dk.teachus.domain.impl;

import dk.teachus.domain.Teacher;

public class TeacherImpl extends PersonImpl implements Teacher {
	private static final long serialVersionUID = 1L;
	
	private String currency;

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}
	
}
