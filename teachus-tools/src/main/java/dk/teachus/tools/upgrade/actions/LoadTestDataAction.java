package dk.teachus.tools.upgrade.actions;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import dk.teachus.tools.upgrade.config.DatabaseNode;
import dk.teachus.tools.upgrade.config.MavenNode;


public class LoadTestDataAction implements Action {
	private static final Log log = LogFactory.getLog(LoadTestDataAction.class);

	private File projectDirectory;
	private DatabaseNode database;
	private MavenNode maven;
	
	public LoadTestDataAction(File projectDirectory, DatabaseNode database, MavenNode maven) {
		this.projectDirectory = projectDirectory;
		this.database = database;
		this.maven = maven;
	}

	public void execute() throws Exception {		
		// First modify the test datas database properties
		ConfigureDemoDatabaseAction configureDemoDatabase = new ConfigureDemoDatabaseAction(projectDirectory, database);
		configureDemoDatabase.execute();
		
		// Generate an ant file from the maven project
		MavenAntAction mavenAnt = new MavenAntAction(maven, projectDirectory);
		mavenAnt.execute();
		
		// Copy the build file down so it can be executed directly
		File demoAntSrc = new File(projectDirectory, "teachus-backend/src/test/resources/build.xml");
		File demoAntDst = new File(projectDirectory, "teachus-backend/demodata_build.xml");
		FileUtils.copyFile(demoAntSrc, demoAntDst);
		
		log.info("Loading testdata for database: "+database.getJdbcUrl());
		
		// Run ant
		RunAntAction runAnt = new RunAntAction(demoAntDst);
		runAnt.execute();
	}
	
}