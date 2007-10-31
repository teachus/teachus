package dk.teachus.tools.actions;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import dk.teachus.tools.config.DatabaseNode;
import dk.teachus.tools.config.MavenNode;
import dk.teachus.tools.config.SshNode;


public class LoadTestDataAction implements Action {
	private static final Log log = LogFactory.getLog(LoadTestDataAction.class);

	private DatabaseNode database;

	private ConfigureDemoDatabaseAction configureDemoDatabase;

	private MavenAntAction mavenAnt;

	private File demoAntSrc;

	private File demoAntDst;

	private RunAntAction runAnt;

	private SshTunnelAction dbTunnel;

	private final SshNode tunnelHost;

	private final File projectDirectory;

	private final MavenNode maven;
	
	public LoadTestDataAction(SshNode tunnelHost, File projectDirectory, DatabaseNode database, MavenNode maven) {
		this.tunnelHost = tunnelHost;
		this.projectDirectory = projectDirectory;
		this.database = database;
		this.maven = maven;
	}
	
	public void init() throws Exception {		
		dbTunnel = new SshTunnelAction(tunnelHost, 13306, database.getHost(), database.getPort());
		configureDemoDatabase = new ConfigureDemoDatabaseAction(projectDirectory, database.withHostPort("127.0.0.1", 13306));
		mavenAnt = new MavenAntAction(maven, projectDirectory);
		demoAntSrc = new File(projectDirectory, "teachus-backend/src/test/resources/build.xml");
		demoAntDst = new File(projectDirectory, "teachus-backend/demodata_build.xml");
		runAnt = new RunAntAction(demoAntDst);
	}

	public void execute() throws Exception {		
		dbTunnel.execute();
		
		configureDemoDatabase.execute();
		
		// Generate an ant file from the maven project
		mavenAnt.execute();
		
		// Copy the build file down so it can be executed directly
		FileUtils.copyFile(demoAntSrc, demoAntDst);
		
		log.info("Loading testdata for database: "+database.getJdbcUrl());
		
		// Run ant
		runAnt.execute();
		
		// Close the port again
		dbTunnel.cleanup();
	}

	public void check() throws Exception {
		dbTunnel.check();
		configureDemoDatabase.check();
		mavenAnt.check();
		runAnt.check();
	}
	
	public void cleanup() throws Exception {
		dbTunnel.cleanup();
		configureDemoDatabase.cleanup();
		mavenAnt.cleanup();
		runAnt.cleanup();
	}
	
}