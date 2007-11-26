package dk.teachus.tools.actions;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import dk.teachus.tools.config.MavenNode;

abstract class AbstractMavenAction implements Action {
	private static final Log log = LogFactory.getLog(AbstractMavenAction.class);

	private MavenNode maven;
	private File projectDirectory;

	public AbstractMavenAction(MavenNode maven, File projectDirectory) {
		this.maven = maven;
		this.projectDirectory = projectDirectory;
	}
	
	public void init() throws Exception {
	}

	public void execute() throws Exception {
		String mavenHome = maven.getHome();
		File parentDir = new File(projectDirectory, getProject());
		
		List<String> command = new ArrayList<String>();
		command.add(mavenHome);
		command.addAll(Arrays.asList(getGoals()));
		
		if (log.isDebugEnabled()) {
			log.debug("Executing maven goals: "+Arrays.toString(getGoals()));
		}
		
		ProcessBuilder processBuilder = new ProcessBuilder(command);
		processBuilder.directory(parentDir);
		processBuilder.environment().put("JAVA_HOME", System.getProperty("java.home"));
		Process maven = processBuilder.start();

		BufferedReader inputReader = new BufferedReader(new InputStreamReader(maven.getInputStream()));
		
		String line = null;
		while ((line = inputReader.readLine()) != null) {
			if (log.isDebugEnabled()) {
				log.debug(line);
			}
		}
		
		if (maven.waitFor() != 0) {
			throw new RuntimeException("Maven didn't complete correctly");
		}
	}
	
	public void check() throws Exception {
		if (new File(maven.getHome()).exists() == false) {
			throw new IllegalArgumentException("The maven binary doesn't exist: "+maven.getHome());
		}
		
		if (projectDirectory == null) {
			throw new IllegalStateException("Project directory must not be null");
		}
		
		if (projectDirectory.exists() == false) {
			throw new IllegalStateException("Project directory doesn't exist");
		}
		
		if (projectDirectory.isDirectory() == false) {
			throw new IllegalStateException("Project directory is not a directory");
		}
	}
	
	public void cleanup() throws Exception {
	}
	
	protected abstract String getProject();
	
	protected abstract String[] getGoals();

}
