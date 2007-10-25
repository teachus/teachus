package dk.teachus.tools.upgrade.actions;

public interface Action {

	void check() throws Exception;
	
	void execute() throws Exception;
	
	void cleanup() throws Exception;
	
}
