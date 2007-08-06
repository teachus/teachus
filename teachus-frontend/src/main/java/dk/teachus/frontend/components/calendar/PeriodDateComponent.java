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
package dk.teachus.frontend.components.calendar;

import org.apache.wicket.Component;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.behavior.SimpleAttributeModifier;
import org.apache.wicket.markup.html.WebComponent;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.model.Model;
import org.apache.wicket.util.string.Strings;
import org.joda.time.DateMidnight;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;

import dk.teachus.backend.domain.Period;
import dk.teachus.frontend.components.RenderingLabel;
import dk.teachus.frontend.utils.CurrencyChoiceRenderer;
import dk.teachus.frontend.utils.Formatters;
import dk.teachus.frontend.utils.Resources;

public abstract class PeriodDateComponent extends Panel {
	private boolean attached = false;
	protected Period period;
	protected DateMidnight date;
	
	public PeriodDateComponent(String id, final Period period, DateMidnight date) {
		super(id);
		
		this.period = period;
		this.date = date;
	}
	
	@Override
	protected void onBeforeRender() {
		super.onBeforeRender();
		
		if (attached == false) {
			// Checks
			if (period.hasDate(date) == false) {
				throw new IllegalArgumentException("Date: "+date+" is not in the period: "+period); //$NON-NLS-1$ //$NON-NLS-2$
			}	
					
			// Header
			{
				add(new Label("weekday", Formatters.getFormatWeekDay().print(date))); //$NON-NLS-1$
				add(new RenderingLabel("price", new Model(period.getPrice()), new CurrencyChoiceRenderer())); //$NON-NLS-1$
				add(new Label("location", period.getLocation()) {
					private static final long serialVersionUID = 1L;

					@Override
					public boolean isVisible() {
						return Strings.isEmpty(period.getLocation()) == false;
					}
				});
				add(new Label("date", Formatters.getFormatShortPrettyDate().print(date))); //$NON-NLS-1$
				add(new Label("duration", new Model(period.getLessonDuration())));
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
									
					WebMarkupContainer contentContainer = new WebMarkupContainer("contentContainer");
					contentContainer.setOutputMarkupId(true);
					row.add(contentContainer);
					
					// A timeentry may or may not display something in this rendering
					// depending on the implementation
					if (shouldDisplayTimeContent(period, time)) {
						contentContainer.add(getTimeContent("content", period, time, contentContainer)); //$NON-NLS-1$
						contentContainer.add(new WebComponent("emptyIcon").setVisible(false));
						
						int rowSpanForTimeContent = getRowSpanForTimeContent(period, time);
						if (rowSpanForTimeContent > 1) {
							contentContainer.add(new SimpleAttributeModifier("rowspan", ""+rowSpanForTimeContent));
						}
					} else {
						contentContainer.add(new WebComponent("content").setVisible(false));
						Image emptyIcon = new Image("emptyIcon", Resources.EMPTY);
						emptyIcon.add(new SimpleAttributeModifier("height", "17"));
						emptyIcon.add(new SimpleAttributeModifier("width", "1"));
						contentContainer.add(emptyIcon);
						contentContainer.setVisible(shouldHideEmptyContent(period, time) == false);
					}
					
					time = time.plusMinutes(period.getIntervalBetweenLessonStart());
				}
			}
			
			
			attached = true;
		}
	}
	
	protected abstract Component getTimeContent(String wicketId, Period period, DateTime time, MarkupContainer contentContainer);
	
	protected abstract boolean shouldDisplayTimeContent(Period period, DateTime time);
	
	protected abstract int getRowSpanForTimeContent(Period period, DateTime time);
	
	protected abstract boolean shouldHideEmptyContent(Period period, DateTime time);

}
