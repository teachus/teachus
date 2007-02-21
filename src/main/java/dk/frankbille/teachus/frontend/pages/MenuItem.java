package dk.frankbille.teachus.frontend.pages;

import java.io.Serializable;

import wicket.ResourceReference;
import wicket.markup.html.WebPage;

public class MenuItem implements Serializable {
	private static final long serialVersionUID = 1L;

	private Class<? extends WebPage> bookmarkablePage;

	private ResourceReference icon;

	private String helpText;

	public MenuItem(Class<? extends WebPage> bookmarkablePage, String helpText) {
		this.bookmarkablePage = bookmarkablePage;
		this.helpText = helpText;
	}

	public MenuItem(Class<? extends WebPage> bookmarkablePage, ResourceReference icon, String helpText) {
		this.bookmarkablePage = bookmarkablePage;
		this.icon = icon;
		this.helpText = helpText;
	}

	public String getHelpText() {
		return helpText;
	}

	public void setHelpText(String helpText) {
		this.helpText = helpText;
	}

	public Class<? extends WebPage> getBookmarkablePage() {
		return bookmarkablePage;
	}

	public void setBookmarkablePage(Class<? extends WebPage> bookmarkablePage) {
		this.bookmarkablePage = bookmarkablePage;
	}

	public ResourceReference getIcon() {
		return icon;
	}

	public void setIcon(ResourceReference icon) {
		this.icon = icon;
	}
}
