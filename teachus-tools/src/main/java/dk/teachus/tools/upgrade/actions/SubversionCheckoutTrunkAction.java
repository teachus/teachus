package dk.teachus.tools.upgrade.actions;

import java.io.File;

import dk.teachus.tools.upgrade.config.SubversionTrunkNode;

public class SubversionCheckoutTrunkAction extends AbstractSubversionCheckoutAction {

	private SubversionTrunkNode subversionTrunk;
	
	public SubversionCheckoutTrunkAction(File workingDirectory, SubversionTrunkNode subversionTrunk) {
		super(workingDirectory);
		this.subversionTrunk = subversionTrunk;
	}

	@Override
	protected String getCheckoutUrl() {
		return subversionTrunk.getTrunkUrl();
	}
	
}