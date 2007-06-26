package dk.teachus.tools.upgrade.actions;

import java.io.File;

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

	public final void execute() throws Exception {
		String checkoutUrl = getCheckoutUrl();
		
		log.info("Checking out project from: "+checkoutUrl);
		
		DAVRepositoryFactory.setup();
		SVNURL url = SVNURL.parseURIDecoded(checkoutUrl);
		SVNUpdateClient updateClient = new SVNUpdateClient(null, null);
		updateClient.doCheckout(url, workingDirectory, SVNRevision.HEAD, SVNRevision.HEAD, true);
	}
	
	protected abstract String getCheckoutUrl();

}
