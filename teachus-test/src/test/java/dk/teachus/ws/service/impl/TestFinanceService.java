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
