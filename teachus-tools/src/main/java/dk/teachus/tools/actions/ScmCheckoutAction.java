package dk.teachus.tools.actions;

import java.io.File;

public class ScmCheckoutAction extends AbstractScmAction {

	private String version;
	private File workingDirectory;
	
	public ScmCheckoutAction(ScmClient scmClient, File workingDirectory) {
		this(scmClient, null, workingDirectory);
	}
	
	public ScmCheckoutAction(ScmClient scmClient, String version, File workingDirectory) {
		super(scmClient);
		this.version = version;
		this.workingDirectory = workingDirectory;
	}

	public void check() throws Exception {
	}

	public void cleanup() throws Exception {
	}

	public void execute() throws Exception {
		getScmClient().checkout(workingDirectory, version);
	}

	public void init() throws Exception {
	}

}
