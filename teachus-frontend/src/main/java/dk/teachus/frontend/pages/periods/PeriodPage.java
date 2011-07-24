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
package dk.teachus.frontend.pages.periods;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebComponent;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.IValidator;
import org.apache.wicket.validation.ValidationError;
import org.apache.wicket.validation.validator.StringValidator;
import org.joda.time.DateMidnight;
import org.joda.time.DateTime;
import org.joda.time.LocalTime;

import dk.teachus.backend.dao.BookingDAO;
import dk.teachus.backend.dao.PeriodDAO;
import dk.teachus.backend.domain.Period;
import dk.teachus.backend.domain.Period.Status;
import dk.teachus.backend.domain.Periods;
import dk.teachus.backend.domain.Teacher;
import dk.teachus.backend.domain.impl.PeriodImpl.WeekDay;
import dk.teachus.backend.domain.impl.PeriodsImpl;
import dk.teachus.frontend.TeachUsApplication;
import dk.teachus.frontend.TeachUsSession;
import dk.teachus.frontend.UserLevel;
import dk.teachus.frontend.components.calendar.PeriodsCalendarPanel;
import dk.teachus.frontend.components.form.ButtonPanelElement;
import dk.teachus.frontend.components.form.CheckGroupElement;
import dk.teachus.frontend.components.form.DateElement;
import dk.teachus.frontend.components.form.DateModel;
import dk.teachus.frontend.components.form.DecimalFieldElement;
import dk.teachus.frontend.components.form.DropDownElement;
import dk.teachus.frontend.components.form.FormPanel;
import dk.teachus.frontend.components.form.IntegerFieldElement;
import dk.teachus.frontend.components.form.TextFieldElement;
import dk.teachus.frontend.pages.AuthenticatedBasePage;
import dk.teachus.frontend.utils.Formatters;
import dk.teachus.frontend.utils.PeriodStatusRenderer;
import dk.teachus.frontend.utils.TimeChoiceRenderer;
import dk.teachus.frontend.utils.WeekDayChoiceRenderer;
import dk.teachus.frontend.utils.WeekDayChoiceRenderer.Format;

public class PeriodPage extends AuthenticatedBasePage {
	private static class TimeModel extends Model<Integer> {
		private static final long serialVersionUID = 1L;
		
		private final IModel<LocalTime> nestedModel;
		
		public TimeModel(final IModel<LocalTime> nestedModel) {
			this.nestedModel = nestedModel;
		}
		
		@Override
		public Integer getObject() {
			Integer returnObject = null;
			
			final LocalTime time = nestedModel.getObject();
			if (time != null) {
				returnObject = time.getHourOfDay()*60+time.getMinuteOfHour();
			}
			
			return returnObject;
		}
		
		@Override
		public void setObject(final Integer minutesOfDay) {
			if (minutesOfDay != null) {
				LocalTime time = new LocalTime(0, 0, 0, 0).plusMinutes(minutesOfDay);
				nestedModel.setObject(time);
			}
		}
		
	}
	
	private static final long serialVersionUID = 1L;
	
	public PeriodPage(final Period period) {
		super(UserLevel.TEACHER, true);
		
		add(new Label("editPeriodTitle", TeachUsSession.get().getString("PeriodPage.editForm"))); //$NON-NLS-1$ //$NON-NLS-2$
		
		final FormPanel form = new FormPanel("form"); //$NON-NLS-1$
		add(form);
		
		// Name
		final TextFieldElement nameElement = new TextFieldElement(TeachUsSession.get().getString("General.name"), new PropertyModel(period, "name"), true); //$NON-NLS-1$ //$NON-NLS-2$
		nameElement.add(StringValidator.maximumLength(100));
		nameElement.setReadOnly(period.getStatus() != Status.DRAFT);
		form.addElement(nameElement);
		
		// Begin date
		final DateElement beginDateElement = new DateElement(TeachUsSession.get().getString("General.startDate"), new DateModel(new PropertyModel<DateMidnight>(period, "beginDate"))); //$NON-NLS-1$ //$NON-NLS-2$
		beginDateElement.setReadOnly(period.getStatus() != Status.DRAFT);
		form.addElement(beginDateElement);
		
		// End date
		final DateElement endDateElement = new DateElement(TeachUsSession.get().getString("General.endDate"), new DateModel(new PropertyModel<DateMidnight>(period, "endDate"))); //$NON-NLS-1$ //$NON-NLS-2$
		endDateElement.add(new IValidator() {
			private static final long serialVersionUID = 1L;
			
			public void validate(IValidatable validatable) {
				Object value = validatable.getValue();
				if (value != null) {
					if (value instanceof Date) {
						DateTime date = new DateTime(value);
						
						// Check if the end date conflicts with some bookings
						if (period.getId() != null) {
							BookingDAO bookingDAO = TeachUsApplication.get().getBookingDAO();
							DateTime lastBookingDate = bookingDAO.getLastBookingDate(period);
							if (lastBookingDate != null) {
								if (date.isBefore(lastBookingDate)) {
									ValidationError validationError = new ValidationError();
									String bookingConflictMessage = TeachUsSession.get().getString("PeriodPage.endDateBookingConflict");
									bookingConflictMessage = bookingConflictMessage.replace("${lastBookingDate}", Formatters.getFormatPrettyDate().print(lastBookingDate));
									validationError.setMessage(bookingConflictMessage); //$NON-NLS-1$
									validatable.error(validationError);
								}
							}
						}
					}
				}
			}
		});
		form.addElement(endDateElement);
		
		// Time elements
		final List<Integer> hours = new ArrayList<Integer>();
		DateTime dt = new DateTime().withTime(0, 0, 0, 0);
		final int day = dt.getDayOfMonth();
		while (day == dt.getDayOfMonth()) {
			hours.add(dt.getMinuteOfDay());
			dt = dt.plusMinutes(30);
		}
		
		final TimeChoiceRenderer timeChoiceRenderer = new TimeChoiceRenderer();
		
		// Start time
		final DropDownElement startTimeElement = new DropDownElement(TeachUsSession.get().getString("General.startTime"), new TimeModel(new PropertyModel<LocalTime>( //$NON-NLS-1$
				period, "startTime")), hours, timeChoiceRenderer, true); //$NON-NLS-1$
		startTimeElement.setReadOnly(period.getStatus() != Status.DRAFT);
		form.addElement(startTimeElement);
		
		// End time
		final DropDownElement endTimeElement = new DropDownElement(TeachUsSession.get().getString("General.endTime"), new TimeModel(new PropertyModel<LocalTime>(period, //$NON-NLS-1$
				"endTime")), hours, timeChoiceRenderer, true); //$NON-NLS-1$
		endTimeElement.setReadOnly(period.getStatus() != Status.DRAFT);
		form.addElement(endTimeElement);
		
		// Location
		final TextFieldElement locationElement = new TextFieldElement(TeachUsSession.get().getString("General.location"), new PropertyModel(period, "location")); //$NON-NLS-1$ //$NON-NLS-2$
		locationElement.add(StringValidator.maximumLength(100));
		locationElement.setReadOnly(period.getStatus() != Status.DRAFT);
		form.addElement(locationElement);
		
		// Price
		final DecimalFieldElement priceElement = new DecimalFieldElement(TeachUsSession.get().getString("General.price"), new PropertyModel(period, "price"), 6); //$NON-NLS-1$ //$NON-NLS-2$
		priceElement.setReadOnly(period.getStatus() != Status.DRAFT);
		priceElement.setDefaultNullValue(0.0);
		form.addElement(priceElement);
		
		// Lesson duration
		final IntegerFieldElement lessonDurationElement = new IntegerFieldElement(TeachUsSession.get().getString("General.lessonDuration"), new PropertyModel( //$NON-NLS-1$
				period, "lessonDuration"), true, 4); //$NON-NLS-1$
		lessonDurationElement.setReadOnly(period.getStatus() != Status.DRAFT);
		form.addElement(lessonDurationElement);
		
		// Interval Between Lesson Start
		final IntegerFieldElement intervalBetweenLessonStartElement = new IntegerFieldElement(TeachUsSession.get().getString(
				"General.intervalBetweenLessonStart"), new PropertyModel(period, "intervalBetweenLessonStart"), true, 4); //$NON-NLS-1$ //$NON-NLS-2$
		intervalBetweenLessonStartElement.setReadOnly(period.getStatus() != Status.DRAFT);
		form.addElement(intervalBetweenLessonStartElement);
		
		// Week days
		final CheckGroupElement weekDaysElement = new CheckGroupElement(TeachUsSession.get().getString("General.weekDays"), new PropertyModel(period, //$NON-NLS-1$
				"weekDays"), Arrays.asList(WeekDay.values()), new WeekDayChoiceRenderer(Format.LONG), true); //$NON-NLS-1$
		weekDaysElement.setReadOnly(period.getStatus() != Status.DRAFT);
		form.addElement(weekDaysElement);
		
		// Repeat every week
		final IntegerFieldElement repeatEveryWeekElement = new IntegerFieldElement(TeachUsSession.get().getString("General.repeatEveryWeek"), //$NON-NLS-1$
				new PropertyModel(period, "repeatEveryWeek")); //$NON-NLS-1$
		repeatEveryWeekElement.setReadOnly(period.getStatus() != Status.DRAFT);
//		repeatEveryWeekElement.setDefaultNullValue(1);
		form.addElement(repeatEveryWeekElement);
		
		// Status
		final List<Status> statusList = Arrays.asList(Status.values());
		final DropDownElement statusElement = new DropDownElement(TeachUsSession.get().getString("General.status"), new PropertyModel(period, "status"), statusList, new PeriodStatusRenderer()); //$NON-NLS-1$ //$NON-NLS-2$
		statusElement.setReadOnly(period.getStatus() != Status.DRAFT);
		form.addElement(statusElement);
		
		// Buttons
		form.addElement(new ButtonPanelElement() {
			private static final long serialVersionUID = 1L;
			
			@Override
			protected List<IButton> getAdditionalButtons() {
				final List<IButton> buttons = new ArrayList<IButton>();
				buttons.add(new IButton() {
					private static final long serialVersionUID = 1L;
					
					public String getValue() {
						return TeachUsSession.get().getString("General.preview"); //$NON-NLS-1$
					}
					
					public void onClick(final AjaxRequestTarget target) {
						final WebMarkupContainer preview = generatePreview(period);
						PeriodPage.this.replace(preview);
						target.addComponent(preview);
					}
				});
				return buttons;
			}
			
			@Override
			protected void onCancel() {
				getRequestCycle().setResponsePage(PeriodsPage.class);
			}
			
			@Override
			protected void onSave(final AjaxRequestTarget target) {
				final PeriodDAO periodDAO = TeachUsApplication.get().getPeriodDAO();
				
				periodDAO.save(period);
				
				getRequestCycle().setResponsePage(PeriodsPage.class);
			}
		});
		
		add(new Label("previewTitle", TeachUsSession.get().getString("General.preview"))); //$NON-NLS-1$ //$NON-NLS-2$
		
		if (period.getId() != null) {
			add(generatePreview(period));
		} else {
			add(new WebComponent("calendar").setOutputMarkupId(true).setOutputMarkupPlaceholderTag(true).setVisible(false));
		}
	}
	
	private WebMarkupContainer generatePreview(final Period period) {
		IModel<DateMidnight> dateModel = new Model<DateMidnight>() {
			private static final long serialVersionUID = 1L;

			private DateMidnight date;
			
			{
				DateMidnight beginDate = period.getBeginDate();
				if (beginDate == null) {
					beginDate = new DateMidnight();
				}
				this.date = beginDate;
			}
			
			@Override
			public DateMidnight getObject() {
				return date;
			}
			
			@Override
			public void setObject(DateMidnight date) {
				this.date = date;
			}
		};
		
		IModel<Periods> periodsModel = new LoadableDetachableModel<Periods>() {
			private static final long serialVersionUID = 1L;

			@Override
			protected Periods load() {
				final Periods periods = new PeriodsImpl();
				final ArrayList<Period> periodList = new ArrayList<Period>();
				periodList.add(period);
				periods.setPeriods(periodList);
				return periods;
			}
		};
		
		PeriodsCalendarPanel periodsCalendarPanel = new PeriodsCalendarPanel("calendar", dateModel, periodsModel) {
			private static final long serialVersionUID = 1L;

			@Override
			protected boolean isTimeSlotBookable(TimeSlot<PeriodBookingTimeSlotPayload> timeSlot) {
				return false;
			}
			
			@Override
			protected Teacher getTeacher() {
				return TeachUsSession.get().getTeacher();
			}
		};
		periodsCalendarPanel.setOutputMarkupId(true);
		return periodsCalendarPanel;
		
//		final WebMarkupContainer preview = new WebMarkupContainer("preview"); //$NON-NLS-1$
//		preview.setOutputMarkupId(true);
//		
//		final RepeatingView weekDays = new RepeatingView("weekDays"); //$NON-NLS-1$
//		preview.add(weekDays);
//		
//		final Periods periods = new PeriodsImpl();
//		final ArrayList<Period> periodList = new ArrayList<Period>();
//		periodList.add(period);
//		periods.setPeriods(periodList);
//		
//		TeachUsDate beginDate = period.getBeginDate();
//		if (beginDate == null) {
//			beginDate = TeachUsSession.get().createNewDate(new DateTime());
//		}
//		final int weekDayCount = period.getWeekDays().size();
//		final List<DatePeriod> dates = periods.generateDates(beginDate, weekDayCount);
//		if (dates.size() < weekDayCount) {
//			beginDate = beginDate.plusWeeks(1).withDayOfWeek(DateTimeConstants.MONDAY);
//			final int diff = weekDayCount - dates.size();
//			dates.addAll(periods.generateDates(beginDate, diff, true));
//		}
//		
//		for (final DatePeriod datePeriod : dates) {
//			final WebMarkupContainer weekDay = new WebMarkupContainer(weekDays.newChildId());
//			weekDays.add(weekDay);
//			
//			TeachUsDate date = datePeriod.getDate();
//			
//			weekDay.add(new PeriodDateComponent("weekDay", period, date) { //$NON-NLS-1$
//				private static final long serialVersionUID = 1L;
//				
//				@Override
//				protected int getRowSpanForTimeContent(final Period period, final TeachUsDate time) {
//					return 0;
//				}
//				
//				@Override
//				protected Component getTimeContent(final String wicketId, final Period period, final TeachUsDate time, final MarkupContainer contentContainer) {
//					return null;
//				}
//				
//				@Override
//				protected boolean shouldDisplayTimeContent(final Period period, final TeachUsDate time) {
//					return false;
//				}
//				
//				@Override
//				protected boolean shouldHideEmptyContent(final Period period, final TeachUsDate time) {
//					return false;
//				}
//				
//			});
//		}
//		
//		return preview;
	}
	
	@Override
	protected AuthenticatedPageCategory getPageCategory() {
		return AuthenticatedPageCategory.PERIODS;
	}
	
	@Override
	protected String getPageLabel() {
		return TeachUsSession.get().getString("General.periods"); //$NON-NLS-1$
	}
	
}
