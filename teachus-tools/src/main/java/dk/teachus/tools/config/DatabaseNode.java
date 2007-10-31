package dk.teachus.tools.config;

import java.util.Map;

import nanoxml.XMLElement;

public class DatabaseNode extends AbstractNode {
	
	private StringNode host;
	private IntegerNode port;
	private StringNode database;
	private StringNode username;
	private StringNode password;
	
	public DatabaseNode() {
		this(null, -1, null, null, null);
	}
	
	public DatabaseNode(String host, String database, String username, String password) {
		this(host, 3306, database, username, password);
	}
	
	public DatabaseNode(String host, int port, String database, String username, String password) {
		this.host = new StringNode("host", host);
		this.port = new IntegerNode("port", port);
		this.database = new StringNode("dbname", database);
		this.username = new StringNode("username", username);
		this.password = new StringNode("password", password);
	}
	
	public String getJdbcUrl() {
		return getJdbcUrl(null);
	}
	
	public String getJdbcUrl(Map<String, String> parameters) {
		StringBuilder url = new StringBuilder();
		
		url.append("jdbc:mysql://");
		url.append(getHost());
		url.append(":");
		url.append(getPort());
		url.append("/");
		url.append(getDatabase());
		
		if (parameters != null) {
			StringBuilder p = new StringBuilder();
			for (String parameter : parameters.keySet()) {
				if (p.length() == 0) {
					p.append("?");
				} else {
					p.append("&");
				}
				
				p.append(parameter);
				p.append("=");
				p.append(parameters.get(parameter));
			}
			
			url.append(p);
		}
		
		return url.toString();
	}

	public String getHost() {
		return this.host.getValue();
	}
	
	public void setHost(String host) {
		this.host.setValue(host);
	}
	
	public int getPort() {
		return this.port.getValue();
	}
	
	public void setPort(int port) {
		this.port.setValue(port);
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
			
			port = new IntegerNode();
			port.deserialize(findChild(element, "port"));
			
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
		element.addChild(port.serialize());
		element.addChild(database.serialize());
		element.addChild(username.serialize());
		element.addChild(password.serialize());
		return element;
	}
	
	public void requestValue() {
		getInputForString(host, "Hostname: ");
		getInputForInteger(port, "Port: ");
		getInputForString(database, "Database: ");
		getInputForString(username, "Username: ");
		getInputForString(password, "Password: ");
	}

	public DatabaseNode withHostPort(String host, int port) {
		return new DatabaseNode(host, port, getDatabase(), getUsername(), getPassword());
	}

}
