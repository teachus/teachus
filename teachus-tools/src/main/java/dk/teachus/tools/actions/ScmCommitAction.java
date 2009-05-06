package dk.teachus.tools.actions;

import java.io.File;

public class ScmCommitAction extends AbstractScmAction {

	private File projectDirectory;
	private String message;
	private File[] files;

	public ScmCommitAction(ScmClient scmClient, File projectDirectory, String message, File[] files) {
		super(scmClient);
		this.projectDirectory = projectDirectory;
		this.message = message;
		this.files = files;
	}

	public void check() throws Exception {
	}

	public void cleanup() throws Exception {
	}

	public void execute() throws Exception {
		getScmClient().commit(projectDirectory, message, files);
	}

	public void init() throws Exception {
	}

}
