package dk.teachus.tools.config;

import nanoxml.XMLElement;

public class MainDeploymentNode extends AbstractDeploymentNode {

	private DatabaseBackupNode backupNode = new DatabaseBackupNode();
	
	public void setBackupNode(DatabaseBackupNode backupNode) {
		this.backupNode = backupNode;
	}

	public DatabaseBackupNode getBackupNode() {
		return backupNode;
	}

	@Override
	protected String getName() {
		return "main";
	}
	
	@Override
	protected void onDeserialize(XMLElement element) {
		backupNode = new DatabaseBackupNode();
		backupNode.deserialize(findChild(element, "databasebackup"));
	}
	
	@Override
	protected void onSerialize(XMLElement element) {
		element.addChild(backupNode.serialize());
	}
	
	@Override
	public void requestValue() {
		backupNode.requestValue();
		super.requestValue();
	}

}
