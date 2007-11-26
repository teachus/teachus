package dk.teachus.tools.actions;

import java.io.File;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.internal.io.dav.DAVRepositoryFactory;
import org.tmatesoft.svn.core.wc.SVNRevision;
import org.tmatesoft.svn.core.wc.SVNUpdateClient;

abstract class AbstractSubversionCheckoutAction implements Action {
	private static final Log log = LogFactory.getLog(AbstractSubversionCheckoutAction.class);

	private File workingDirectory;

	public AbstractSubversionCheckoutAction(File workingDirectory) {
		this.workingDirectory = workingDirectory;
	}
	
	public void init() throws Exception {
	}

	public final void execute() throws Exception {
		String checkoutUrl = getCheckoutUrl();
		
		if (log.isDebugEnabled()) {
			log.debug("Checking out project from: "+checkoutUrl);
		}
		
		DAVRepositoryFactory.setup();
		SVNURL url = SVNURL.parseURIDecoded(checkoutUrl);
		SVNUpdateClient updateClient = new SVNUpdateClient(null, null);
		updateClient.doCheckout(url, workingDirectory, SVNRevision.HEAD, SVNRevision.HEAD, true);
	}
	
	public void check() throws Exception {
		if (workingDirectory == null) {
			throw new IllegalStateException("Working directory must not be null");
		}
		
		if (workingDirectory.exists() == false) {
			throw new IllegalStateException("Working directory doesn't exist");
		}
		
		if (workingDirectory.isDirectory() == false) {
			throw new IllegalStateException("Working directory is not a directory");
		}
		
		// Check that the version exists in the repository
		String checkoutUrl = getCheckoutUrl();
		GetMethod getMethod = new GetMethod(checkoutUrl);
		int status = new HttpClient().executeMethod(getMethod);
		if (status != HttpStatus.SC_OK) {
			throw new IllegalArgumentException("The release path doesn't exist: "+checkoutUrl);
		}
	}
	
	public void cleanup() throws Exception {
	}
	
	protected abstract String getCheckoutUrl();

}
