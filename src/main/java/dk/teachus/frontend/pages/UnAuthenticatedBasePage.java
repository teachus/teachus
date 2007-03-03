package dk.teachus.frontend.pages;

import java.util.ArrayList;
import java.util.List;

public abstract class UnAuthenticatedBasePage extends BasePage {

	@Override
	protected List<MenuItem> createMenuItems() {
		return new ArrayList<MenuItem>();
	}

}
