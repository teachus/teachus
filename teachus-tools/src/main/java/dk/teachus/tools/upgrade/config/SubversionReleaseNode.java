package dk.teachus.tools.upgrade.config;

import nanoxml.XMLElement;

public class SubversionReleaseNode extends AbstractNode {
	
	private StringNode releaseUrl;
	
	public SubversionReleaseNode() {
		this(null);
	}
	
	public SubversionReleaseNode(String releaseUrl) {
		this.releaseUrl = new StringNode("releaseurl");
		this.releaseUrl.setValue(releaseUrl);
	}
	
	public String getReleaseUrl(String version) {
		String svnRelease = getReleaseUrl();
		svnRelease = svnRelease.replace("%r", version);
		return svnRelease;
	}
	
	public String getReleaseUrl() {
		return releaseUrl.getValue();
	}

	public void setReleaseUrl(String releaseUrl) {
		this.releaseUrl.setValue(releaseUrl);
	}

	public void deserialize(XMLElement element) {
		if (element.getName().equalsIgnoreCase("svnrelease")) {
			releaseUrl = new StringNode();
			releaseUrl.deserialize(findChild(element, "releaseurl"));
		}
	}

	public XMLElement serialize() {
		XMLElement element = new XMLElement();
		element.setName("svnrelease");
		element.addChild(releaseUrl.serialize());
		return element;
	}
	
	public void requestValue() {
		getInputForString(releaseUrl, "Subversion release url (%r is replaced by the version number): ");
	}

}
