package dk.teachus.tools.actions;


abstract class AbstractScmAction implements Action {
	
	private ScmClient scmClient;

	public AbstractScmAction(ScmClient scmClient) {
		this.scmClient = scmClient;
	}
	
	protected ScmClient getScmClient() {
		return scmClient;
	}

}
