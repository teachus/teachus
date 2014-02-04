package dk.teachus.backend.dao.jdo;

import javax.jdo.JDOObjectNotFoundException;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;

abstract class AbstractJdoDAO {
	
	protected PersistenceManagerFactory persistenceManagerFactory;
	
	public void setPersistenceManagerFactory(final PersistenceManagerFactory persistenceManagerFactory) {
		this.persistenceManagerFactory = persistenceManagerFactory;
	}
	
	/**
	 * Will return the found object or null if not found
	 */
	protected <T> T findObjectById(final Class<T> type, final Object id) {
		final PersistenceManager pm = persistenceManagerFactory.getPersistenceManager();
		T foundObject = null;
		try {
			foundObject = pm.getObjectById(type, id);
		} catch (final JDOObjectNotFoundException e) {
			// Do nothing
		}
		return foundObject;
	}
	
}
