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

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import dk.teachus.backend.dao.StatisticsDAO;
import dk.teachus.backend.domain.TeacherStatistics;
import dk.teachus.backend.domain.Period.Status;

@Transactional(propagation=Propagation.REQUIRED)
public class HibernateStatisticsDAO extends HibernateDaoSupport implements StatisticsDAO {

	@SuppressWarnings("unchecked")
	@Transactional(readOnly=true)
	public List<TeacherStatistics> getTeachers() {
		StringBuilder hql = new StringBuilder();
		
		hql.append("SELECT ");
		hql.append("new dk.teachus.backend.domain.impl.TeacherStatisticsImpl(t, ");
		hql.append("(SELECT count(p) FROM PupilImpl p WHERE p.teacher = t AND p.active = 1), ");
		hql.append("(SELECT count(p) FROM PeriodImpl p WHERE p.teacher = t AND p.status = ?), ");
		hql.append("(SELECT count(b) FROM PupilBookingImpl b WHERE b.pupil.teacher = t AND b.active = 1), ");
		hql.append("(SELECT count(b) FROM TeacherBookingImpl b WHERE b.teacher = t AND b.active = 1) ");
		hql.append(") ");
		hql.append("FROM ");
		hql.append("TeacherImpl AS t ");
		hql.append("WHERE ");
		hql.append("t.active = 1 ");
		hql.append("ORDER BY ");
		hql.append("t.id ");
				
		return getHibernateTemplate().find(hql.toString(), Status.FINAL);
	}
	
}
