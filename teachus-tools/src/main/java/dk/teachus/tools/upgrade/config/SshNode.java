package dk.teachus.tools.upgrade.config;

import nanoxml.XMLElement;

public class SshNode extends AbstractNode {
	
	private StringNode host;
	private StringNode username;
	private StringNode password;
	
	public SshNode() {
		this(null, null, null);
	}
	
	public SshNode(String host, String username, String password) {
		this.host = new StringNode("host", host);
		this.username = new StringNode("username", username);
		this.password = new StringNode("password", password);
	}

	public String getHost() {
		return host.getValue();
	}
	
	public void setHost(String host) {
		this.host.setValue(host);
	}
	
	public String getUsername() {
		return username.getValue();
	}
	
	public void setUsername(String username) {
		this.username.setValue(username);
	}
	
	public String getPassword() {
		return password.getValue();
	}
	
	public void setPassword(String password) {
		this.password.setValue(password);
	}

	public void deserialize(XMLElement element) {
		if (element.getName().equalsIgnoreCase("ssh")) {
			host = new StringNode();
			host.deserialize(findChild(element, "host"));

			username = new StringNode();
			username.deserialize(findChild(element, "username"));

			password = new StringNode();
			password.deserialize(findChild(element, "password"));
		}
	}

	public XMLElement serialize() {
		XMLElement element = new XMLElement();
		element.setName("ssh");
		element.addChild(host.serialize());
		element.addChild(username.serialize());
		element.addChild(password.serialize());
		return element;
	}
	
	public void requestValue() {
		getInputForString(host, "Host: ");
		getInputForString(username, "Username: ");
		getInputForString(password, "Password: ");
	}

}
