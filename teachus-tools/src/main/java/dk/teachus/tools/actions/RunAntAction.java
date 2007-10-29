package dk.teachus.tools.actions;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class RunAntAction implements Action {
	private static final Log log = LogFactory.getLog(RunAntAction.class);

	private File antFile;
	
	public RunAntAction(File antFile) {
		this.antFile = antFile;
	}

	public void execute() throws Exception {
		String[] command = new String[] {
				"ant", 
				"-f", antFile.getAbsolutePath()
		};
		
		log.info("Running ant with file: "+antFile);
		
		ProcessBuilder pb = new ProcessBuilder(command);
		pb.directory(antFile.getParentFile().getAbsoluteFile());
		pb.environment().put("JAVA_HOME", System.getProperty("java.home"));
		Process antProcess = pb.start();
		
		BufferedReader inputReader = new BufferedReader(new InputStreamReader(antProcess.getInputStream()));
		
		String line = null;
		while ((line = inputReader.readLine()) != null) {
			log.debug(line);
		}
		
		if (antProcess.waitFor() != 0) {
			throw new RuntimeException("Ant didn't complete correctly");
		}
	}
	
	public void check() throws Exception {
	}
	
	public void cleanup() throws Exception {
	}

}
