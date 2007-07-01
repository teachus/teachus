package dk.teachus.frontend.pages.stats.admin;

import java.util.List;

import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.model.Model;

import dk.teachus.backend.domain.TeacherStatistics;
import dk.teachus.frontend.TeachUsApplication;
import dk.teachus.frontend.TeachUsSession;
import dk.teachus.frontend.components.ListPanel;

public class TeachersSummaryPage extends AbstractAdminStatisticsPage {
	private static final long serialVersionUID = 1L;

	public TeachersSummaryPage() {
		IColumn[] columns = new IColumn[] {
				new PropertyColumn(new Model(TeachUsSession.get().getString("General.teacher")), "teacher.name"), //$NON-NLS-1$ //$NON-NLS-2$
				new PropertyColumn(new Model(TeachUsSession.get().getString("TeachersSummaryPage.numberOfPupils")), "pupilCount"), //$NON-NLS-1$ //$NON-NLS-2$
				new PropertyColumn(new Model(TeachUsSession.get().getString("TeachersSummaryPage.numberOfPeriods")), "periodCount"), //$NON-NLS-1$ //$NON-NLS-2$
				new PropertyColumn(new Model(TeachUsSession.get().getString("TeachersSummaryPage.numberOfPupilBookings")), "pupilBookingCount"), //$NON-NLS-1$ //$NON-NLS-2$
				new PropertyColumn(new Model(TeachUsSession.get().getString("TeachersSummaryPage.numberOfTeacherBookings")), "teacherBookingCount"), //$NON-NLS-1$ //$NON-NLS-2$
				new PropertyColumn(new Model(TeachUsSession.get().getString("TeachersSummaryPage.numberOfBookings")), "totalBookingCount"), //$NON-NLS-1$ //$NON-NLS-2$
		};

		
		
		List<TeacherStatistics> data = TeachUsApplication.get().getStatisticsDAO().getTeachers();

		add(new ListPanel("teachersSummary", columns, data)); //$NON-NLS-1$
	}

	@Override
	protected String getPageLabel() {
		return TeachUsSession.get().getString("General.teachersSummary"); //$NON-NLS-1$
	}

}
