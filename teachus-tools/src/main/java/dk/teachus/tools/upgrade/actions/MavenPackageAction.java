package dk.teachus.tools.upgrade.actions;

import java.io.File;

import dk.teachus.tools.upgrade.config.MavenNode;

public class MavenPackageAction extends AbstractMavenAction {

	public MavenPackageAction(MavenNode maven, File workingDirectory) {
		super(maven, workingDirectory);
	}

	@Override
	protected String getProject() {
		return "teachus-parent";
	}

	@Override
	protected String[] getGoals() {
		return new String[] {
				"clean",
				"package"
		};
	}

}
