package dk.teachus.frontend.pages.periods;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.joda.time.DateTime;

import wicket.Component;
import wicket.ajax.AjaxRequestTarget;
import wicket.markup.html.form.validation.StringValidator;
import wicket.model.AbstractModel;
import wicket.model.IModel;
import wicket.model.PropertyModel;
import dk.teachus.dao.PeriodDAO;
import dk.teachus.domain.Period;
import dk.teachus.domain.impl.PeriodImpl.WeekDay;
import dk.teachus.frontend.TeachUsApplication;
import dk.teachus.frontend.TeachUsSession;
import dk.teachus.frontend.UserLevel;
import dk.teachus.frontend.components.form.ButtonPanelElement;
import dk.teachus.frontend.components.form.CheckGroupElement;
import dk.teachus.frontend.components.form.DateElement;
import dk.teachus.frontend.components.form.DecimalFieldElement;
import dk.teachus.frontend.components.form.DropDownElement;
import dk.teachus.frontend.components.form.FormPanel;
import dk.teachus.frontend.components.form.IntegerFieldElement;
import dk.teachus.frontend.components.form.TextFieldElement;
import dk.teachus.frontend.pages.AuthenticatedBasePage;
import dk.teachus.frontend.utils.TimeChoiceRenderer;
import dk.teachus.frontend.utils.WeekDayChoiceRenderer;
import dk.teachus.frontend.utils.WeekDayChoiceRenderer.Format;

public class PeriodPage extends AuthenticatedBasePage {
	private static final long serialVersionUID = 1L;

	private static class TimeModel extends AbstractModel {
		private static final long serialVersionUID = 1L;
		
		private IModel nestedModel;

		public TimeModel(IModel nestedModel) {
			this.nestedModel = nestedModel;
		}

		public Object getObject(Component component) {
			Object returnObject = null;
			
			Object object = nestedModel.getObject(component);
			if (object != null) {
				Date date = (Date) object;
				returnObject = new DateTime(date).getMinuteOfDay();
			}
			
			return returnObject;
		}

		public void setObject(Component component, Object object) {
			if (object != null) {
				Integer minutesOfDay = (Integer) object;
				nestedModel.setObject(component, new DateTime().withTime(0, 0, 0, 0).plusMinutes(minutesOfDay).toDate());
			}
		}
		
	}
	
	public PeriodPage(final Period period) {
		super(UserLevel.TEACHER, true);
		
		FormPanel form = new FormPanel("form"); //$NON-NLS-1$
		add(form);
		
		// Name
		TextFieldElement nameElement = new TextFieldElement(TeachUsSession.get().getString("General.name"), new PropertyModel(period, "name"), true);
		nameElement.add(StringValidator.maximumLength(100));
		form.addElement(nameElement); //$NON-NLS-1$ //$NON-NLS-2$
		
		// Begin date
		form.addElement(new DateElement(TeachUsSession.get().getString("General.startDate"), new PropertyModel(period, "beginDate"))); //$NON-NLS-1$ //$NON-NLS-2$
		
		// End date
		form.addElement(new DateElement(TeachUsSession.get().getString("General.endDate"), new PropertyModel(period, "endDate"))); //$NON-NLS-1$ //$NON-NLS-2$
		
		// Time elements
		List<Integer> hours = new ArrayList<Integer>();
		DateTime dt = new DateTime().withTime(0, 0, 0, 0);
		int day = dt.getDayOfMonth();
		while (day == dt.getDayOfMonth()) {
			hours.add(dt.getMinuteOfDay());
			dt = dt.plusMinutes(30);
		}
		
		TimeChoiceRenderer timeChoiceRenderer = new TimeChoiceRenderer();
		
		// Start time
		form.addElement(new DropDownElement(TeachUsSession.get().getString("General.startTime"), new TimeModel(new PropertyModel(period, "startTime")), hours, timeChoiceRenderer, true)); //$NON-NLS-1$ //$NON-NLS-2$
		
		// End time
		form.addElement(new DropDownElement(TeachUsSession.get().getString("General.endTime"), new TimeModel(new PropertyModel(period, "endTime")), hours, timeChoiceRenderer, true)); //$NON-NLS-1$ //$NON-NLS-2$
		
		// Location
		TextFieldElement locationElement = new TextFieldElement(TeachUsSession.get().getString("General.location"), new PropertyModel(period, "location"));
		locationElement.add(StringValidator.maximumLength(100));
		form.addElement(locationElement); //$NON-NLS-1$ //$NON-NLS-2$
		
		// Price
		form.addElement(new DecimalFieldElement(TeachUsSession.get().getString("General.price"), new PropertyModel(period, "price"), 6)); //$NON-NLS-1$ //$NON-NLS-2$

		// Currency
		TextFieldElement currencyElement = new TextFieldElement("Currency", new PropertyModel(period, "currency"), 4);
		currencyElement.add(StringValidator.maximumLength(10));
		form.addElement(currencyElement);
		
		// Lesson duration
		form.addElement(new IntegerFieldElement(TeachUsSession.get().getString("General.lessonDuration"), new PropertyModel(period, "lessonDuration"), true, 4)); //$NON-NLS-1$ //$NON-NLS-2$
		
		// Interval Between Lesson Start
		form.addElement(new IntegerFieldElement(TeachUsSession.get().getString("General.intervalBetweenLessonStart"), new PropertyModel(period, "intervalBetweenLessonStart"), true, 4)); //$NON-NLS-1$ //$NON-NLS-2$
		
		// Week days
		form.addElement(new CheckGroupElement(TeachUsSession.get().getString("General.weekDays"), new PropertyModel(period, "weekDays"), Arrays.asList(WeekDay.values()), new WeekDayChoiceRenderer(Format.LONG), true)); //$NON-NLS-1$ //$NON-NLS-2$
		
		// Repeat every week
		form.addElement(new IntegerFieldElement(TeachUsSession.get().getString("General.repeatEveryWeek"), new PropertyModel(period, "repeatEveryWeek"))); //$NON-NLS-1$ //$NON-NLS-2$
		
		// Buttons
		form.addElement(new ButtonPanelElement() {
			private static final long serialVersionUID = 1L;

			@Override
			protected void onCancel(AjaxRequestTarget target) {
				getRequestCycle().setResponsePage(PeriodsPage.class);
			}

			@Override
			protected void onSave(AjaxRequestTarget target) {
				PeriodDAO periodDAO = TeachUsApplication.get().getPeriodDAO();

				periodDAO.save(period);				
				
				getRequestCycle().setResponsePage(PeriodsPage.class);
			}			
		});
	}

	@Override
	protected String getPageLabel() {
		return TeachUsSession.get().getString("General.periods"); //$NON-NLS-1$
	}

	@Override
	protected AuthenticatedPageCategory getPageCategory() {
		return AuthenticatedPageCategory.PERIODS;
	}
	
}
