package dk.frankbille.teachus.domain;

import java.util.Date;
import java.util.List;


public interface DatePeriod {

	public abstract Date getDate();

	public abstract List<Period> getPeriods();

	public abstract void addPeriod(Period period);

}