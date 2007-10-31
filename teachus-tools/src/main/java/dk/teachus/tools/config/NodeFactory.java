package dk.teachus.tools.config;

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
		} else if (name.equalsIgnoreCase("test")) {
			node = new TestDeploymentNode();
		} else if (name.equalsIgnoreCase("ssh")) {
			node = new SshNode();
		} else if (name.equalsIgnoreCase("svnrelease")) {
			node = new SubversionReleaseNode();
		} else if (name.equalsIgnoreCase("svntrunk")) {
			node = new SubversionTrunkNode();
		} else if (name.equalsIgnoreCase("maven")) {
			node = new MavenNode();
		} else if (name.equalsIgnoreCase("workingdirectory")) {
			node = new WorkingDirectoryNode();
		}
		
		if (node != null) {
			node.deserialize(element);
		}
		
		return node;
	}

}
