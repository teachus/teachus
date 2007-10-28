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
package dk.teachus.backend.dao;

import java.io.Serializable;
import java.util.Map;

import dk.teachus.backend.domain.Period;
import dk.teachus.backend.domain.Periods;
import dk.teachus.backend.domain.Teacher;

public interface PeriodDAO extends Serializable {
	
	void save(Period p);
	
	Period get(Long id);
	
	Periods getPeriods(Teacher teacher);
	
	Periods getPeriods(Teacher teacher, boolean onlyFinal);
	
	/**
	 * It's only possible to delete periods which doesn't have any active periods.
	 * 
	 * @param period
	 *            The period to delete.
	 */
	void delete(Period period);
	
	Period createPeriodObject();
	
	/**
	 * Get a list of periods and their ability to be deleted. This is currently based on if they are not yet deleted and
	 * if they have in active bookings.
	 * 
	 * @return A map with the period id as key and a boolean as value to indicate if the period can be deleted or not.
	 *         True means that it can be deleted.
	 */
	Map<Long, Boolean> getPeriodDeleteability();
	
}
