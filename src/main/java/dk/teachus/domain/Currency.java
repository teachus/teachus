package dk.teachus.domain;

public interface Currency {

	Long getId();
	
	Teacher getTeacher();
	
	String getLabel();
	
	boolean isBase();
	
	double getExchangeRate();
	
	void setTeacher(Teacher teacher);
	
	void setLabel(String label);
	
	void setBase(boolean base);
	
	void setExchangeRate(double exchangeRate);
	
}
