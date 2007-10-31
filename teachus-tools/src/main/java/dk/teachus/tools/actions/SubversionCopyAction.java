package dk.teachus.tools.actions;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.internal.io.dav.DAVRepositoryFactory;
import org.tmatesoft.svn.core.internal.io.fs.FSRepositoryFactory;
import org.tmatesoft.svn.core.wc.SVNCopyClient;
import org.tmatesoft.svn.core.wc.SVNRevision;

import dk.teachus.tools.config.SubversionReleaseNode;
import dk.teachus.tools.config.SubversionTrunkNode;

public class SubversionCopyAction implements Action {
	private static final Log log = LogFactory.getLog(SubversionCopyAction.class);
	
	private SubversionReleaseNode subversionRelease;
	private SubversionTrunkNode subversionTrunk;
	private String version;

	public SubversionCopyAction(SubversionReleaseNode subversionRelease, SubversionTrunkNode subversionTrunk) {
		this.subversionRelease = subversionRelease;
		this.subversionTrunk = subversionTrunk;
	}
	
	public void init() throws Exception {
	}
	
	public void setVersion(String version) {
		this.version = version;
	}

	public void execute() throws Exception {
		String trunkUrl = subversionTrunk.getTrunkUrl();
		String releaseUrl = subversionRelease.getReleaseUrl(version);
		
		log.info("[SVN] Copy from "+trunkUrl+" to "+releaseUrl);
		
		FSRepositoryFactory.setup();
		DAVRepositoryFactory.setup();
		
		SVNURL srcUrl = SVNURL.parseURIDecoded(trunkUrl);
		SVNURL dstUrl = SVNURL.parseURIDecoded(releaseUrl);
		
		SVNCopyClient copyClient = new SVNCopyClient(null, null);
		copyClient.doCopy(srcUrl, SVNRevision.HEAD, dstUrl, false, true, "Release "+version);		
	}
	
	public void check() throws Exception {
		
	}
	
	public void cleanup() throws Exception {
		
	}

}
