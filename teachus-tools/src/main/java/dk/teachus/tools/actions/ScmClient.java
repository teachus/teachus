package dk.teachus.tools.actions;

import java.io.File;

public interface ScmClient {

	void checkout(File projectDirectory, String version);
	
	void tag(File projectDirectory, String version);
	
	void commit(File projectDirectory, String message, File[] files);
	
}
