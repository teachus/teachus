package dk.teachus.tools.config;

import nanoxml.XMLElement;

public class SubversionTrunkNode extends AbstractNode {
	
	private StringNode trunkUrl;
	
	public SubversionTrunkNode() {
		this(null);
	}
	
	public SubversionTrunkNode(String trunkUrl) {
		this.trunkUrl = new StringNode("trunkurl");
		this.trunkUrl.setValue(trunkUrl);
	}
	
	public String getTrunkUrl() {
		return trunkUrl.getValue();
	}

	public void setReleaseUrl(String releaseUrl) {
		this.trunkUrl.setValue(releaseUrl);
	}

	public void deserialize(XMLElement element) {
		if (element.getName().equalsIgnoreCase("svntrunk")) {
			trunkUrl = new StringNode();
			trunkUrl.deserialize(findChild(element, "trunkurl"));
		}
	}

	public XMLElement serialize() {
		XMLElement element = new XMLElement();
		element.setName("svntrunk");
		element.addChild(trunkUrl.serialize());
		return element;
	}
	
	public void requestValue() {
		getInputForString(trunkUrl, "Subversion trunk url: ");
	}

}
