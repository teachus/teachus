package dk.teachus.tools.actions;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import dk.teachus.tools.config.MavenNode;

public class MavenPackageAction extends AbstractMavenAction {
	
	private final boolean runTests;

	public MavenPackageAction(MavenNode maven, File workingDirectory) {
		this(maven, workingDirectory, true);
	}
	
	public MavenPackageAction(MavenNode maven, File workingDirectory, boolean runTests) {
		super(maven, workingDirectory);
		this.runTests = runTests;
	}

	@Override
	protected String getProject() {
		return "teachus-parent";
	}

	@Override
	protected String[] getGoals() {
		List<String> goals = new ArrayList<String>();
		goals.add("clean");
		goals.add("package");
		if (false == runTests) {
			goals.add("-DskipTests=true");
		}
		return goals.toArray(new String[goals.size()]);
	}

}
