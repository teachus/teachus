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

import java.util.Date;
import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import dk.teachus.backend.dao.MessageDAO;
import dk.teachus.backend.domain.Message;
import dk.teachus.backend.domain.MessageState;
import dk.teachus.backend.domain.Person;
import dk.teachus.backend.domain.impl.AbstractMessage;

@Transactional(propagation=Propagation.REQUIRED)
public class HibernateMessageDAO extends HibernateDaoSupport implements MessageDAO {

	@SuppressWarnings("unchecked")
	@Transactional(readOnly=true)
	public List<Message> getMessages(Person sender) {
		DetachedCriteria c = DetachedCriteria.forClass(AbstractMessage.class);
		c.add(Restrictions.eq("sender", sender));
		
		return getHibernateTemplate().findByCriteria(c);
	}

	@SuppressWarnings("unchecked")
	@Transactional(readOnly=true)
	public List<Message> getUnsentMessages() {
		DetachedCriteria c = DetachedCriteria.forClass(AbstractMessage.class);
		c.add(Restrictions.eq("state", MessageState.FINAL));
		
		return getHibernateTemplate().findByCriteria(c);
	}

	public void save(Message message) {
		if (message.getId() == null) {
			message.setCreateDate(new Date());
		}
		
		getHibernateTemplate().saveOrUpdate(message);
		getHibernateTemplate().flush();
	}
	
}
