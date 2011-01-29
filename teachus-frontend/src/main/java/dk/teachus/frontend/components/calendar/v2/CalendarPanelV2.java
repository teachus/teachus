package dk.teachus.frontend.components.calendar.v2;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ResourceReference;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.behavior.HeaderContributor;
import org.apache.wicket.behavior.SimpleAttributeModifier;
import org.apache.wicket.markup.html.IHeaderContributor;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.html.resources.JavascriptResourceReference;
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

import dk.teachus.backend.domain.TeachUsDate;
import dk.teachus.frontend.TeachUsSession;
import dk.teachus.frontend.components.jquery.JQueryBehavior;

public abstract class CalendarPanelV2 extends Panel {
	private static final long serialVersionUID = 1L;

	public static final ResourceReference JS_CALENDAR = new JavascriptResourceReference(CalendarPanelV2.class, "calendar.js"); //$NON-NLS-1$
	
	private static final DateTimeFormatter TIME_FORMAT = DateTimeFormat.forPattern("HH:mm"); //$NON-NLS-1$
	private static final DateTimeFormatter HEADER_FORMAT = DateTimeFormat.forPattern("EE d/M"); //$NON-NLS-1$
	
	public static class TimeSlot implements Serializable {
		private static final long serialVersionUID = 1L;
		
		private LocalTime startTime;
		private LocalTime endTime;
		private Object payload;
		
		public TimeSlot(LocalTime startTime, LocalTime endTime, Object payload) {
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
		
		public Object getPayload() {
			return payload;
		}
	}
	
	public CalendarPanelV2(String id, IModel<TeachUsDate> weekDateModel) {
		super(id, weekDateModel);
		
		add(new JQueryBehavior());
		add(new HeaderContributor(new IHeaderContributor() {
			private static final long serialVersionUID = 1L;

			public void renderHead(IHeaderResponse response) {
				response.renderJavascriptReference(JS_CALENDAR);
				response.renderOnDomReadyJavascript("layoutCalendar()"); //$NON-NLS-1$
			}
		}));

		/*
		 * Navigation
		 */
		Link<TeachUsDate> previousWeekLink = new Link<TeachUsDate>("previousWeek", weekDateModel) { //$NON-NLS-1$
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick() {
				setModelObject(getModelObject().minusWeeks(1));
			}
		};
		previousWeekLink.add(new Label("label", TeachUsSession.get().getString("CalendarPanelV2.previousWeek"))); //$NON-NLS-1$ //$NON-NLS-2$
		add(previousWeekLink);
		Link<TeachUsDate> thisWeekLink = new Link<TeachUsDate>("thisWeek", weekDateModel) { //$NON-NLS-1$
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick() {
				setModelObject(getModelObject().withDate(new DateMidnight()));
			}
		};
		thisWeekLink.add(new Label("label", TeachUsSession.get().getString("CalendarPanelV2.thisWeek"))); //$NON-NLS-1$ //$NON-NLS-2$
		add(thisWeekLink);
		Link<TeachUsDate> nextWeekLink = new Link<TeachUsDate>("nextWeek", weekDateModel) { //$NON-NLS-1$
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
		IModel<List<TeachUsDate>> daysModel = new LoadableDetachableModel<List<TeachUsDate>>() {
			private static final long serialVersionUID = 1L;

			@Override
			protected List<TeachUsDate> load() {
				TeachUsDate thisMonday = CalendarPanelV2.this.getModelObject().withDayOfWeek(DateTimeConstants.MONDAY);
				List<TeachUsDate> days = new ArrayList<TeachUsDate>();
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
		add(new ListView<TeachUsDate>("headers", daysModel) { //$NON-NLS-1$
			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(final ListItem<TeachUsDate> item) {
				item.add(new Label("label", new AbstractReadOnlyModel<String>() { //$NON-NLS-1$
					private static final long serialVersionUID = 1L;

					@Override
					public String getObject() {
						return HEADER_FORMAT.withLocale(TeachUsSession.get().getLocale()).print(item.getModelObject().getDateMidnight());
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
				item.add(new AttributeAppender("class", true, appendModel, " ")); //$NON-NLS-1$ //$NON-NLS-2$
			}
		});

		// Days
		add(new ListView<TeachUsDate>("days", daysModel) { //$NON-NLS-1$
			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(final ListItem<TeachUsDate> dayItem) {
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
						item.add(new AttributeAppender("class", true, appendModel, " ")); //$NON-NLS-1$ //$NON-NLS-2$
					}
				});
				
				/*
				 * Entries
				 */
				dayItem.add(new ListView<TimeSlot>("timeSlots", getTimeSlotModel(dayItem.getModelObject())) { //$NON-NLS-1$
					private static final long serialVersionUID = 1L;

					@Override
					protected void populateItem(final ListItem<TimeSlot> timeSlotItem) {
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
						
						timeSlotItem.add(new SimpleAttributeModifier("style", "left: 0; top: "+top+"px; height: "+height+"px;")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
						
						// Find booking
						
						// Time slot content
						final Label timeSlotContent = new Label("timeSlotContent", getTimeSlotContentModel(dayItem.getModelObject(), timeSlotItem.getModelObject(), timeSlotItem)); //$NON-NLS-1$
						timeSlotContent.setOutputMarkupId(true);
						timeSlotItem.add(timeSlotContent);
						
						// Time label
						timeSlotItem.add(new Label("timeLabel", new AbstractReadOnlyModel<String>() { //$NON-NLS-1$
							private static final long serialVersionUID = 1L;

							@Override
							public String getObject() {
								return TIME_FORMAT.print(startTime) + "-" + TIME_FORMAT.print(endTime) + " - "+Math.round(minutesEnd)+"m"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
							}
						}));
						
						// Details
						final WebMarkupContainer dayTimeLessonContent = new WebMarkupContainer("dayTimeLessonDetails"); //$NON-NLS-1$
						dayTimeLessonContent.setOutputMarkupId(true);
						dayTimeLessonContent.setVisible(false);
						timeSlotItem.add(dayTimeLessonContent);
						timeSlotItem.add(new AttributeModifier("rel", true, new AbstractReadOnlyModel<String>() { //$NON-NLS-1$
							private static final long serialVersionUID = 1L;
		
							@Override
							public String getObject() {
								return "#"+dayTimeLessonContent.getMarkupId(); //$NON-NLS-1$
							}
						}));
					}
				});
			}
		});
	}
	
	private int calculateNumberOfCalendarHours() {
		DateTime calStart = getCalendarStartTime().toDateTimeToday();
		DateTime calEnd = getCalendarEndTime().toDateTimeToday();
		if (getCalendarEndTime().getHourOfDay() == 0 && getCalendarEndTime().getMinuteOfHour() == 0) {
			calEnd = calEnd.plusDays(1);
		}
		return new org.joda.time.Period(calStart, calEnd, PeriodType.hours()).getHours();
	}
	
	protected LocalTime getCalendarStartTime() {
		return new LocalTime(0, 0);
	}
	
	protected LocalTime getCalendarEndTime() {
		return new LocalTime(0, 0);
	}
	
	protected abstract IModel<List<TimeSlot>> getTimeSlotModel(TeachUsDate date);
	
	protected abstract IModel<String> getTimeSlotContentModel(TeachUsDate date, TimeSlot timeSlot, ListItem<TimeSlot> timeSlotItem);
	
	protected void modifyTimeSlotItem(ListItem<TimeSlot> timeSlotItem) {
	}
	
	public TeachUsDate getModelObject() {
		return (TeachUsDate) getDefaultModelObject();
	}
	
}
