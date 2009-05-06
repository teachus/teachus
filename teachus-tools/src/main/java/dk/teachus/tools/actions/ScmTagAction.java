package dk.teachus.tools.actions;

import java.io.File;

public class ScmTagAction extends AbstractScmAction {

	private File projectDirectory;

	private String version;
	
	public ScmTagAction(ScmClient scmClient, File projectDirectory) {
		super(scmClient);
		this.projectDirectory = projectDirectory;
	}
	
	public void check() throws Exception {
	}

	public void cleanup() throws Exception {
	}

	public void execute() throws Exception {
		if (version == null) {
			throw new IllegalStateException("No version specified");
		}
		
		getScmClient().tag(projectDirectory, version);
	}

	public void init() throws Exception {
	}
	
	public void setVersion(String version) {
		this.version = version;
	}

}
