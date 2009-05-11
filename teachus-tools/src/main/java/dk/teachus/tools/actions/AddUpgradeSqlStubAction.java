package dk.teachus.tools.actions;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

public class AddUpgradeSqlStubAction implements Action {

	private File projectDirectory;
	private ScmClient scmClient;
	private String version;
	
	public AddUpgradeSqlStubAction(File projectDirectory, ScmClient scmClient) {
		this.projectDirectory = projectDirectory;
		this.scmClient = scmClient;
	}
	
	public void setVersion(String version) {
		this.version = version;
	}

	public void init() throws Exception {

	}

	public void check() throws Exception {

	}

	public void cleanup() throws Exception {

	}

	public void execute() throws Exception {
		if (version == null || version.length() == 0) {
			throw new IllegalStateException("No version is set");
		}
		
		// Remove snapshot from version
		version = version.replace("-SNAPSHOT", "");
		
		String stubString = IOUtils.toString((AddUpgradeSqlStubAction.class.getResourceAsStream("DatabaseStub.sql")));
		stubString = stubString.replace("{THEVERSION}", version);
		
		File upgradeSqlFile = new File(projectDirectory, "teachus-backend/src/main/database/upgrade/"+version+".sql");
		FileUtils.writeStringToFile(upgradeSqlFile, stubString, "UTF-8");
		
		ScmCommitAction commit = new ScmCommitAction(scmClient, projectDirectory, "Creating stub for database upgrade sql file.", new File[] {
				upgradeSqlFile
		});
		commit.execute();
	}

}
