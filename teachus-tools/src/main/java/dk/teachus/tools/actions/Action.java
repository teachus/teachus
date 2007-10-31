package dk.teachus.tools.actions;

public interface Action {
	
	void init() throws Exception;

	void check() throws Exception;
	
	void execute() throws Exception;
	
	void cleanup() throws Exception;
	
}
