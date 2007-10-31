package dk.teachus.tools.actions;

import java.io.File;

import dk.teachus.tools.config.DatabaseNode;
import dk.teachus.tools.config.SshNode;

public class LoadSchemaAction extends LoadSqlFileAction {

	private final File projectDirectory;

	public LoadSchemaAction(SshNode host, DatabaseNode database, File projectDirectory) {
		super(host, database);
		this.projectDirectory = projectDirectory;
	}

	@Override
	protected File getSqlFile() {
		return new File(projectDirectory, "teachus-backend/src/main/database/mysql.sql");
	}

}
