package dk.teachus.tools.upgrade.actions;

import java.io.File;

import dk.teachus.tools.upgrade.config.SubversionReleaseNode;

public class SubversionCheckoutReleaseAction extends AbstractSubversionCheckoutAction {

	private SubversionReleaseNode subversionRelease;
	private String version;
	
	public SubversionCheckoutReleaseAction(SubversionReleaseNode subversionRelease, String version, File workingDirectory) {
		super(workingDirectory);
		this.subversionRelease = subversionRelease;
		this.version = version;
	}

	@Override
	protected String getCheckoutUrl() {
		return subversionRelease.getReleaseUrl(version);
	}
	
}