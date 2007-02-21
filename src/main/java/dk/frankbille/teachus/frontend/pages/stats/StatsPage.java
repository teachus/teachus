package dk.frankbille.teachus.frontend.pages.stats;

import java.util.ArrayList;
import java.util.List;

import wicket.markup.html.WebMarkupContainer;
import wicket.markup.html.basic.Label;
import wicket.markup.html.link.BookmarkablePageLink;
import wicket.markup.html.link.Link;
import wicket.markup.repeater.RepeatingView;

import dk.frankbille.teachus.frontend.pages.MenuItem;


public class StatsPage extends AbstractStatisticsPage {
	private static final long serialVersionUID = 1L;

	public StatsPage() {
		List<MenuItem> menuItems = new ArrayList<MenuItem>();
		
		menuItems.add(new MenuItem(IncomePerPupilPage.class, "Income per pupil"));
		
		
		RepeatingView items = new RepeatingView("items");
		add(items);
		
		for (MenuItem item : menuItems) {
			WebMarkupContainer li = new WebMarkupContainer(items.newChildId());
			items.add(li);
			
			Link link = new BookmarkablePageLink("link", item.getBookmarkablePage());
			li.add(link);
			
			link.add(new Label("label", item.getHelpText()));
		}
	}

	@Override
	protected String getPageLabel() {
		return "Statistik";
	}

}
