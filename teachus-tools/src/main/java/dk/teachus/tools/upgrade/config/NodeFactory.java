package dk.teachus.tools.upgrade.config;

import nanoxml.XMLElement;

public abstract class NodeFactory {

	public static Node createNode(XMLElement element) {
		Node node = null;
		
		String name = element.getName();
		
		if (name.equalsIgnoreCase("configuration")) {
			node = new Configuration();
		} else if (name.equalsIgnoreCase("tomcat")) {
			node = new TomcatNode();
		} else if (name.equalsIgnoreCase("database")) {
			node = new DatabaseNode();
		} else if (name.equalsIgnoreCase("main")) {
			node = new MainDeploymentNode();
		} else if (name.equalsIgnoreCase("demo")) {
			node = new DemoDeploymentNode();
		} else if (name.equalsIgnoreCase("ssh")) {
			node = new SshNode();
		} else if (name.equalsIgnoreCase("subversion")) {
			node = new SubversionNode();
		} else if (name.equalsIgnoreCase("maven")) {
			node = new MavenNode();
		}
		
		if (node != null) {
			node.deserialize(element);
		}
		
		return node;
	}

}
