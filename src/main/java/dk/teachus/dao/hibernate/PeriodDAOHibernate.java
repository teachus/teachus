package dk.teachus.dao.hibernate;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.DistinctRootEntityResultTransformer;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import dk.teachus.dao.PeriodDAO;
import dk.teachus.domain.Period;
import dk.teachus.domain.Periods;
import dk.teachus.domain.Teacher;
import dk.teachus.domain.impl.PeriodImpl;
import dk.teachus.domain.impl.PeriodsImpl;

@Transactional(propagation=Propagation.REQUIRED)
public class PeriodDAOHibernate extends HibernateDaoSupport implements PeriodDAO {
	private static final long serialVersionUID = 1L;

	public void save(Period p) {
		getHibernateTemplate().saveOrUpdate(p);
		getHibernateTemplate().flush();
	}

	@Transactional(readOnly=true)
	public Period get(Long id) {
		return (Period) getHibernateTemplate().load(PeriodImpl.class, id);
	}

	@SuppressWarnings("unchecked")
	@Transactional(readOnly=true)
	public Periods getPeriods(Teacher teacher) {
		DetachedCriteria c = DetachedCriteria.forClass(PeriodImpl.class);
		
		c.add(Restrictions.eq("teacher", teacher));
		
		c.setResultTransformer(new DistinctRootEntityResultTransformer());
		
		List<Period> result = getHibernateTemplate().findByCriteria(c);
		
		Periods periods = new PeriodsImpl();
		periods.setPeriods(result);
		return periods;
	}

}
