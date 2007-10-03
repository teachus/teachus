package dk.teachus.tools.upgrade.config;

import nanoxml.XMLElement;

public abstract class AbstractDeploymentNode extends AbstractNode {
	
	private DatabaseNode database = new DatabaseNode();

	public DatabaseNode getDatabase() {
		return database;
	}

	public void setDatabase(DatabaseNode database) {
		this.database = database;
	}

	public void deserialize(XMLElement element) {
		if (element.getName().equalsIgnoreCase(getName())) {
			database = new DatabaseNode();
			database.deserialize(findChild(element, "database"));
		}
	}

	public XMLElement serialize() {
		XMLElement element = new XMLElement();
		element.setName(getName());
		element.addChild(database.serialize());
		return element;
	}
	
	public void requestValue() {
		System.out.println("Database configuration for "+getName());
		database.requestValue();
	}
	
	protected abstract String getName();

}
