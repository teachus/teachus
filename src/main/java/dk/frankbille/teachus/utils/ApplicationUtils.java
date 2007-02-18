package dk.frankbille.teachus.utils;

import wicket.util.string.Strings;

public abstract class ApplicationUtils {

	public static String getVersion() {
		String implVersion = null;
		Package pkg = ApplicationUtils.class.getPackage();
		if (pkg != null)
		{
			implVersion = pkg.getImplementationVersion();
		}
		return Strings.isEmpty(implVersion) ? "n/a" : implVersion;
	}
	
}
