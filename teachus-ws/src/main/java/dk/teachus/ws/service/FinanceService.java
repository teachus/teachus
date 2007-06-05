package dk.teachus.ws.service;


public interface FinanceService {

	double getIncomeForMonth(String teacherUserId, String password, int year, int month);
	
}
