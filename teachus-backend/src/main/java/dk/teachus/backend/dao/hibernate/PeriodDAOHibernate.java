/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package dk.teachus.backend.dao.hibernate;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.DistinctRootEntityResultTransformer;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import dk.teachus.backend.dao.PeriodDAO;
import dk.teachus.backend.domain.Period;
import dk.teachus.backend.domain.Periods;
import dk.teachus.backend.domain.Teacher;
import dk.teachus.backend.domain.Period.Status;
import dk.teachus.backend.domain.impl.PeriodImpl;
import dk.teachus.backend.domain.impl.PeriodsImpl;

@Transactional(propagation=Propagation.REQUIRED)
public class PeriodDAOHibernate extends HibernateDaoSupport implements PeriodDAO {
	private static final long serialVersionUID = 1L;

	public void save(Period p) {
		getHibernateTemplate().saveOrUpdate(p);
		getHibernateTemplate().flush();
	}

	@SuppressWarnings("unchecked")
	@Transactional(readOnly=true)
	public Period get(Long id) {
		DetachedCriteria c = DetachedCriteria.forClass(PeriodImpl.class);
		
		c.add(Restrictions.eq("id", id));
		c.createCriteria("teacher").add(Restrictions.eq("active", true));
		
		Period period = null;
		List<Period> result = getHibernateTemplate().findByCriteria(c);
		if (result.size() == 1) {
			period = result.get(0);
		}
		
		return period;
	}

	@Transactional(readOnly=true)
	public Periods getPeriods(Teacher teacher) {
		return getPeriods(teacher, true);
	}

	@SuppressWarnings("unchecked")
	@Transactional(readOnly=true)
	public Periods getPeriods(Teacher teacher, boolean onlyFinal) {
		DetachedCriteria c = DetachedCriteria.forClass(PeriodImpl.class);
		
		c.add(Restrictions.eq("teacher", teacher));
		c.createCriteria("teacher").add(Restrictions.eq("active", true));
		if (onlyFinal) {
			c.add(Restrictions.eq("status", Status.FINAL));
		} else {
			c.add(Restrictions.ne("status", Status.DELETED));
		}
		
		c.setResultTransformer(new DistinctRootEntityResultTransformer());
		
		List<Period> result = getHibernateTemplate().findByCriteria(c);
		
		Periods periods = new PeriodsImpl();
		periods.setPeriods(result);
		return periods;
	}
	
	public void delete(Period period) {
		period.setStatus(Status.DELETED);
		getHibernateTemplate().update(period);
		getHibernateTemplate().flush();
	}

	@Transactional(readOnly=true)
	public Period createPeriodObject() {
		return new PeriodImpl();
	}

}
