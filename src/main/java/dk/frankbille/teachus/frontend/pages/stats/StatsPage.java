package dk.frankbille.teachus.frontend.pages.stats;

import java.util.ArrayList;
import java.util.List;

import wicket.markup.html.WebMarkupContainer;
import wicket.markup.html.basic.Label;
import wicket.markup.html.link.BookmarkablePageLink;
import wicket.markup.html.link.Link;
import wicket.markup.repeater.RepeatingView;

import dk.frankbille.teachus.frontend.TeachUsSession;
import dk.frankbille.teachus.frontend.pages.MenuItem;


public class StatsPage extends AbstractStatisticsPage {
	private static final long serialVersionUID = 1L;

	public StatsPage() {
		List<MenuItem> menuItems = new ArrayList<MenuItem>();
		
		menuItems.add(new MenuItem(IncomePerPupilPage.class, TeachUsSession.get().getString("General.incomePerPupil"))); //$NON-NLS-1$
		menuItems.add(new MenuItem(IncomePerPeriodPage.class, TeachUsSession.get().getString("General.incomePerPeriod"))); //$NON-NLS-1$
		
		
		RepeatingView items = new RepeatingView("items"); //$NON-NLS-1$
		add(items);
		
		for (MenuItem item : menuItems) {
			WebMarkupContainer li = new WebMarkupContainer(items.newChildId());
			items.add(li);
			
			Link link = new BookmarkablePageLink("link", item.getBookmarkablePage()); //$NON-NLS-1$
			li.add(link);
			
			link.add(new Label("label", item.getHelpText()).setRenderBodyOnly(true)); //$NON-NLS-1$
		}
	}

	@Override
	protected String getPageLabel() {
		return TeachUsSession.get().getString("General.statistics"); //$NON-NLS-1$
	}

}
