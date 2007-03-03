package dk.teachus.frontend.pages.stats;

import java.util.ArrayList;
import java.util.List;

import dk.teachus.domain.Teacher;
import dk.teachus.frontend.TeachUsSession;
import dk.teachus.frontend.UserLevel;
import dk.teachus.frontend.components.Toolbar;
import dk.teachus.frontend.components.Toolbar.ToolbarItem;
import dk.teachus.frontend.pages.AuthenticatedBasePage;

public abstract class AbstractStatisticsPage extends AuthenticatedBasePage {
	
	public AbstractStatisticsPage() {
		super(UserLevel.TEACHER, true);
		
		List<ToolbarItem> items = new ArrayList<ToolbarItem>();
		
		items.add(new ToolbarItem(TeachUsSession.get().getString("General.incomePerPupil")) {
			private static final long serialVersionUID = 1L;

			@Override
			public void onEvent() {
				getRequestCycle().setResponsePage(IncomePerPupilPage.class);
			}			
		});
		
		items.add(new ToolbarItem(TeachUsSession.get().getString("General.incomePerPeriod")) {
			private static final long serialVersionUID = 1L;

			@Override
			public void onEvent() {
				getRequestCycle().setResponsePage(IncomePerPeriodPage.class);
			}			
		});
		
		add(new Toolbar("toolbar", items));
	}
	
	protected Teacher getTeacher() {
		return (Teacher) TeachUsSession.get().getPerson();
	}

}
