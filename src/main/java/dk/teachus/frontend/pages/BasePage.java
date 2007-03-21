package dk.teachus.frontend.pages;

import java.util.List;

import org.joda.time.DateMidnight;

import wicket.behavior.HeaderContributor;
import wicket.behavior.SimpleAttributeModifier;
import wicket.markup.html.WebMarkupContainer;
import wicket.markup.html.WebPage;
import wicket.markup.html.basic.Label;
import wicket.markup.html.link.BookmarkablePageLink;
import wicket.markup.html.link.Link;
import wicket.markup.repeater.RepeatingView;
import dk.teachus.frontend.TeachUsApplication;
import dk.teachus.frontend.TeachUsSession;
import dk.teachus.frontend.utils.Resources;

public abstract class BasePage extends WebPage {	
	
	public static interface PageCategory {
	}

	boolean attached = false;
	
	public BasePage() {
		add(HeaderContributor.forCss(Resources.CSS_ANDREAS09));
		add(HeaderContributor.forCss(Resources.CSS_SCREEN));

		StringBuilder sb = new StringBuilder(TeachUsSession.get().getString("General.teachUsTitle")); //$NON-NLS-1$
		sb.append(" "); //$NON-NLS-1$
		sb.append(TeachUsApplication.get().getVersion());
		add(new Label("title", sb.toString())); //$NON-NLS-1$
		
		createMenu();		
		
		add(new Label("copyright", "2006-"+new DateMidnight().getYear()+" TeachUs Booking Systems"));
	}

	private void createMenu() {
		RepeatingView menuItems = new RepeatingView("menuItems"); //$NON-NLS-1$
		add(menuItems);
		
		List<MenuItem> menuItemsList = createMenuItems();
		if (menuItemsList != null) {
			for (MenuItem menuItem : menuItemsList) {
				WebMarkupContainer menuItemContainer = new WebMarkupContainer(menuItems.newChildId());
				menuItems.add(menuItemContainer);
				
				Link menuLink = new BookmarkablePageLink("menuLink", menuItem.getBookmarkablePage());
				menuItemContainer.add(menuLink);
				
				if (menuItem.getPageCategory() == getPageCategory()) {
					menuLink.add(new SimpleAttributeModifier("class", "current"));
				}
				
				menuLink.add(new Label("menuLabel", menuItem.getHelpText()));
			}
		}
	}

	@Override
	protected final void onAttach() {
		if (attached == false) {
			add(new Label("pageLabel", getPageLabel())); //$NON-NLS-1$
			attached = true;
		}
		
		onAttach2();
	}

	protected void onAttach2() {
	}

	protected abstract String getPageLabel();

	protected abstract PageCategory getPageCategory();
	
	protected abstract List<MenuItem> createMenuItems();
}
