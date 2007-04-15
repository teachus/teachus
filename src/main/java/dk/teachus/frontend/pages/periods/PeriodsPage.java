package dk.teachus.frontend.pages.periods;

import java.util.ArrayList;
import java.util.List;

import wicket.extensions.markup.html.repeater.data.table.IColumn;
import wicket.model.Model;
import dk.teachus.dao.PeriodDAO;
import dk.teachus.domain.Period;
import dk.teachus.domain.Periods;
import dk.teachus.domain.Teacher;
import dk.teachus.frontend.TeachUsApplication;
import dk.teachus.frontend.TeachUsSession;
import dk.teachus.frontend.UserLevel;
import dk.teachus.frontend.components.FunctionsColumn;
import dk.teachus.frontend.components.LinkPropertyColumn;
import dk.teachus.frontend.components.ListPanel;
import dk.teachus.frontend.components.RendererPropertyColumn;
import dk.teachus.frontend.components.Toolbar;
import dk.teachus.frontend.components.FunctionsColumn.FunctionItem;
import dk.teachus.frontend.components.Toolbar.ToolbarItem;
import dk.teachus.frontend.pages.AuthenticatedBasePage;
import dk.teachus.frontend.utils.DateChoiceRenderer;
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
			public void onEvent() {
				Period period = TeachUsApplication.get().getPeriodDAO().createPeriodObject();
				period.setTeacher(teacher);
				getRequestCycle().setResponsePage(new PeriodPage(period));
			}			
		});
		add(new Toolbar("toolbar", items)); //$NON-NLS-1$

		Periods periods = periodDAO.getPeriods(teacher);
		final DateChoiceRenderer dateChoiceRenderer = new DateChoiceRenderer();
		final TimeChoiceRenderer timeChoiceRenderer = new TimeChoiceRenderer();
		final WeekDayChoiceRenderer weekDayChoiceRenderer = new WeekDayChoiceRenderer(Format.SHORT);
		
		List<FunctionItem> functions = new ArrayList<FunctionItem>();
		functions.add(new FunctionItem(TeachUsSession.get().getString("General.delete")) { //$NON-NLS-1$
			private static final long serialVersionUID = 1L;

			@Override
			public void onEvent(Object object) {
				PeriodDAO periodDAO = TeachUsApplication.get().getPeriodDAO();
				periodDAO.delete((Period) object);
				getRequestCycle().setResponsePage(PeriodsPage.class);
			}
			
			@Override
			public String getClickConfirmText(Object object) {
				Period period = (Period) object;
				String deleteText = TeachUsSession.get().getString("PeriodPage.deleteConfirm"); //$NON-NLS-1$
				deleteText = deleteText.replace("{periodname}", period.getName()); //$NON-NLS-1$
				return deleteText;
			}
		});
		
		IColumn[] columns = new IColumn[] {
				new LinkPropertyColumn(new Model(TeachUsSession.get().getString("General.name")), "name") { //$NON-NLS-1$ //$NON-NLS-2$
					private static final long serialVersionUID = 1L;

					@Override
					protected void onClick(Object rowModelObject) {
						Period period = (Period) rowModelObject;
						getRequestCycle().setResponsePage(new PeriodPage(period));
					}					
				},
				new RendererPropertyColumn(new Model(TeachUsSession.get().getString("General.startDate")), "beginDate", dateChoiceRenderer), //$NON-NLS-1$ //$NON-NLS-2$
				new RendererPropertyColumn(new Model(TeachUsSession.get().getString("General.endDate")), "endDate", dateChoiceRenderer), //$NON-NLS-1$ //$NON-NLS-2$
				new RendererPropertyColumn(new Model(TeachUsSession.get().getString("General.startTime")), "startTime", timeChoiceRenderer), //$NON-NLS-1$ //$NON-NLS-2$
				new RendererPropertyColumn(new Model(TeachUsSession.get().getString("General.endTime")), "endTime", timeChoiceRenderer), //$NON-NLS-1$ //$NON-NLS-2$
				new RendererPropertyColumn(new Model(TeachUsSession.get().getString("General.weekDays")), "weekDays", weekDayChoiceRenderer), //$NON-NLS-1$ //$NON-NLS-2$
				new FunctionsColumn(new Model(TeachUsSession.get().getString("General.functions")), functions) //$NON-NLS-1$
		};
		
		add(new ListPanel("list", columns, periods.getPeriods())); //$NON-NLS-1$
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
