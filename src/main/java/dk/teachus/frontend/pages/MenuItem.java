package dk.teachus.frontend.pages;

import java.io.Serializable;

import wicket.markup.html.WebPage;
import dk.teachus.frontend.pages.BasePage.PageCategory;

public class MenuItem implements Serializable {
	private static final long serialVersionUID = 1L;

	private Class<? extends WebPage> bookmarkablePage;

	private String helpText;
	
	private PageCategory pageCategory;

	public MenuItem(Class<? extends WebPage> bookmarkablePage, String helpText, PageCategory pageCategory) {
		this.bookmarkablePage = bookmarkablePage;
		this.helpText = helpText;
		this.pageCategory = pageCategory;
	}

	public Class<? extends WebPage> getBookmarkablePage() {
		return bookmarkablePage;
	}

	public String getHelpText() {
		return helpText;
	}

	public PageCategory getPageCategory() {
		return pageCategory;
	}
}
