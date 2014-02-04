package dk.teachus.backend.dao.jdo;

import java.util.List;
import java.util.Map;

import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.jdo.Query;

import dk.teachus.backend.dao.PeriodDAO;
import dk.teachus.backend.domain.Period;
import dk.teachus.backend.domain.PeriodStatus;
import dk.teachus.backend.domain.Periods;
import dk.teachus.backend.domain.Teacher;
import dk.teachus.backend.domain.impl.PeriodImpl;
import dk.teachus.backend.domain.impl.PeriodsImpl;

public class JdoPeriodDAO implements PeriodDAO {
	private static final long serialVersionUID = 1L;
	
	private PersistenceManagerFactory persistenceManagerFactory;
	
	@Override
	public Period createPeriodObject() {
		return new PeriodImpl();
	}
	
	@Override
	public void save(final Period p) {
		final PersistenceManager pm = persistenceManagerFactory.getPersistenceManager();
		pm.makePersistent(p);
	}
	
	@Override
	public Period get(final Long id) {
		final PersistenceManager pm = persistenceManagerFactory.getPersistenceManager();
		return pm.getObjectById(PeriodImpl.class, id);
	}
	
	@Override
	public Periods getPeriods(final Teacher teacher) {
		return getPeriods(teacher, true);
	}
	
	@Override
	public Periods getPeriods(final Teacher teacher, final boolean onlyFinal) {
		final PersistenceManager pm = persistenceManagerFactory.getPersistenceManager();
		final Query query = pm.newQuery(PeriodImpl.class);
		query.setFilter("teacher == t && status == s");
		query.declareImports("import " + Teacher.class.getName() + "; import " + PeriodStatus.class.getName() + ";");
		query.declareParameters(Teacher.class.getSimpleName() + " t, " + PeriodStatus.class.getSimpleName() + " s");
		@SuppressWarnings("unchecked")
		final List<Period> result = (List<Period>) query.executeWithArray(teacher, onlyFinal ? PeriodStatus.FINAL : PeriodStatus.DELETED);
		return new PeriodsImpl(result);
	}
	
	@Override
	public void delete(final Period period) {
		// TODO
	}
	
	@Override
	public Map<Long, Boolean> getPeriodDeleteability() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public void setPersistenceManagerFactory(final PersistenceManagerFactory persistenceManagerFactory) {
		this.persistenceManagerFactory = persistenceManagerFactory;
	}
	
}
