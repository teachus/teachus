package dk.teachus.tools.actions;

import java.io.File;

import org.tmatesoft.svn.core.wc.SVNCommitClient;

public class SubversionCommitAction implements Action {
	
	private File[] files;
	private String message;

	public SubversionCommitAction(String message, File... files) {
		this.files = files;
		this.message = message;
	}
	
	public void init() throws Exception {
	}

	public void execute() throws Exception {		
		SVNCommitClient commitClient = new SVNCommitClient(null, null);
		commitClient.doCommit(files, false, message, false, false);
	}
	
	public void check() throws Exception {
		if (message == null || message.length() == 0) {
			throw new IllegalStateException("You must specify a message for the commit.");
		}
		
		if (files == null || files.length == 0) {
			throw new IllegalStateException("You have to specify at least one file to commit.");
		}
		
		for (File file : files) {
			if (file.exists() == false) {
				throw new IllegalStateException("You are trying to commit a file which doesn't exists: "+file);
			}
		}
	}
	
	public void cleanup() throws Exception {
	}

}
