package dk.teachus.frontend.pages.periods;

import java.util.ArrayList;
import java.util.List;

import wicket.ResourceReference;
import wicket.ajax.AjaxRequestTarget;
import wicket.extensions.markup.html.repeater.data.table.IColumn;
import wicket.model.Model;
import dk.teachus.dao.PeriodDAO;
import dk.teachus.domain.Period;
import dk.teachus.domain.Periods;
import dk.teachus.domain.Teacher;
import dk.teachus.domain.impl.PeriodImpl;
import dk.teachus.frontend.TeachUsApplication;
import dk.teachus.frontend.TeachUsSession;
import dk.teachus.frontend.UserLevel;
import dk.teachus.frontend.components.LinkPropertyColumn;
import dk.teachus.frontend.components.ListPanel;
import dk.teachus.frontend.components.RendererPropertyColumn;
import dk.teachus.frontend.components.Toolbar;
import dk.teachus.frontend.components.Toolbar.ToolbarItem;
import dk.teachus.frontend.pages.AuthenticatedBasePage;
import dk.teachus.frontend.utils.DateChoiceRenderer;
import dk.teachus.frontend.utils.Resources;
import dk.teachus.frontend.utils.TimeChoiceRenderer;
import dk.teachus.frontend.utils.WeekDayChoiceRenderer;
import dk.teachus.frontend.utils.WeekDayChoiceRenderer.Format;

public class PeriodsPage extends AuthenticatedBasePage {
	private static final long serialVersionUID = 1L;

	public PeriodsPage() {
		super(UserLevel.TEACHER, true);
				
		final Teacher teacher = (Teacher) TeachUsSession.get().getPerson();
		PeriodDAO periodDAO = TeachUsApplication.get().getPeriodDAO();
		
		// TOOLBAR
		List<ToolbarItem> items = new ArrayList<ToolbarItem>();
		items.add(new ToolbarItem(TeachUsSession.get().getString("PeriodPage.newPeriod")) { //$NON-NLS-1$
			private static final long serialVersionUID = 1L;

			@Override
			public void onEvent(AjaxRequestTarget target) {
				Period period = new PeriodImpl();
				period.setTeacher(teacher);
				getRequestCycle().setResponsePage(new PeriodPage(period));
			}			
		});
		add(new Toolbar("toolbar", items)); //$NON-NLS-1$

		Periods periods = periodDAO.getPeriods(teacher);
		final DateChoiceRenderer dateChoiceRenderer = new DateChoiceRenderer();
		final TimeChoiceRenderer timeChoiceRenderer = new TimeChoiceRenderer();
		final WeekDayChoiceRenderer weekDayChoiceRenderer = new WeekDayChoiceRenderer(Format.SHORT);
		
		IColumn[] columns = new IColumn[] {
				new LinkPropertyColumn(new Model(TeachUsSession.get().getString("General.name")), "name") {
					private static final long serialVersionUID = 1L;

					@Override
					protected void onClick(Object rowModelObject) {
						Period period = (Period) rowModelObject;
						getRequestCycle().setResponsePage(new PeriodPage(period));
					}					
				},
				new RendererPropertyColumn(new Model(TeachUsSession.get().getString("General.startDate")), "beginDate", dateChoiceRenderer),
				new RendererPropertyColumn(new Model(TeachUsSession.get().getString("General.endDate")), "endDate", dateChoiceRenderer),
				new RendererPropertyColumn(new Model(TeachUsSession.get().getString("General.startTime")), "startTime", timeChoiceRenderer),
				new RendererPropertyColumn(new Model(TeachUsSession.get().getString("General.endTime")), "endTime", timeChoiceRenderer),
				new RendererPropertyColumn(new Model(TeachUsSession.get().getString("General.weekDays")), "weekDays", weekDayChoiceRenderer)
		};
		
		add(new ListPanel("list", columns, periods.getPeriods()));
	}

	@Override
	protected ResourceReference getPageIcon() {
		return Resources.PERIOD;
	}

	@Override
	protected String getPageLabel() {
		return TeachUsSession.get().getString("General.periods"); //$NON-NLS-1$
	}

}
