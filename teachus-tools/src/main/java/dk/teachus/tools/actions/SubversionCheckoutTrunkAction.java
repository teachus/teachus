package dk.teachus.tools.actions;

import java.io.File;

import dk.teachus.tools.config.SubversionTrunkNode;

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