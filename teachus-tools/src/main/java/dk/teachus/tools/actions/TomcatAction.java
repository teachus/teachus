package dk.teachus.tools.actions;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import dk.teachus.tools.config.TomcatNode;

public class TomcatAction implements Action {
	private static final Log log = LogFactory.getLog(TomcatAction.class);

	public static enum ProcessAction {
		START, STOP
	}
	
	private SshRemoteCommandAction remoteCommand;
	private final ProcessAction processAction;
	private final TomcatNode tomcat;

	public TomcatAction(TomcatNode tomcat, ProcessAction processAction) {
		this.tomcat = tomcat;
		this.processAction = processAction;
	}
	
	public void init() throws Exception {
		long sleep = 0;
		
		StringBuilder command = new StringBuilder();
		command.append(tomcat.getHome());
		command.append("/bin/");
		switch (processAction) {
		case START:
			command.append("startup.sh");
			break;
		case STOP:
			command.append("shutdown.sh");
			sleep = 5000;
			break;
		}
		
		remoteCommand = new SshRemoteCommandAction(tomcat.getHost(), command.toString(), sleep, processAction == ProcessAction.START);
	}

	public void execute() throws Exception {
		if (processAction == ProcessAction.START) {
			log.info("Starting tomcat on: "+tomcat.getHost().getHost());
		} else {
			log.info("Stopping tomcat on: "+tomcat.getHost().getHost());			
		}
		
		remoteCommand.execute();
	}
	
	public void check() throws Exception {
		remoteCommand.check();
	}
	
	public void cleanup() throws Exception {
		remoteCommand.cleanup();
	}

}
