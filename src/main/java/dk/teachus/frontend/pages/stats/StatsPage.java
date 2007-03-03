package dk.teachus.frontend.pages.stats;

import dk.teachus.frontend.TeachUsSession;


public class StatsPage extends AbstractStatisticsPage {
	private static final long serialVersionUID = 1L;

	public StatsPage() {

	}

	@Override
	protected String getPageLabel() {
		return TeachUsSession.get().getString("General.statistics"); //$NON-NLS-1$
	}

	@Override
	protected PageCategory getPageCategory() {
		return AuthenticatedPageCategory.STATISTICS;
	}

}
