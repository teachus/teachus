package dk.teachus.tools.actions;

import java.io.File;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

import dk.teachus.tools.config.SshNode;

abstract class AbstractSshAction implements Action {
	private static final Log log = LogFactory.getLog(AbstractSshAction.class);
	
	protected SshNode host;

	private Session session;

	public AbstractSshAction(SshNode host) {
		this.host = host;
	}
	
	public void init() throws Exception {
	}
	
	public void execute() throws Exception {
		if (log.isDebugEnabled()) {
			log.debug("Connecting to remote host: "+host.getHost());
		}
		
		createSession();
		
		doExecute(session);
	}
	
	public final void check() throws Exception {
		createSession();
		session.disconnect();
		session = null;
		
		doCheck();
	}
	
	protected void doCheck() throws Exception {}
	
	public final void cleanup() throws Exception {
		doCleanup();
		
		if (session != null) {
			session.disconnect();
		}
	}
	
	protected void doCleanup() throws Exception {}
	
	protected abstract void doExecute(Session session) throws Exception;

	private void createSession() throws JSchException {
		JSch jsch = new JSch();
		
		// Set known hosts
		File userHome = new File(System.getProperty("user.home"));
		File sshHome = new File(userHome, ".ssh");
		File knownHosts = new File(sshHome, "known_hosts");
		if (knownHosts.exists()) {
			jsch.setKnownHosts(knownHosts.getAbsolutePath());
		}
		
		jsch.addIdentity(host.getPrivateKeyPath());
		
		session = jsch.getSession(host.getUsername(), host.getHost());
		session.connect();
	}
	
}
