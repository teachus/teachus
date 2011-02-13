package dk.teachus.tools.config;

import nanoxml.XMLElement;

public class SshNode extends AbstractNode {
	
	private StringNode host;
	private StringNode username;
	private StringNode privateKeyPath;
	
	public SshNode() {
		this(null, null, null);
	}
	
	public SshNode(String host, String username, String privateKeyPath) {
		this.host = new StringNode("host", host);
		this.username = new StringNode("username", username);
		this.privateKeyPath = new StringNode("privatekeypath", privateKeyPath);
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
	
	public String getPrivateKeyPath() {
		return privateKeyPath.getValue();
	}
	
	public void setPrivateKeyPath(String privateKeyPath) {
		this.privateKeyPath.setValue(privateKeyPath);
	}

	public void deserialize(XMLElement element) {
		if (element.getName().equalsIgnoreCase("ssh")) {
			host = new StringNode();
			host.deserialize(findChild(element, "host"));

			username = new StringNode();
			username.deserialize(findChild(element, "username"));
			
			privateKeyPath = new StringNode();
			privateKeyPath.deserialize(findChild(element, "privatekeypath"));
		}
	}

	public XMLElement serialize() {
		XMLElement element = new XMLElement();
		element.setName("ssh");
		element.addChild(host.serialize());
		element.addChild(username.serialize());
		element.addChild(privateKeyPath.serialize());
		return element;
	}
	
	public void requestValue() {
		getInputForString(host, "Host: ");
		getInputForString(username, "Username: ");
		getInputForString(privateKeyPath, "Private key path: ");
	}

}
