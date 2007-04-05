package dk.teachus.dao;

import java.io.Serializable;
import java.util.List;

import dk.teachus.domain.Currency;
import dk.teachus.domain.Teacher;

public interface CurrencyDAO extends Serializable {

	void save(Currency currency);
	
	Currency get(Long id);
	
	List<Currency> getAll(Teacher teacher);
	
	void setBase(Currency currency);
	
}
