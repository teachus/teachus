package dk.teachus.tools.upgrade.actions;

import java.io.File;

import dk.teachus.tools.upgrade.config.MavenNode;

public class MavenAntAction extends AbstractMavenAction {

	public MavenAntAction(MavenNode maven, File projectDirectory) {
		super(maven, projectDirectory);
	}

	@Override
	protected String[] getGoals() {
		return new String[] {
				"clean",
				"test-compile",
				"ant:ant"
		};
	}

	@Override
	protected String getProject() {
		return "teachus-backend";
	}

}
