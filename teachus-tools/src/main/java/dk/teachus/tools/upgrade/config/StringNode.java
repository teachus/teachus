package dk.teachus.tools.upgrade.config;

import nanoxml.XMLElement;

public class StringNode implements Node {

	private String name;
	private String value;
	
	public StringNode() {
	}

	public StringNode(String name) {
		this.name = name;
	}

	public StringNode(String name, String value) {
		this.name = name;
		this.value = value;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getName() {
		return name;
	}

	public String getValue() {
		return value;
	}

	public XMLElement serialize() {
		XMLElement element = new XMLElement();
		element.setName(name);
		element.setContent(value);
		return element;
	}

	public void deserialize(XMLElement element) {
		name = element.getName();
		value = element.getContent();
	}
	
	public void requestValue() {
	}

}
