package dk.frankbille.teachus.dao;

import java.io.Serializable;

import dk.frankbille.teachus.domain.Period;
import dk.frankbille.teachus.domain.Periods;
import dk.frankbille.teachus.domain.Teacher;

public interface PeriodDAO extends Serializable {

	void save(Period p);

	Period get(Long id);

	Periods getPeriods(Teacher teacher);

}
