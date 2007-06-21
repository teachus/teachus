package dk.teachus.tools.upgrade.config;

import nanoxml.XMLElement;

public class DatabaseNode extends AbstractNode {
	
	private StringNode host;
	private StringNode database;
	private StringNode username;
	private StringNode password;
	
	public DatabaseNode() {
		this(null, null, null, null);
	}
	
	public DatabaseNode(String host, String database, String username, String password) {
		this.host = new StringNode("host", host);
		this.database = new StringNode("dbname", database);
		this.username = new StringNode("username", username);
		this.password = new StringNode("password", password);
	}
	
	public String getJdbcUrl() {
		StringBuilder url = new StringBuilder();
		
		url.append("jdbc:mysql://");
		url.append(getHost());
		url.append("/");
		url.append(getDatabase());
		
		return url.toString();
	}

	public String getHost() {
		return this.host.getValue();
	}
	
	public void setHost(String host) {
		this.host.setValue(host);
	}
	
	public String getDatabase() {
		return this.database.getValue();
	}
	
	public void setDatabase(String database) {
		this.database.setValue(database);
	}
	
	public String getUsername() {
		return this.username.getValue();
	}
	
	public void setUsername(String username) {
		this.username.setValue(username);
	}
	
	public String getPassword() {
		return this.password.getValue();
	}
	
	public void setPassword(String password) {
		this.password.setValue(password);
	}

	public void deserialize(XMLElement element) {
		if (element.getName().equalsIgnoreCase("database")) {
			host = new StringNode();
			host.deserialize(findChild(element, "host"));
			
			database = new StringNode();
			database.deserialize(findChild(element, "dbname"));
			
			username = new StringNode();
			username.deserialize(findChild(element, "username"));
			
			password = new StringNode();
			password.deserialize(findChild(element, "password"));
		}
	}

	public XMLElement serialize() {
		XMLElement element = new XMLElement();
		element.setName("database");
		element.addChild(host.serialize());
		element.addChild(database.serialize());
		element.addChild(username.serialize());
		element.addChild(password.serialize());
		return element;
	}
	
	public void requestValue() {
		getInputForString(host, "Hostname: ");
		getInputForString(database, "Database: ");
		getInputForString(username, "Username: ");
		getInputForString(password, "Password: ");
	}

}
