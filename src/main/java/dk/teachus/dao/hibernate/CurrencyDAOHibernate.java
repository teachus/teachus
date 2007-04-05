package dk.teachus.dao.hibernate;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import dk.teachus.dao.CurrencyDAO;
import dk.teachus.domain.Currency;
import dk.teachus.domain.Teacher;
import dk.teachus.domain.impl.CurrencyImpl;

@Transactional(propagation=Propagation.REQUIRED)
public class CurrencyDAOHibernate extends HibernateDaoSupport implements CurrencyDAO {
	private static final long serialVersionUID = 1L;

	@Transactional(readOnly=true)
	public Currency get(Long id) {
		return (Currency) getHibernateTemplate().load(CurrencyImpl.class, id);
	}

	@SuppressWarnings("unchecked")
	@Transactional(readOnly=true)
	public List<Currency> getAll(Teacher teacher) {
		DetachedCriteria c = DetachedCriteria.forClass(CurrencyImpl.class);
		
		c.add(Restrictions.eq("teacher", teacher));
		
		return getHibernateTemplate().findByCriteria(c);
	}

	public void save(Currency currency) {
		if (currency.getTeacher() == null) {
			throw new IllegalArgumentException("The currency must have a teacher associated");
		}
		
		if (currency.getExchangeRate() <= 0) {
			throw new IllegalArgumentException("The currency must have a positive exchange rate");
		}
		
		currency.setBase(false);

		// Round to 3 digits
		double exchangeRate = currency.getExchangeRate();
		exchangeRate *= 1000.0;
		exchangeRate = Math.round(exchangeRate);
		exchangeRate /= 1000.0;
		currency.setExchangeRate(exchangeRate);
		
		getHibernateTemplate().save(currency);
		getHibernateTemplate().flush();
	}

	public void setBase(Currency newBaseCurrency) {
		if (newBaseCurrency.getId() == null) {
			throw new IllegalArgumentException("Can only set a already saved currency as base");
		}

		/*
		 * Recalculate the existing currencies.
		 */	
		double rateFactor = 1.0 / newBaseCurrency.getExchangeRate();
		
		List<Currency> currencies = getAll(newBaseCurrency.getTeacher());
		
		for (Currency currency : currencies) {
			if (currency.getId().equals(newBaseCurrency.getId()) == false) {
				double newExchangeRate = rateFactor * currency.getExchangeRate();
				currency.setExchangeRate(newExchangeRate);
				currency.setBase(false);
				save(currency);
			}			
		}
		
		/*
		 * Set the newBaseCurrency to base and exchange rate
		 */
		newBaseCurrency.setBase(true);
		newBaseCurrency.setExchangeRate(1.0);
	}

}
