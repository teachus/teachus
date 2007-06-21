package dk.teachus.tools.upgrade.actions;

import java.io.File;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.internal.io.dav.DAVRepositoryFactory;
import org.tmatesoft.svn.core.wc.SVNRevision;
import org.tmatesoft.svn.core.wc.SVNUpdateClient;

import dk.teachus.tools.upgrade.config.SubversionNode;

public class SubversionCheckoutAction implements Action {
	private static final Log log = LogFactory.getLog(SubversionCheckoutAction.class);

	private SubversionNode subversion;
	private String version;
	private File workingDirectory;

	public SubversionCheckoutAction(SubversionNode subversion, String version, File workingDirectory) {
		this.subversion = subversion;
		this.version = version;
		this.workingDirectory = workingDirectory;
	}

	public void execute() throws Exception {		
		String svnRelease = subversion.getReleaseUrl();
		svnRelease = svnRelease.replace("%r", version);
		
		log.info("Checking out project from: "+svnRelease);
		
		DAVRepositoryFactory.setup();
		SVNURL url = SVNURL.parseURIDecoded(svnRelease);
		SVNUpdateClient updateClient = new SVNUpdateClient(null, null);
		updateClient.doCheckout(url, workingDirectory, SVNRevision.HEAD, SVNRevision.HEAD, true);
	}

}
