package dk.teachus.tools.upgrade.config;

import nanoxml.XMLElement;

public class MavenNode extends AbstractNode {
	
	private StringNode home;
	
	public MavenNode() {
		this(null);
	}
	
	public MavenNode(String home) {
		this.home = new StringNode("home", home);
	}

	public String getHome() {
		return home.getValue();
	}

	public void setHome(String home) {
		this.home.setValue(home);
	}

	public void deserialize(XMLElement element) {
		if (element.getName().equalsIgnoreCase("maven")) {
			home = new StringNode();
			home.deserialize(findChild(element, "home"));
		}
	}

	public XMLElement serialize() {
		XMLElement element = new XMLElement();
		element.setName("maven");
		element.addChild(home.serialize());
		return element;
	}

	public void requestValue() {
		getInputForString(home, "Maven home (bin): ");
	}

}
