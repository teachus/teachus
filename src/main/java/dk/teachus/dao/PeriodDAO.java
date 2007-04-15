package dk.teachus.dao;

import java.io.Serializable;

import dk.teachus.domain.Period;
import dk.teachus.domain.Periods;
import dk.teachus.domain.Teacher;

public interface PeriodDAO extends Serializable {

	void save(Period p);

	Period get(Long id);

	Periods getPeriods(Teacher teacher);

	void delete(Period period);
	
	Period createPeriodObject();

}
