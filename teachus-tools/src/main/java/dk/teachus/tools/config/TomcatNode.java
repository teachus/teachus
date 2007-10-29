package dk.teachus.tools.config;

import nanoxml.XMLElement;

public class TomcatNode extends AbstractNode {

	private StringNode home;
	private SshNode host;
	
	public TomcatNode() {
		this(null, null);
	}
	
	public TomcatNode(String homeString, SshNode host) {
		home = new StringNode();
		home.setName("home");
		home.setValue(homeString);
		
		if (host == null) {
			host = new SshNode();
		}
		
		this.host = host;
	}
	
	public String getHome() {
		return home.getValue();
	}

	public void setHome(String home) {
		this.home.setValue(home);
	}

	public SshNode getHost() {
		return host;
	}

	public void setHost(SshNode host) {
		this.host = host;
	}

	public void deserialize(XMLElement element) {
		if (element.getName().equals("tomcat")) {		
			home = new StringNode();
			home.deserialize(findChild(element, "home"));
			
			host = new SshNode();
			host.deserialize(findChild(element, "ssh"));
		}
	}

	public XMLElement serialize() {
		XMLElement element = new XMLElement();
		element.setName("tomcat");
		element.addChild(home.serialize());
		element.addChild(host.serialize());
		return element;
	}
	
	public void requestValue() {
		getInputForString(home, "Tomcat home: ");
		System.out.println("Tomcat host:");
		host.requestValue();
	}

}
