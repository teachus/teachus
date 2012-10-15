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

import org.springframework.dao.DataAccessException;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import dk.teachus.backend.dao.ApplicationDAO;
import dk.teachus.backend.domain.ApplicationConfiguration;
import dk.teachus.backend.domain.impl.ApplicationConfigurationEntry;
import dk.teachus.backend.domain.impl.ApplicationConfigurationImpl;

@Transactional(propagation=Propagation.MANDATORY)
public class HibernateApplicationDAO extends HibernateDaoSupport implements ApplicationDAO {

	@SuppressWarnings("unchecked")
	@Transactional(readOnly=true)
	public ApplicationConfiguration loadConfiguration() {
		List<ApplicationConfigurationEntry> list = getHibernateTemplate().loadAll(ApplicationConfigurationEntry.class);
				
		return new ApplicationConfigurationImpl(list);
	}

	@Transactional(rollbackFor=DataAccessException.class)
	public void saveConfiguration(ApplicationConfiguration configuration) {
		if (configuration instanceof ApplicationConfigurationImpl == false) {
			throw new IllegalArgumentException("Unsupported "+ApplicationConfiguration.class.getName()+" implementation: "+configuration);
		}
		
		ApplicationConfigurationImpl configurationImpl = (ApplicationConfigurationImpl) configuration;
		
		List<ApplicationConfigurationEntry> entries = configurationImpl.getEntries();
		
		if (entries != null) {
			for (ApplicationConfigurationEntry entry : entries) {
				getHibernateTemplate().saveOrUpdate(entry);
			}
		}
	}

}
