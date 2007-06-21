package dk.teachus.tools.upgrade.config;

import nanoxml.XMLElement;

public abstract class AbstractDeploymentNode extends AbstractNode {
	
	private DatabaseNode database = new DatabaseNode();
	private TomcatNode tomcat = new TomcatNode();

	public DatabaseNode getDatabase() {
		return database;
	}

	public void setDatabase(DatabaseNode database) {
		this.database = database;
	}

	public TomcatNode getTomcat() {
		return tomcat;
	}

	public void setTomcat(TomcatNode tomcat) {
		this.tomcat = tomcat;
	}

	public void deserialize(XMLElement element) {
		if (element.getName().equalsIgnoreCase(getName())) {
			database = new DatabaseNode();
			database.deserialize(findChild(element, "database"));
			
			tomcat = new TomcatNode();
			tomcat.deserialize(findChild(element, "tomcat"));
		}
	}

	public XMLElement serialize() {
		XMLElement element = new XMLElement();
		element.setName(getName());
		element.addChild(database.serialize());
		element.addChild(tomcat.serialize());
		return element;
	}
	
	public void requestValue() {
		System.out.println("Database configuration for "+getName());
		database.requestValue();
		System.out.println("Tomcat configuration for "+getName());
		tomcat.requestValue();
	}
	
	protected abstract String getName();

}
