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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.joda.time.DateMidnight;
import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.LocalTime;
import org.joda.time.PeriodType;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import dk.teachus.frontend.TeachUsSession;

/**
 * @param <T> TimeSlot payload
 */
public abstract class CalendarPanel<T> extends Panel {
	private static final long serialVersionUID = 1L;

	public static final DateTimeFormatter TIME_FORMAT = DateTimeFormat.forPattern("HH:mm"); //$NON-NLS-1$
	private static final DateTimeFormatter HEADER_FORMAT = DateTimeFormat.forPattern("EE d/M"); //$NON-NLS-1$
	
	public static class TimeSlot<T> implements Serializable {
		private static final long serialVersionUID = 1L;
		
		private LocalTime startTime;
		private LocalTime endTime;
		private T payload;
		
		public TimeSlot(LocalTime startTime, LocalTime endTime, T payload) {
			this.startTime = startTime;
			this.endTime = endTime;
			this.payload = payload;
		}

		public LocalTime getStartTime() {
			return startTime;
		}
		
		public LocalTime getEndTime() {
			return endTime;
		}
		
		public T getPayload() {
			return payload;
		}
	}
	
	public CalendarPanel(String id, IModel<DateMidnight> weekDateModel) {
		super(id, weekDateModel);

		/*
		 * Navigation
		 */
		Link<DateMidnight> previousWeekLink = new Link<DateMidnight>("previousWeek", weekDateModel) { //$NON-NLS-1$
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick() {
				setModelObject(getModelObject().minusWeeks(1));
			}
		};
		previousWeekLink.add(new Label("label", TeachUsSession.get().getString("CalendarPanelV2.previousWeek"))); //$NON-NLS-1$ //$NON-NLS-2$
		add(previousWeekLink);
		Link<DateMidnight> thisWeekLink = new Link<DateMidnight>("thisWeek", weekDateModel) { //$NON-NLS-1$
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick() {
				setModelObject(new DateMidnight());
			}
		};
		thisWeekLink.add(new Label("label", TeachUsSession.get().getString("CalendarPanelV2.thisWeek"))); //$NON-NLS-1$ //$NON-NLS-2$
		add(thisWeekLink);
		Link<DateMidnight> nextWeekLink = new Link<DateMidnight>("nextWeek", weekDateModel) { //$NON-NLS-1$
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick() {
				setModelObject(getModelObject().plusWeeks(1));
			}
		};
		nextWeekLink.add(new Label("label", TeachUsSession.get().getString("CalendarPanelV2.nextWeek"))); //$NON-NLS-1$ //$NON-NLS-2$
		add(nextWeekLink);
		
		/*
		 * Calendar
		 */		
		IModel<List<DateMidnight>> daysModel = new LoadableDetachableModel<List<DateMidnight>>() {
			private static final long serialVersionUID = 1L;

			@Override
			protected List<DateMidnight> load() {
				DateMidnight thisMonday = CalendarPanel.this.getModelObject().withDayOfWeek(DateTimeConstants.MONDAY);
				List<DateMidnight> days = new ArrayList<DateMidnight>();
				for (int i = 0; i < 7; i++) {
					days.add(thisMonday);
					thisMonday = thisMonday.plusDays(1);
				}
				return days;
			}
		};
		
		final IModel<List<LocalTime>> timesModel = new LoadableDetachableModel<List<LocalTime>>() {
			private static final long serialVersionUID = 1L;

			@Override
			protected List<LocalTime> load() {
				int minutesDivider = 30;
				LocalTime localTime = getCalendarStartTime();
				final List<LocalTime> times = new ArrayList<LocalTime>();
				for (int i = 0; i < calculateNumberOfCalendarHours()*(60/minutesDivider); i++) {
					times.add(localTime);
					localTime = localTime.plusMinutes(minutesDivider);
				}
				
				return times;
			}
		};
		
		// Headers
		add(new ListView<DateMidnight>("headers", daysModel) { //$NON-NLS-1$
			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(final ListItem<DateMidnight> item) {
				item.add(new Label("label", new AbstractReadOnlyModel<String>() { //$NON-NLS-1$
					private static final long serialVersionUID = 1L;

					@Override
					public String getObject() {
						return HEADER_FORMAT.withLocale(TeachUsSession.get().getLocale()).print(item.getModelObject());
					}
				}).setRenderBodyOnly(true));
			}
		});
		
		// Body
		// Times
		add(new ListView<LocalTime>("times", timesModel) { //$NON-NLS-1$
			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(final ListItem<LocalTime> item) {
				Label label = new Label("label", new AbstractReadOnlyModel<String>() { //$NON-NLS-1$
					private static final long serialVersionUID = 1L;

					@Override
					public String getObject() {
						if (item.getModelObject().getMinuteOfHour() == 0) {
							return TIME_FORMAT.withLocale(TeachUsSession.get().getLocale()).print(item.getModelObject());
						} else {
							return null;
						}
					}
				});
				item.add(label);
				
				IModel<String> appendModel = new AbstractReadOnlyModel<String>() {
					private static final long serialVersionUID = 1L;
					
					@Override
					public String getObject() {
						if (item.getModelObject().getMinuteOfHour() == 0) {
							return "timehour"; //$NON-NLS-1$
						} else {
							return null;
						}
					}
				};
				item.add(AttributeModifier.append("class", appendModel)); //$NON-NLS-1$
			}
		});

		// Days
		add(new ListView<DateMidnight>("days", daysModel) { //$NON-NLS-1$
			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(final ListItem<DateMidnight> dayItem) {
				// Times
				dayItem.add(new ListView<LocalTime>("times", timesModel) { //$NON-NLS-1$
					private static final long serialVersionUID = 1L;

					@Override
					protected void populateItem(final ListItem<LocalTime> item) {
						IModel<String> appendModel = new AbstractReadOnlyModel<String>() {
							private static final long serialVersionUID = 1L;
							
							@Override
							public String getObject() {
								if (item.getModelObject().getMinuteOfHour() == 0) {
									return "daytimehour"; //$NON-NLS-1$
								} else {
									return null;
								}
							}
						};
						item.add(AttributeModifier.append("class", appendModel)); //$NON-NLS-1$
					}
				});
				
				/*
				 * Entries
				 */
				dayItem.add(new ListView<TimeSlot<T>>("timeSlots", getTimeSlotModel(dayItem.getModelObject())) { //$NON-NLS-1$
					private static final long serialVersionUID = 1L;

					@Override
					protected void populateItem(final ListItem<TimeSlot<T>> timeSlotItem) {
						timeSlotItem.setOutputMarkupId(true);
						
						final LocalTime startTime = timeSlotItem.getModelObject().getStartTime();
						final LocalTime endTime = timeSlotItem.getModelObject().getEndTime();
						int dividerPixelHeight = 25;
						double minutesPerDivider = calculateNumberOfCalendarHours()*60 / timesModel.getObject().size();
						
						// Calculate top/y (start time)
						double minutesStart = startTime.getHourOfDay()*60+startTime.getMinuteOfHour();
						minutesStart -= getCalendarStartTime().getHourOfDay()*60 + getCalendarStartTime().getMinuteOfHour();
						double pixelStart = minutesStart/minutesPerDivider;
						long top = Math.round(pixelStart*dividerPixelHeight) - 1;
						
						// Calculate height (end time)
						final double minutesEnd = (endTime.getHourOfDay()*60+endTime.getMinuteOfHour()) - minutesStart - getCalendarStartTime().getHourOfDay()*60 + getCalendarStartTime().getMinuteOfHour();
						double pixelEnd = minutesEnd/minutesPerDivider;
						long height = Math.round(pixelEnd*dividerPixelHeight) - 1;
						
						timeSlotItem.add(AttributeModifier.replace("style", "left: 0; top: "+top+"px; height: "+height+"px;")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
						
						// Time slot content
						IModel<List<String>> timeSlotContentModel = new LoadableDetachableModel<List<String>>() {
							private static final long serialVersionUID = 1L;

							@Override
							protected List<String> load() {
								return getTimeSlotContent(dayItem.getModelObject(), timeSlotItem.getModelObject(), timeSlotItem);
							}
						};
						
						timeSlotItem.add(new ListView<String>("timeSlotContent", timeSlotContentModel) {
							private static final long serialVersionUID = 1L;

							@Override
							protected void populateItem(ListItem<String> item) {
								item.add(new Label("content", item.getModel()));
								item.add(AttributeModifier.replace("title", item.getModel()));
							}
						});
						
						// Details
						final Component dayTimeLessonDetails = createTimeSlotDetailsComponent("dayTimeLessonDetails", timeSlotItem.getModelObject());
						dayTimeLessonDetails.setOutputMarkupId(true);
						timeSlotItem.add(dayTimeLessonDetails);
						timeSlotItem.add(AttributeModifier.append("class", new AbstractReadOnlyModel<String>() {
							private static final long serialVersionUID = 1L;

							@Override
							public String getObject() {
								if (dayTimeLessonDetails.isVisible()) {
									return "popover-external";
								}
								return null;
							}
						}));
						timeSlotItem.add(AttributeModifier.replace("data-content-id", new AbstractReadOnlyModel<String>() { //$NON-NLS-1$
							private static final long serialVersionUID = 1L;
		
							@Override
							public String getObject() {
								if (dayTimeLessonDetails.isVisible()) {
									return "#"+dayTimeLessonDetails.getMarkupId(); //$NON-NLS-1$
								} else {
									return null;
								}
							}
						}));
					}
				});
			}
		});
	}
	
	@Override
	public void renderHead(IHeaderResponse response) {
		response.renderOnDomReadyJavaScript("layoutCalendar()"); //$NON-NLS-1$
	}
	
	private int calculateNumberOfCalendarHours() {
		DateTime calStart = getCalendarStartTime().toDateTimeToday();
		DateTime calEnd = getCalendarEndTime().toDateTimeToday();
		if (getCalendarEndTime().getHourOfDay() == 0 && getCalendarEndTime().getMinuteOfHour() == 0) {
			calEnd = calEnd.plusDays(1);
		}
		if (calEnd.getMinuteOfHour() > 0) {
			calEnd = calEnd.plusHours(1).withMinuteOfHour(0);
		}
		return new org.joda.time.Period(calStart, calEnd, PeriodType.hours()).getHours();
	}
	
	protected LocalTime getCalendarStartTime() {
		return new LocalTime(0, 0);
	}
	
	protected LocalTime getCalendarEndTime() {
		return new LocalTime(0, 0);
	}
	
	protected abstract IModel<List<TimeSlot<T>>> getTimeSlotModel(DateMidnight date);
	
	protected abstract List<String> getTimeSlotContent(DateMidnight date, TimeSlot<T> timeSlot, ListItem<TimeSlot<T>> timeSlotItem);
	
	protected void modifyTimeSlotItem(ListItem<TimeSlot<T>> timeSlotItem) {
	}
	
	protected Component createTimeSlotDetailsComponent(String wicketId, TimeSlot<T> timeSlot) {
		return new WebMarkupContainer(wicketId).setVisible(false);
	}
	
	public DateMidnight getModelObject() {
		return (DateMidnight) getDefaultModelObject();
	}
	
}
