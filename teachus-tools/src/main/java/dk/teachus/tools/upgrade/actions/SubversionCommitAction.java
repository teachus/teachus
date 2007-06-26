package dk.teachus.tools.upgrade.actions;

import java.io.File;

import org.tmatesoft.svn.core.wc.SVNCommitClient;

public class SubversionCommitAction implements Action {
	
	private File[] files;
	private String message;

	public SubversionCommitAction(String message, File... files) {
		this.files = files;
		this.message = message;
	}

	public void execute() throws Exception {		
		SVNCommitClient commitClient = new SVNCommitClient(null, null);
		commitClient.doCommit(files, false, message, false, false);
	}

}
