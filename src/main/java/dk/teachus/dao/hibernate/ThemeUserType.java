package dk.teachus.dao.hibernate;

import dk.teachus.domain.Theme;

public class ThemeUserType extends EnumUserType<Theme> {

	public ThemeUserType() {
		super(Theme.class);
	}

}
