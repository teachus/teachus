package dk.teachus.domain.impl;

import dk.teachus.domain.Currency;
import dk.teachus.domain.Teacher;

public class CurrencyImpl extends AbstractHibernateObject implements Currency {
	private static final long serialVersionUID = 1L;

	private Teacher teacher;
	
	private String label;

	private boolean base;

	private double exchangeRate;

	public double getExchangeRate() {
		return exchangeRate;
	}

	public String getLabel() {
		return label;
	}

	public Teacher getTeacher() {
		return teacher;
	}

	public boolean isBase() {
		return base;
	}

	public void setBase(boolean base) {
		this.base = base;
	}

	public void setExchangeRate(double exchangeRate) {
		this.exchangeRate = exchangeRate;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public void setTeacher(Teacher teacher) {
		this.teacher = teacher;
	}

}
