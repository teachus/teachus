package dk.frankbille.teachus.frontend.pages;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.joda.time.DateTime;

import wicket.Component;
import wicket.ResourceReference;
import wicket.ajax.AjaxEventBehavior;
import wicket.ajax.AjaxRequestTarget;
import wicket.ajax.markup.html.form.AjaxSubmitButton;
import wicket.behavior.SimpleAttributeModifier;
import wicket.datetime.markup.html.form.DateTextField;
import wicket.extensions.yui.calendar.DateField;
import wicket.markup.html.basic.Label;
import wicket.markup.html.form.Button;
import wicket.markup.html.form.CheckBoxMultipleChoice;
import wicket.markup.html.form.DropDownChoice;
import wicket.markup.html.form.Form;
import wicket.markup.html.form.TextField;
import wicket.markup.html.panel.FeedbackPanel;
import wicket.model.AbstractModel;
import wicket.model.CompoundPropertyModel;
import wicket.model.IModel;
import wicket.model.Model;
import wicket.model.PropertyModel;
import dk.frankbille.teachus.dao.PeriodDAO;
import dk.frankbille.teachus.domain.Period;
import dk.frankbille.teachus.domain.impl.PeriodImpl.WeekDay;
import dk.frankbille.teachus.frontend.TeachUsApplication;
import dk.frankbille.teachus.frontend.TeachUsSession;
import dk.frankbille.teachus.frontend.UserLevel;
import dk.frankbille.teachus.frontend.utils.Resources;
import dk.frankbille.teachus.frontend.utils.TimeChoiceRenderer;
import dk.frankbille.teachus.frontend.utils.WeekDayChoiceRenderer;
import dk.frankbille.teachus.frontend.utils.WeekDayChoiceRenderer.Format;

public class PeriodPage extends AuthenticatedBasePage {
	private static final long serialVersionUID = 1L;

	private FeedbackPanel feedbackPanel;
	
	public PeriodPage(final Period period) {
		super(UserLevel.TEACHER, true);
		
		Form form = new Form("form", new CompoundPropertyModel(period)); //$NON-NLS-1$
		add(form);

		feedbackPanel = new FeedbackPanel("feedback"); //$NON-NLS-1$
		feedbackPanel.setOutputMarkupId(true);
		form.add(feedbackPanel);

		form.add(new Label("nameLabel", TeachUsSession.get().getString("General.name"))); //$NON-NLS-1$ //$NON-NLS-2$
		form.add(new TextField("name").setRequired(true)); //$NON-NLS-1$

		form.add(new Label("beginDateLabel", TeachUsSession.get().getString("General.startDate"))); //$NON-NLS-1$ //$NON-NLS-2$
		DateField beginDate = new DateField("beginDate"); //$NON-NLS-1$
		beginDate.visitChildren(DateTextField.class, new IVisitor() {
			public Object component(Component component) {
				DateTextField dateTextField = (DateTextField) component;
				dateTextField.add(new SimpleAttributeModifier("readonly", "readonly"));				 //$NON-NLS-1$ //$NON-NLS-2$
				return CONTINUE_TRAVERSAL;
			}
		});
		form.add(beginDate);

		form.add(new Label("endDateLabel", TeachUsSession.get().getString("General.endDate"))); //$NON-NLS-1$ //$NON-NLS-2$
		DateField endDate = new DateField("endDate"); //$NON-NLS-1$
		endDate.visitChildren(DateTextField.class, new IVisitor() {
			public Object component(Component component) {
				DateTextField dateTextField = (DateTextField) component;
				dateTextField.add(new SimpleAttributeModifier("readonly", "readonly"));				 //$NON-NLS-1$ //$NON-NLS-2$
				return CONTINUE_TRAVERSAL;
			}
		});
		form.add(endDate);

		List<Integer> hours = new ArrayList<Integer>();
		DateTime dt = new DateTime().withTime(0, 0, 0, 0);
		int day = dt.getDayOfMonth();
		while (day == dt.getDayOfMonth()) {
			hours.add(dt.getMinuteOfDay());
			dt = dt.plusHours(1);
		}
		TimeChoiceRenderer timeChoiceRenderer = new TimeChoiceRenderer();
		
		form.add(new Label("startTimeLabel", TeachUsSession.get().getString("General.startTime"))); //$NON-NLS-1$ //$NON-NLS-2$
		DropDownChoice startTime = new DropDownChoice("startTime", new TimeModel(new PropertyModel(period, "startTime")), hours, timeChoiceRenderer); //$NON-NLS-1$ //$NON-NLS-2$
		startTime.setRequired(true);
		startTime.setNullValid(false);
		form.add(startTime);

		form.add(new Label("endTimeLabel", TeachUsSession.get().getString("General.endTime"))); //$NON-NLS-1$ //$NON-NLS-2$
		DropDownChoice endTime = new DropDownChoice("endTime", new TimeModel(new PropertyModel(period, "endTime")), hours, timeChoiceRenderer); //$NON-NLS-1$ //$NON-NLS-2$
		endTime.setRequired(true);
		endTime.setNullValid(false);
		form.add(endTime);
		
		form.add(new Label("priceLabel", TeachUsSession.get().getString("General.price"))); //$NON-NLS-1$ //$NON-NLS-2$
		form.add(new TextField("price").setType(Double.class)); //$NON-NLS-1$

		form.add(new Label("weekDaysLabel", TeachUsSession.get().getString("General.weekDays"))); //$NON-NLS-1$ //$NON-NLS-2$
		CheckBoxMultipleChoice weekDays = new CheckBoxMultipleChoice("weekDays", Arrays.asList(WeekDay.values()), new WeekDayChoiceRenderer(Format.LONG)); //$NON-NLS-1$
		weekDays.setSuffix("<br />"); //$NON-NLS-1$
		weekDays.setRequired(true);
		form.add(weekDays);
		
		Button cancelButton = new Button("cancelButton", new Model(TeachUsSession.get().getString("PeriodPanel.regretInput"))); //$NON-NLS-1$ //$NON-NLS-2$
		cancelButton.add(new AjaxEventBehavior("onclick") { //$NON-NLS-1$
			private static final long serialVersionUID = 1L;

			@Override
			protected void onEvent(AjaxRequestTarget target) {
				getRequestCycle().setResponsePage(PeriodsPage.class);
			}			
		});
		form.add(cancelButton);
		
		AjaxSubmitButton saveButton = new AjaxSubmitButton("saveButton", form) { //$NON-NLS-1$
			private static final long serialVersionUID = 1L;

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form form) {
				PeriodDAO periodDAO = TeachUsApplication.get().getPeriodDAO();

				periodDAO.save(period);				
				
				getRequestCycle().setResponsePage(PeriodsPage.class);
			}		
			
			@Override
			protected void onError(AjaxRequestTarget target, Form form) {
				target.addComponent(feedbackPanel);
			}
		};
		saveButton.add(new SimpleAttributeModifier("value", TeachUsSession.get().getString("General.save"))); //$NON-NLS-1$ //$NON-NLS-2$
		form.add(saveButton);
	}
	
	@Override
	protected ResourceReference getPageIcon() {
		return Resources.PERIOD;
	}

	@Override
	protected String getPageLabel() {
		return TeachUsSession.get().getString("General.periods"); //$NON-NLS-1$
	}

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
	
}
