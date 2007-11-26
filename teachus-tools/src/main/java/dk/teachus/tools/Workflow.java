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
			// INIT
			log.info("Start initializing the actions");
			for (Action action : actions) {
				log.debug("Initialize action: "+action.getClass());
				action.init();
			}
			
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
		} catch (Exception e) {
			log.error("Error during processing of the workflow", e);
		} finally {
			try {
				// CLEANUP
				log.info("Start cleaning up after execution");
				for (Action action : actions) {
					log.debug("Clean up action: "+action.getClass());
					action.cleanup();
				}
			} catch(Exception e) {
				log.fatal("FATAL exception. Were not able to clean everyting up.", e);
			}
		}
	}
	
}
