package dk.teachus.tools.upgrade.config;

import nanoxml.XMLElement;

public abstract class AbstractDeploymentNode extends AbstractNode {
	
	private DatabaseNode database = new DatabaseNode();
	private SmtpServerNode smtpServer = new SmtpServerNode();

	public SmtpServerNode getSmtpServer() {
		return smtpServer;
	}
	
	public void setSmtpServer(SmtpServerNode smtpServer) {
		this.smtpServer = smtpServer;
	}
	
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
			smtpServer = new SmtpServerNode();
			smtpServer.deserialize(findChild(element, "smtp"));
		}
	}

	public XMLElement serialize() {
		XMLElement element = new XMLElement();
		element.setName(getName());
		element.addChild(database.serialize());
		element.addChild(smtpServer.serialize());
		return element;
	}
	
	public void requestValue() {
		System.out.println("Database configuration for "+getName());
		database.requestValue();
		smtpServer.requestValue();
	}
	
	protected abstract String getName();

}
