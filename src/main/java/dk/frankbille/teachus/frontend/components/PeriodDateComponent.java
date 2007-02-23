package dk.frankbille.teachus.frontend.components;

import org.joda.time.DateMidnight;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;

import wicket.Component;
import wicket.markup.html.WebMarkupContainer;
import wicket.markup.html.basic.Label;
import wicket.markup.html.panel.Panel;
import wicket.markup.repeater.RepeatingView;
import wicket.model.Model;
import dk.frankbille.teachus.domain.Period;
import dk.frankbille.teachus.frontend.utils.CurrencyChoiceRenderer;
import dk.frankbille.teachus.frontend.utils.Formatters;

public abstract class PeriodDateComponent extends Panel {
	private boolean attached = false;
	private Period period;
	private DateMidnight date;
	
	public PeriodDateComponent(String id, final Period period, DateMidnight date) {
		super(id);
		
		this.period = period;
		this.date = date;
	}
	
	@Override
	protected void onAttach() {
		if (attached == false) {
			// Checks
			if (period.hasDate(date.toDate()) == false) {
				throw new IllegalArgumentException("Date: "+date+" is not in the period: "+period); //$NON-NLS-1$ //$NON-NLS-2$
			}	
					
			// Header
			{
				add(new Label("weekday", Formatters.getFormatWeekDay().print(date))); //$NON-NLS-1$
				add(new RenderingLabel("price", new Model(period.getPrice()), new CurrencyChoiceRenderer())); //$NON-NLS-1$
				add(new Label("date", Formatters.getFormatPrettyDate().print(date))); //$NON-NLS-1$
			}
			
			// Body
			{
				RepeatingView rows = new RepeatingView("rows"); //$NON-NLS-1$
				add(rows);
				
				DateTime time = new DateTime(period.getStartTime()).withDate(date.getYear(), date.getMonthOfYear(), date.getDayOfMonth());
				DateTime end = new DateTime(period.getEndTime()).withDate(date.getYear(), date.getMonthOfYear(), date.getDayOfMonth());
	
				DateTimeFormatter timeFormat = Formatters.getFormatTime();
				
				while(time.isBefore(end)) {
					final WebMarkupContainer row = new WebMarkupContainer(rows.newChildId());
					rows.add(row);
					
					row.add(new Label("hour", timeFormat.print(time))); //$NON-NLS-1$
									
					row.add(getTimeContent("content", period, time)); //$NON-NLS-1$
					
					time = time.plusHours(1);
				}
			}
			
			
			attached = true;
		}
	}
	
	protected abstract Component getTimeContent(String wicketId, Period period, DateTime time);

}
