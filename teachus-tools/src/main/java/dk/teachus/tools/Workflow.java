package dk.teachus.tools;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import dk.teachus.tools.actions.Action;

public class Workflow {
	private static final Log log = LogFactory.getLog(Workflow.class);

	private List<Action> actions = new ArrayList<Action>();
	
	public void addAction(Action action) {
		actions.add(action);
	}
	
	public void start() throws Exception {
		try {
			// CHECK
			log.info("Start checking the actions");
			for (Action action : actions) {
				log.debug("Check action: "+action.getClass());
				action.check();
			}
			
			// EXECUTE
			log.info("Start executing the actions");
			for (Action action : actions) {
				log.debug("Execute action: "+action.getClass());
				action.execute();
			}
		} finally {
			try {
				// CLEANUP
				log.info("Start cleaning up after execution");
				for (Action action : actions) {
					log.debug("Clean up action: "+action.getClass());
					action.cleanup();
				}
			} catch(Exception e) {
				throw new RuntimeException("FATAL exception. Were not able to clean everyting up.", e);
			}
		}
	}
	
}
