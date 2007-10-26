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
package dk.teachus.ws.service.impl;

import java.util.List;

import dk.teachus.backend.test.SpringTestCase;
import dk.teachus.ws.service.FinanceService;

public class TestFinanceService extends SpringTestCase {
	private static final long serialVersionUID = 1L;

	@Override
	protected void addConfigLocations(List<String> configLocations) {
		configLocations.add("/dk/teachus/ws/applicationContext.xml");
	}
	
	public void testGetIncomeForMonth() {
		FinanceService financeService = (FinanceService) applicationContext.getBean("financeService");
		
		double amount = financeService.getIncomeForMonth("sadolin", "sadolin", 2007, 1);
		endTransaction();
		assertEquals(2000.0, amount);
		
		amount = financeService.getIncomeForMonth("sadolin", "sadolin", 2008, 1);
		endTransaction();
		assertEquals(0.0, amount);
	}
	
}
