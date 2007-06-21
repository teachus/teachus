package dk.teachus.tools.upgrade.config;

import nanoxml.XMLElement;

public class SubversionNode extends AbstractNode {
	
	private StringNode releaseUrl;
	
	public SubversionNode() {
		this(null);
	}
	
	public SubversionNode(String releaseUrl) {
		this.releaseUrl = new StringNode("releaseurl");
		this.releaseUrl.setValue(releaseUrl);
	}
	
	public String getReleaseUrl() {
		return releaseUrl.getValue();
	}

	public void setReleaseUrl(String releaseUrl) {
		this.releaseUrl.setValue(releaseUrl);
	}

	public void deserialize(XMLElement element) {
		if (element.getName().equalsIgnoreCase("subversion")) {
			releaseUrl = new StringNode();
			releaseUrl.deserialize(findChild(element, "releaseurl"));
		}
	}

	public XMLElement serialize() {
		XMLElement element = new XMLElement();
		element.setName("subversion");
		element.addChild(releaseUrl.serialize());
		return element;
	}
	
	public void requestValue() {
		getInputForString(releaseUrl, "Subversion release url (%r is replaced by the version number): ");
	}

}
