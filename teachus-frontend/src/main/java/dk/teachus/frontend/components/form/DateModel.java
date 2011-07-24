package dk.teachus.frontend.components.form;

import java.util.Date;

import org.apache.wicket.model.IModel;
import org.joda.time.DateMidnight;

/**
 * Wrapper model, that does conversion from DateMidnight to Date
 */
public class DateModel implements IModel<Date> {
	private static final long serialVersionUID = 1L;

	private IModel<DateMidnight> nestedModel;
	
	public DateModel(IModel<DateMidnight> nestedModel) {
		if (nestedModel == null) {
			throw new IllegalArgumentException("Must provide a nested model");
		}
		
		this.nestedModel = nestedModel;
	}

	public void detach() {
		nestedModel.detach();
	}
	
	public Date getObject() {
		Date date = null;
		
		DateMidnight dateMidnight = nestedModel.getObject();
		if (dateMidnight != null) {
			date = dateMidnight.toDate();
		}
		
		return date;
	}
	
	public void setObject(Date date) {
		DateMidnight dateMidnight = null;
		
		if (date != null) {
			dateMidnight = new DateMidnight(date);
		}
		
		nestedModel.setObject(dateMidnight);
	}
	
}
