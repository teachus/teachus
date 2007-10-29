package dk.teachus.tools.config;

import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import nanoxml.XMLElement;

import org.apache.commons.io.FileUtils;

public class Configuration extends AbstractNode {
	
	private List<Node> nodes;
	
	public Configuration() {
		nodes = new ArrayList<Node>();
	}
	
	public void initialize(File preferenceFile) throws Exception {
		if (preferenceFile.exists()) {
			nodes.clear();
			XMLElement element = new XMLElement();
			element.parseFromReader(new FileReader(preferenceFile));
			deserialize(element);
		}
		
		requestValue();
		
		// Serialize the changes
		XMLElement element = serialize();
		preferenceFile.getParentFile().mkdirs();
		FileUtils.writeStringToFile(preferenceFile, element.toString(), "UTF-8");
	}

	public void requestValue() {
		for (Node node : nodes) {
			node.requestValue();
		}
	}
	
	public void add(Node node) {
		nodes.add(node);
	}
	
	@SuppressWarnings("unchecked")
	public <N extends Node> N getNode(Class<N> nodeClass) {
		N foundNode = null;
		
		for (Node node : nodes) {
			if (nodeClass.isInstance(node)) {
				foundNode = (N) node;
				break;
			}
		}
		
		return foundNode;
	}

	public void deserialize(XMLElement element) {
		if (element.getName().equalsIgnoreCase("configuration")) {
			Vector<?> children = element.getChildren();
			if (children != null) {
				for (Object object : children) {
					XMLElement child = (XMLElement) object;
					add(NodeFactory.createNode(child));
				}
			}
		}
	}

	public XMLElement serialize() {
		XMLElement element = new XMLElement();
		element.setName("configuration");
		
		for (Node node : nodes) {
			element.addChild(node.serialize());
		}
		
		return element;
	}

}
