package dk.teachus.tools.config;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import nanoxml.XMLElement;

public class IntegerNode implements Node {
	
	private static final Pattern INT_PATTERN = Pattern.compile("^-?[0-9]+$");
	
	private String name;
	private int value = -1;
	
	public IntegerNode() {
	}
	
	public IntegerNode(String name) {
		this.name = name;
	}

	public IntegerNode(String name, int value) {
		this.name = name;
		this.value = value;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}
	
	public void setValue(String value) {
		this.value = -1;
		Matcher matcher = INT_PATTERN.matcher(value);
		if (matcher.matches()) {
			this.value = Integer.parseInt(value);
		}
	}

	public void deserialize(XMLElement element) {
		name = element.getName();		
		setValue(element.getContent());
	}

	public void requestValue() {
	}

	public XMLElement serialize() {
		XMLElement element = new XMLElement();
		element.setName(name);
		element.setContent(""+value);
		return element;
	}

}
