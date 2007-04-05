package dk.teachus.dao.hibernate;

import java.util.List;

import dk.teachus.domain.Currency;
import dk.teachus.domain.Teacher;
import dk.teachus.domain.impl.CurrencyImpl;
import dk.teachus.domain.impl.TeacherImpl;
import dk.teachus.frontend.WicketSpringTestCase;

public class TestCurrencyDAO extends WicketSpringTestCase {
	private static final long serialVersionUID = 1L;

	public void testSetBase() {
		// Create a new teacher
		Teacher teacher = new TeacherImpl();
		teacher.setActive(true);
		teacher.setEmail("t@t.dk");
		teacher.setName("Test Teacher");
		teacher.setUsername("tttt");
		teacher.setPassword("tttt");
		getPersonDAO().save(teacher);
		
		// Create some currencies
		Currency currency1 = new CurrencyImpl();
		currency1.setBase(false);
		currency1.setExchangeRate(0.1342);
		currency1.setLabel("euro");
		currency1.setTeacher(teacher);
		getCurrencyDAO().save(currency1);
		
		Currency currency2 = new CurrencyImpl();
		currency2.setBase(false);
		currency2.setExchangeRate(1);
		currency2.setLabel("kr");
		currency2.setTeacher(teacher);
		getCurrencyDAO().save(currency2);
		
		// First check the currencies
		List<Currency> all = getCurrencyDAO().getAll(teacher);
		assertEquals(0.134, all.get(0).getExchangeRate());
		assertFalse(all.get(0).isBase());
		assertEquals(1.0, all.get(1).getExchangeRate());
		assertFalse(all.get(1).isBase());
		
		// Set the base to kr
		getCurrencyDAO().setBase(currency2);
		
		// Check the currencies again. Should still be the same
		all = getCurrencyDAO().getAll(teacher);
		assertEquals(0.134, all.get(0).getExchangeRate());
		assertFalse(all.get(0).isBase());
		assertEquals(1.0, all.get(1).getExchangeRate());
		assertTrue(all.get(1).isBase());
		
		// Now set the base to euro
		getCurrencyDAO().setBase(currency1);
		
		// Check the currencies last time. The exchange rates should have changes
		all = getCurrencyDAO().getAll(teacher);
		assertEquals(1.0, all.get(0).getExchangeRate());
		assertTrue(all.get(0).isBase());
		assertEquals(7.463, all.get(1).getExchangeRate());
		assertFalse(all.get(1).isBase());
		
	}
	
}
