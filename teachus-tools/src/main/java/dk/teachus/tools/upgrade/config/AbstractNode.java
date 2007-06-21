package dk.teachus.tools.upgrade.config;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Vector;

import nanoxml.XMLElement;


abstract class AbstractNode implements Node {
	
	protected static interface XMLElementVisitor {
		void visit(XMLElement element);
	}
	
	protected XMLElement findChild(XMLElement parent, String name) {
		XMLElement foundChild = null;
		
		Vector children = parent.getChildren();
		if (children != null) {
			for (Object object : children) {
				XMLElement child = (XMLElement) object;
				
				if (child.getName().equals(name)) {
					foundChild = child;
					break;
				}
			}
		}
		
		return foundChild;
	}
	
	protected void traverse(XMLElement parent, XMLElementVisitor visitor) {
		visitor.visit(parent);
		
		Vector children = parent.getChildren();
		if (children != null) {
			for (Object object : children) {
				XMLElement child = (XMLElement) object;
				
				traverse(child, visitor);
			}
		}
	}
	
	protected void getInputForString(StringNode string, String label) {
		if (string.getValue() == null) {
			string.setValue(getInput(label));
		}
	}
	
	protected String getInput(String label) {
		System.out.print(label);
		
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		
		String line = null;
		
		try {
			line = br.readLine();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		
		return line;
	}
	
	protected boolean isEmpty(String string) {
		return string == null || string.length() == 0;
	}

}
